package icey.survivaloverhaul.common.blocks;

import java.util.Random;


import icey.survivaloverhaul.Main;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

public class BlockTemperatureCoil extends Block implements IWaterLoggable
{
	public static final DirectionProperty DIRECTION = BlockStateProperties.FACING;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	
	private static final VoxelShape[] SHAPES = new VoxelShape[]
			{
					Block.makeCuboidShape(4.25d, 0.0d, 4.25d, 11.75d, 16.0d, 11.75d), // DOWN
					Block.makeCuboidShape(4.25d, 0.0d, 4.25d, 11.75d, 16.0d, 11.75d), // UP
					Block.makeCuboidShape(4.25d, 4.25d, 0.0d, 11.75d, 11.75d, 16.0d), // NORTH
					Block.makeCuboidShape(4.25d, 4.25d, 0.0d, 11.75d, 11.75d, 16.0d), // SOUTH
					Block.makeCuboidShape(0.0d, 4.25d, 4.25d, 16.0d,  11.75d, 11.75d), // WEST
					Block.makeCuboidShape(0.0d, 4.25d, 4.25d, 16.00d, 11.75d, 11.75d), // EAST
			};
	
	public final CoilType coilType;
	
	public BlockTemperatureCoil(CoilType coilType)
	{
		super(AbstractBlock.Properties
				.create(Material.IRON)
				.hardnessAndResistance(4.0f, 10.0f)
				.harvestTool(ToolType.PICKAXE)
				.harvestLevel(1)
				.notSolid());
		this.coilType = coilType;
		
		this.setDefaultState(this.stateContainer.getBaseState()
				.with(DIRECTION, Direction.DOWN)
				.with(POWERED, Boolean.valueOf(false))
				.with(WATERLOGGED, Boolean.valueOf(false)));
	}
	
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		if (!worldIn.isRemote)
		{
			boolean powered = worldIn.isBlockPowered(pos);
			boolean enabled = state.get(POWERED);
			
			if (enabled && !powered)
			{
				worldIn.getPendingBlockTicks().scheduleTick(pos, this, 4);
			}
			else if (!enabled && powered)
			{
				turnOn(worldIn, pos, state);
			}
		}
	}
	
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand)
	{
		if (state.get(POWERED) && !worldIn.isBlockPowered(pos))
		{
			turnOff(worldIn, pos, state);
		}
	}
	
	private void turnOff(final World world, final BlockPos pos, final BlockState state)
	{
		world.setBlockState(pos, state.with(POWERED, false));
	}
	
	private void turnOn(final World world, final BlockPos pos, final BlockState state)
	{
		world.setBlockState(pos, state.with(POWERED, true));
	}
	
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return this.getDefaultState().with(DIRECTION, context.getFace().getOpposite()).with(POWERED, context.getWorld().isBlockPowered(context.getPos()));
	}
	
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(DIRECTION, POWERED, WATERLOGGED);
	}
	
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SHAPES[state.get(DIRECTION).getIndex()];
	}
	
	@SuppressWarnings("deprecation")
	public FluidState getFluidState(BlockState state)
	{
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}
	
	public enum CoilType
	{
		COOLING("cooling", -1.0f),
		HEATING("heating", 1.0f),
		BROKEN("broken", 0f);
		
		private String name;
		private float temperature;
		
		private CoilType(String name, float temperature)
		{
			this.name = name;
			this.temperature = temperature;
		}
		
		public String getName()
		{
			return name;
		}
		
		public float getTemperatureMultiplier()
		{
			return temperature;
		}
	}

	/*
	public BlockRenderType getRenderType(BlockState state)
	{
		return BlockRenderType.MODEL;
	}
	
	@Nullable
	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn)
	{
		return new CoilTileEntity();
	}
	*/
}
