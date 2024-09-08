package sfiomn.legendarysurvivaloverhaul.common.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;

import java.util.Random;

public class WaterPlantBlock extends CropsBlock implements IPlantable {
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public static final int MAX_AGE = 7;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(4.0D, 0.0D, 4.0D, 12.0D, 6.0D, 12.0D),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 9.0D, 14.0D),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 11.0D, 16.0D)};

    public static final Properties properties = getProperties();

    public WaterPlantBlock() {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER));
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
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!world.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (world.getRawBrightness(pos, 0) >= 9 && !this.isUpperBlock(state)) {
            int age = this.getAge(state);
            if (canGrow(world, state, pos)) {
                float f = getGrowthSpeed(this, world, pos);
                if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(world, pos, state, random.nextInt((int)(25.0F / f) + 1) == 0)) {
                    world.setBlock(pos, this.getStateForAge(age + 1), 2);
                    if (this.getAge(state) > this.getMaxAge() / 2) {
                        world.setBlock(pos.above(), this.getStateForAge(age + 1).setValue(HALF, DoubleBlockHalf.UPPER), 2);
                    }
                    net.minecraftforge.common.ForgeHooks.onCropsGrowPost(world, pos, state);
                }
            }
        }
    }

    @Override
    public void growCrops(World world, BlockPos pos, BlockState state) {
        int age = this.getAge(state);
        if (canGrow(world, state, pos)) {
            BlockPos basePos = pos;
            if (isUpperBlock(state)) {
                basePos = pos.below();
            }
            age = Math.min(age + this.getBonemealAgeIncrease(world), this.getMaxAge());
            world.setBlock(basePos, this.getStateForAge(age), 2);
            if (age > this.getMaxAge() / 2) {
                world.setBlock(basePos.above(), this.getStateForAge(age).setValue(HALF, DoubleBlockHalf.UPPER), 2);
            }
        }
    }

    public boolean isUpperBlock(BlockState state) {
        return state.getValue(HALF) == DoubleBlockHalf.UPPER;
    }

    public boolean canGrow(World world, BlockState state, BlockPos pos) {
        if (this.isUpperBlock(state) || world.isEmptyBlock(pos.above()) || world.getBlockState(pos.above()).is(this)) {
            return this.getAge(state) < this.getMaxAge();
        } else {
            return this.getAge(state) < this.getMaxAge() / 2;
        }
    }

    @Override
    protected IItemProvider getBaseSeedId() {
        return ItemRegistry.WATER_PLANT_SEEDS.get();
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
        builder.add(AGE).add(HALF);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState p_149656_1_) {
        return PushReaction.DESTROY;
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
        if (state.getBlock() == this && isUpperBlock(state)) {
            BlockState stateBelow = world.getBlockState(pos.below());
            return ((world.getRawBrightness(pos, 0) >= 8 || world.canSeeSky(pos)) && stateBelow.getBlock() == this && this.getAge(stateBelow) > this.getMaxAge() / 2);
        }
        return super.canSurvive(state, world, pos);
    }

    @Override
    protected boolean mayPlaceOn(BlockState blockState, IBlockReader blockReader, BlockPos blockPos) {
        return blockState.getMaterial() == Material.SAND || blockState.is(Blocks.RED_SAND);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        Vector3d vector3d = state.getOffset(reader, pos);
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER)
            return SHAPE_BY_AGE[Math.min(this.getAge(state), 3)].move(vector3d.x, vector3d.y, vector3d.z);
        else
            return SHAPE_BY_AGE[this.getAge(state)].move(vector3d.x, vector3d.y, vector3d.z);
    }

    @Override
    public PlantType getPlantType(IBlockReader world, BlockPos pos) {
        return PlantType.DESERT;
    }

    @Override
    public BlockState getPlant(IBlockReader p_getPlant_1_, BlockPos p_getPlant_2_) {
        return defaultBlockState();
    }

    @Override
    public void playerWillDestroy(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClientSide) {
            if (isUpperBlock(state)) {
                if (world.getBlockState(pos.below()).is(this))
                    world.removeBlock(pos.below(), false);
            } else if (world.getBlockState(pos.above()).is(this))
                world.removeBlock(pos.above(), false);

            if (player.isCreative()) {
                world.removeBlock(pos, false);
            }
        }

        super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    protected int getBonemealAgeIncrease(World world) {
        return MathHelper.nextInt(world.random, 1, 2);
    }

    @Override
    public boolean isValidBonemealTarget(IBlockReader reader, BlockPos pos, BlockState state, boolean isClient) {
        if (canGrow((World) reader, state, pos)) {
            return state.getBlock() == this;
        }
        return false;
    }
}
