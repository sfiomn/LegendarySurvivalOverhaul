package sfiomn.legendarysurvivaloverhaul.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.common.tileentities.CoolerTileEntity;
import sfiomn.legendarysurvivaloverhaul.common.tileentities.HeaterTileEntity;
import sfiomn.legendarysurvivaloverhaul.registry.TileEntityRegistry;

import javax.annotation.Nullable;

public class ThermalBlock extends HorizontalBlock
{
	public static final BooleanProperty LIT = BlockStateProperties.LIT;
	private static final VoxelShape[] SHAPES = new VoxelShape[]
			{
					Block.box(0.0d, 0.0d, 0.0d, 16.0d, 16.0d, 16.0d), // DOWN
					Block.box(0.0d, 0.0d, 0.0d, 16.0d, 16.0d, 16.0d), // UP
					Block.box(0.0d, 0.0d, 0.0d, 16.0d, 16.0d, 16.0d), // NORTH
					Block.box(0.0d, 0.0d, 0.0d, 16.0d, 16.0d, 16.0d), // SOUTH
					Block.box(0.0d, 0.0d, 0.0d, 16.0d,  16.0d, 16.0d), // WEST
					Block.box(0.0d, 0.0d, 0.0d, 16.0d, 16.0d, 16.0d), // EAST
			};

	public final ThermalTypeEnum thermalType;
	
	public ThermalBlock(ThermalTypeEnum thermalType, Properties properties)
	{
		super(properties);
		this.thermalType = thermalType;
		
		this.registerDefaultState(this.getStateDefinition().any()
				.setValue(FACING, Direction.NORTH)
				.setValue(LIT, Boolean.FALSE));
	}
	
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
	}
	
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(FACING, LIT);
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
		this.interactWith(world, pos, player);
		return ActionResultType.CONSUME;
	}

	private void interactWith(World world, BlockPos pos, PlayerEntity player) {
		TileEntity tileEntity = world.getBlockEntity(pos);

		if (tileEntity instanceof HeaterTileEntity && player instanceof ServerPlayerEntity) {
			HeaterTileEntity te = (HeaterTileEntity) tileEntity;
			NetworkHooks.openGui((ServerPlayerEntity) player, te, pos);
		} else if (tileEntity instanceof CoolerTileEntity && player instanceof ServerPlayerEntity) {
			CoolerTileEntity te = (CoolerTileEntity) tileEntity;
			NetworkHooks.openGui((ServerPlayerEntity) player, te, pos);
		} else {
			throw new IllegalStateException("Tile entity container is missing!");
		}
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		if (this.thermalType == ThermalTypeEnum.COOLING) {
			return TileEntityRegistry.COOLER_TILE_ENTITY.get().create();
		} else {
			return TileEntityRegistry.HEATER_TILE_ENTITY.get().create();
		}
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
