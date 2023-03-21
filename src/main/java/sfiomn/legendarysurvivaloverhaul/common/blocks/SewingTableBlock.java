package sfiomn.legendarysurvivaloverhaul.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.containers.SewingTableContainer;

public class SewingTableBlock extends HorizontalBlock
{
	private static final ITextComponent CONTAINER_TITLE = new TranslationTextComponent("container." + LegendarySurvivalOverhaul.MOD_ID + ".sewing_table").withStyle();


	private static final VoxelShape BASE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
	private static final VoxelShape X_TOP = Block.box(6.0D, 1.0D, 2.0D, 10.0D, 10.0D, 14.0D);
	private static final VoxelShape Z_TOP = Block.box(2.0D, 1.0D, 6.0D, 14.0D, 10.0D, 10.0D);

	private static final VoxelShape X_AXIS_AABB = VoxelShapes.or(BASE, X_TOP);
	private static final VoxelShape Z_AXIS_AABB = VoxelShapes.or(BASE, Z_TOP);

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
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
	}
	
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(FACING);
	}
	
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		Direction direction = state.getValue(FACING);
		return direction.getAxis() == Direction.Axis.X ? X_AXIS_AABB : Z_AXIS_AABB;
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
}
