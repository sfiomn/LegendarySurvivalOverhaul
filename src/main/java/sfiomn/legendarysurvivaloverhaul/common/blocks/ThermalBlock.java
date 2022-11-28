package sfiomn.legendarysurvivaloverhaul.common.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.common.tileentity.CoolerTileEntity;
import sfiomn.legendarysurvivaloverhaul.common.tileentity.HeaterTileEntity;
import sfiomn.legendarysurvivaloverhaul.registry.TileEntityRegistry;

import javax.annotation.Nullable;
import java.util.Random;

public class ThermalBlock extends HorizontalBlock
{
	public static final BooleanProperty LIT = BlockStateProperties.LIT;
	private static final VoxelShape[] SHAPES = new VoxelShape[]
			{
					Block.box(4.25d, 0.0d, 4.25d, 11.75d, 16.0d, 11.75d), // DOWN
					Block.box(4.25d, 0.0d, 4.25d, 11.75d, 16.0d, 11.75d), // UP
					Block.box(4.25d, 4.25d, 0.0d, 11.75d, 11.75d, 16.0d), // NORTH
					Block.box(4.25d, 4.25d, 0.0d, 11.75d, 11.75d, 16.0d), // SOUTH
					Block.box(0.0d, 4.25d, 4.25d, 16.0d,  11.75d, 11.75d), // WEST
					Block.box(0.0d, 4.25d, 4.25d, 16.00d, 11.75d, 11.75d), // EAST
			};
	
	public final ThermalTypeEnum thermalType;
	
	public ThermalBlock(ThermalTypeEnum thermalType)
	{
		super(AbstractBlock.Properties
				.of(Material.STONE)
				.strength(4.0f, 10.0f)
				.harvestTool(ToolType.PICKAXE)
				.harvestLevel(1)
				.noOcclusion());
		this.thermalType = thermalType;
		
		this.registerDefaultState(this.getStateDefinition().any()
				.setValue(FACING, Direction.NORTH)
				.setValue(LIT, Boolean.FALSE));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World worldIn, BlockPos pos, Random rand) {
		if (state.getValue(LIT) && this.thermalType == ThermalTypeEnum.HEATING) {
			double posX = (double) pos.getX() + 0.5D;
			double posY = pos.getY();
			double posZ = (double) pos.getZ() + 0.5D;
			if (rand.nextDouble() < 0.1D) {
				worldIn.playLocalSound(posX, posY, posZ, SoundEvents.FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			}
		}
	}
	
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
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
