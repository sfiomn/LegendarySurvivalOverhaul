package sfiomn.legendarysurvivaloverhaul.common.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import sfiomn.legendarysurvivaloverhaul.registry.BlockRegistry;

public class HeaterTopBlock extends HorizontalBlock {

    public static final AbstractBlock.Properties properties = getProperties();

    private static final VoxelShape BASE = Block.box(0.0d, 0.0d, 0.0d, 16.0d, 2.0d, 16.0d);
    private static final VoxelShape TUBE = Block.box(4.0d, 2.0d, 4.0d, 12.0d, 14.0d, 12.0d);
    private static final VoxelShape TOP = Block.box(3.0d, 14.0d, 3.0d, 13.0d, 16.0d, 13.0d);

    private static final VoxelShape XZ_AXIS_AABB = VoxelShapes.or(BASE, TUBE, TOP);

    public HeaterTopBlock()
    {
        super(properties);

        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.NORTH));
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    public static Properties getProperties()
    {
        return Properties
                .of(Material.METAL)
                .sound(SoundType.METAL)
                .strength(3f, 10f)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(1)
                .noOcclusion();
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return XZ_AXIS_AABB;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult)
    {
        BlockState bottomState = worldIn.getBlockState(pos.below());
        if (bottomState.is(BlockRegistry.HEATER.get()))
        {
            ((HeaterBaseBlock) bottomState.getBlock()).use(bottomState, worldIn, pos.below(), player, hand, rayTraceResult);
        }
        return ActionResultType.SUCCESS;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        if (!worldIn.getBlockState(pos.below()).is(BlockRegistry.HEATER.get()))
        {
            worldIn.removeBlock(pos, false);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

}
