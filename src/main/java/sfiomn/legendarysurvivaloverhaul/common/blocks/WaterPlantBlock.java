package sfiomn.legendarysurvivaloverhaul.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TallFlowerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WaterPlantBlock extends TallFlowerBlock implements IPlantable {
    protected static final VoxelShape SHAPE_BOTTOM = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_TOP = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 11.0D, 16.0D);

    public WaterPlantBlock(Properties p_i48412_1_) {
        super(p_i48412_1_);
    }

    @Override
    protected boolean mayPlaceOn(BlockState blockState, IBlockReader blockReader, BlockPos blockPos) {
        return blockState.getMaterial() == Material.SAND;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        Vector3d vector3d = state.getOffset(reader, pos);
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER)
            return SHAPE_BOTTOM.move(vector3d.x, vector3d.y, vector3d.z);
        else
            return SHAPE_TOP.move(vector3d.x, vector3d.y, vector3d.z);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder context) {
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER)
            return super.getDrops(state, context);
        return Collections.emptyList();
    }

    @Override
    public PlantType getPlantType(IBlockReader world, BlockPos pos) {
        return PlantType.DESERT;
    }

    @Override
    public BlockState getPlant(IBlockReader world, BlockPos pos) {
        return defaultBlockState();
    }

    @Override
    public void performBonemeal(ServerWorld world, Random rand, BlockPos pos, BlockState state) {
        popResource(world, pos, new ItemStack(ItemRegistry.WATER_PLANT_BAG.get(), rand.nextFloat() < 0.5f ? 1 : 2));
    }
}
