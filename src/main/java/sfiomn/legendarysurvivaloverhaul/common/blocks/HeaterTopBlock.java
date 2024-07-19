package sfiomn.legendarysurvivaloverhaul.common.blocks;

import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import sfiomn.legendarysurvivaloverhaul.registry.BlockRegistry;

public class HeaterTopBlock extends HorizontalDirectionalBlock {

    public static final Properties properties = getProperties();

    private static final VoxelShape BASE = Block.box(0.0d, 0.0d, 0.0d, 16.0d, 2.0d, 16.0d);
    private static final VoxelShape TUBE = Block.box(4.0d, 2.0d, 4.0d, 12.0d, 14.0d, 12.0d);
    private static final VoxelShape TOP = Block.box(3.0d, 14.0d, 3.0d, 13.0d, 16.0d, 13.0d);

    private static final VoxelShape XZ_AXIS_AABB = Shapes.or(BASE, TUBE, TOP);

    public HeaterTopBlock()
    {
        super(properties);

        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.NORTH));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    public static Properties getProperties() {
        return Properties
                .of()
                .mapColor(MapColor.METAL)
                .sound(SoundType.METAL)
                .strength(3f, 10f)
                .noOcclusion()
                .noLootTable();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        return XZ_AXIS_AABB;
    }

    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult)
    {
        BlockState bottomState = level.getBlockState(pos.below());
        if (bottomState.is(BlockRegistry.HEATER.get()))
        {
            ((HeaterBaseBlock) bottomState.getBlock()).use(bottomState, level, pos.below(), player, hand, rayTraceResult);
        }
        return InteractionResult.SUCCESS;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        super.neighborChanged(state, level, pos, blockIn, fromPos, isMoving);
        if (!level.getBlockState(pos.below()).is(BlockRegistry.HEATER.get()))
        {
            level.removeBlock(pos, false);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

}
