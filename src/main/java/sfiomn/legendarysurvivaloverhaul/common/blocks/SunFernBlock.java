package sfiomn.legendarysurvivaloverhaul.common.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.PlantType;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.ParticleTypeRegistry;

import java.util.Random;

public class SunFernBlock extends CropsBlock {
    public static final int MAX_AGE = 3;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(3.0D, 0.0D, 3.0D, 13.0D, 8.0D, 13.0D),
            Block.box(1.0D, 0.0D, 1.0D, 15.0D, 10.0D, 15.0D),
            Block.box(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)};

    public static final Properties properties = getProperties();

    public SunFernBlock() {
        super(properties);
    }

    public static Properties getProperties() {
        return Properties
                .of(Material.REPLACEABLE_PLANT)
                .randomTicks()
                .noOcclusion()
                .noCollission()
                .instabreak()
                .sound(SoundType.CROP);
    }

    @Override
    protected int getBonemealAgeIncrease(World world) {
        return MathHelper.nextInt(world.random, 0, 1);
    }

    @Override
    protected IItemProvider getBaseSeedId() {
        return ItemRegistry.SUN_FERN_SEEDS.get();
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        return SHAPE_BY_AGE[this.getAge(state)];
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState p_149656_1_) {
        return PushReaction.DESTROY;
    }

    @Override
    protected boolean mayPlaceOn(BlockState blockState, IBlockReader blockReader, BlockPos blockPos) {
        return blockState.is(Blocks.GRASS_BLOCK) || blockState.is(Blocks.DIRT) || blockState.is(Blocks.COARSE_DIRT) || blockState.is(Blocks.PODZOL) || blockState.is(Blocks.FARMLAND) || blockState.is(Blocks.SAND) || blockState.is(Blocks.RED_SAND);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState state, World worldIn, BlockPos pos, Random rand) {
        if (getAge(state) < getMaxAge())
            return;
        double offsetX = (2 * rand.nextFloat() - 1) * 0.3F;
        double offsetZ = (2 * rand.nextFloat() - 1) * 0.3F;

        double x = pos.getX() + 0.5D + offsetX;
        double y = pos.getY() + 0.5D + (rand.nextFloat() * 0.05F);
        double z = pos.getZ() + 0.5D + offsetZ;

        if (worldIn.getGameTime() % 3 == 0)
            worldIn.addParticle(ParticleTypeRegistry.SUN_FERN_BLOSSOM.get(), x, y, z, 0.04D, 0.01D, 0.04D);
    }

    @Override
    public net.minecraftforge.common.PlantType getPlantType(IBlockReader world, BlockPos pos) {
        return PlantType.DESERT;
    }

    @Override
    public BlockState getPlant(IBlockReader world, BlockPos pos) {
        return defaultBlockState();
    }
}
