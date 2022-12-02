package sfiomn.legendarysurvivaloverhaul.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.containers.SewingTableContainer;
import sfiomn.legendarysurvivaloverhaul.registry.TileEntityRegistry;

import javax.annotation.Nullable;

public class SewingTableBlock extends HorizontalBlock
{
	private static final ITextComponent CONTAINER_TITLE = new TranslationTextComponent("container." + LegendarySurvivalOverhaul.MOD_ID + ".sewing_table");
	private static final VoxelShape[] SHAPES = new VoxelShape[]
			{
					Block.box(0.0d, 0.0d, 0.0d, 16.0d, 13.0d, 16.0d), // Block oriented DOWN
					Block.box(0.0d, 0.0d, 0.0d, 16.0d, 13.0d, 16.0d), // Block oriented UP
					Block.box(0.0d, 0.0d, 4.25d, 16.0d, 13.0d, 11.75d), // Block oriented NORTH
					Block.box(0.0d, 0.0d, 4.25d, 16.0d, 13.0d, 11.75d), // Block oriented SOUTH
					Block.box(4.25d, 0.0d, 0.0d, 11.75d, 13.0d, 16.0d), // Block oriented WEST
					Block.box(4.25d, 0.0d, 0.0d, 11.75d, 13.0d, 16.0d), // Block oriented EAST
			};

	public SewingTableBlock()
	{
		super(Properties
				.of(Material.WOOD)
				.strength(4.0f, 10.0f)
				.harvestTool(ToolType.AXE)
				.harvestLevel(1)
				.noOcclusion());
		
		this.registerDefaultState(this.getStateDefinition().any()
				.setValue(FACING, Direction.NORTH));
	}
	
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}
	
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(FACING);
	}
	
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SHAPES[state.getValue(FACING).get3DDataValue()];
	}

	@SuppressWarnings("deprecation")
	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTrace) {
		if (world.isClientSide) {
			return ActionResultType.SUCCESS;
		}
		player.openMenu(state.getMenuProvider(world, pos));
		return ActionResultType.CONSUME;
	}

	@Override
	public INamedContainerProvider getMenuProvider(BlockState state, World world, BlockPos pos) {
		return new SimpleNamedContainerProvider((windowId, playerInventory, packetBuffer) ->
				new SewingTableContainer(windowId, playerInventory, IWorldPosCallable.create(world, pos)), CONTAINER_TITLE);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return false;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return TileEntityRegistry.SEWING_TABLE_TILE_ENTITY.get().create();
	}

	@Override
	public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			TileEntity tileEntity = world.getBlockEntity(pos);
			if (tileEntity instanceof IInventory) {
				InventoryHelper.dropContents(world, pos, (IInventory) tileEntity);
				world.updateNeighbourForOutputSignal(pos, this);
			}
			super.onRemove(state, world, pos, newState, isMoving);
		}
	}
}
