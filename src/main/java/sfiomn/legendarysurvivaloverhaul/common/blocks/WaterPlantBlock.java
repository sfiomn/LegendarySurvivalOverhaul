package sfiomn.legendarysurvivaloverhaul.common.blocks;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;

public class WaterPlantBlock extends TallFlowerBlock implements IPlantable {
    protected static final VoxelShape SHAPE_BOTTOM = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_TOP = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 11.0D, 16.0D);

    public WaterPlantBlock(Properties p_i48412_1_) {
        super(p_i48412_1_);
    }

    @Override
    protected boolean mayPlaceOn(BlockState blockState, BlockGetter blockReader, BlockPos blockPos) {
        return blockState.getMapColor(blockReader, blockPos) == MapColor.SAND;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
        Vec3 vector3d = state.getOffset(reader, pos);
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER)
            return SHAPE_BOTTOM.move(vector3d.x, vector3d.y, vector3d.z);
        else
            return SHAPE_TOP.move(vector3d.x, vector3d.y, vector3d.z);
    }
    @Override
    public PlantType getPlantType(BlockGetter world, BlockPos pos) {
        return PlantType.DESERT;
    }

    @Override
    public BlockState getPlant(BlockGetter world, BlockPos pos) {
        return defaultBlockState();
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource rand, BlockPos pos, BlockState state) {
        popResource(level, pos, new ItemStack(ItemRegistry.WATER_PLANT_BAG.get(), rand.nextFloat() < 0.5f ? 1 : 2));
    }
}
