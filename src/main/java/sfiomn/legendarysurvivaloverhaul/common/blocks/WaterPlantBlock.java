package sfiomn.legendarysurvivaloverhaul.common.blocks;

import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;

public class WaterPlantBlock extends CropBlock implements IPlantable {
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
        return BlockBehaviour.Properties
                .of()
                .mapColor(MapColor.PLANT)
                .randomTicks()
                .noOcclusion()
                .noCollission()
                .instabreak()
                .sound(SoundType.CROP)
                .pushReaction(PushReaction.DESTROY);
    }

    @Override
    public void randomTick(@NotNull BlockState state, ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (level.getRawBrightness(pos, 0) >= 9 && !this.isUpperBlock(state)) {
            int age = this.getAge(state);
            if (canGrow(level, state, pos)) {
                float f = getGrowthSpeed(this, level, pos);
                if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt((int)(25.0F / f) + 1) == 0)) {
                    level.setBlock(pos, this.getStateForAge(age + 1), 2);
                    if (this.getAge(state) > this.getMaxAge() / 2) {
                        level.setBlock(pos.above(), this.getStateForAge(age + 1).setValue(HALF, DoubleBlockHalf.UPPER), 2);
                    }
                    net.minecraftforge.common.ForgeHooks.onCropsGrowPost(level, pos, state);
                }
            }
        }
    }

    @Override
    public void growCrops(Level level, BlockPos pos, BlockState state) {
        int age = this.getAge(state);
        if (canGrow(level, state, pos)) {
            BlockPos basePos = pos;
            if (isUpperBlock(state)) {
                basePos = pos.below();
            }
            age = Math.min(age + this.getBonemealAgeIncrease(level), this.getMaxAge());
            level.setBlock(basePos, this.getStateForAge(age), 2);
            if (age > this.getMaxAge() / 2) {
                level.setBlock(basePos.above(), this.getStateForAge(age).setValue(HALF, DoubleBlockHalf.UPPER), 2);
            }
        }
    }

    public boolean isUpperBlock(BlockState state) {
        return state.getValue(HALF) == DoubleBlockHalf.UPPER;
    }

    public boolean canGrow(Level level, BlockState state, BlockPos pos) {
        if (this.isUpperBlock(state) || level.isEmptyBlock(pos.above()) || level.getBlockState(pos.above()).is(this)) {
            return this.getAge(state) < this.getMaxAge();
        } else {
            return this.getAge(state) < this.getMaxAge() / 2;
        }
    }

    @Override
    protected @NotNull ItemLike getBaseSeedId() {
        return ItemRegistry.WATER_PLANT_SEEDS.get();
    }

    @Override
    public @NotNull IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE).add(HALF);
    }

    @Override
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        if (state.getBlock() == this && isUpperBlock(state)) {
            BlockState stateBelow = level.getBlockState(pos.below());
            return ((level.getRawBrightness(pos, 0) >= 8 || level.canSeeSky(pos)) && stateBelow.getBlock() == this && this.getAge(stateBelow) > this.getMaxAge() / 2);
        }
        return super.canSurvive(state, level, pos);
    }

    @Override
    protected boolean mayPlaceOn(BlockState blockState, BlockGetter blockReader, BlockPos blockPos) {
        return blockState.getMapColor(blockReader, blockPos) == MapColor.SAND || blockState.is(Blocks.RED_SAND);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
        Vec3 vector3d = state.getOffset(reader, pos);
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER)
            return SHAPE_BY_AGE[Math.min(this.getAge(state), 3)].move(vector3d.x, vector3d.y, vector3d.z);
        else
            return SHAPE_BY_AGE[this.getAge(state)].move(vector3d.x, vector3d.y, vector3d.z);
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
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            if (player.isCreative()) {
                preventCreativeDropFromBottomPart(level, pos, state, player);
            } else {
                dropResources(state, level, pos, null, player, player.getMainHandItem());
            }
        }

        super.playerWillDestroy(level, pos, state, player);
    }

    public static void preventCreativeDropFromBottomPart(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        DoubleBlockHalf doubleblockhalf = pState.getValue(HALF);
        if (doubleblockhalf == DoubleBlockHalf.UPPER) {
            BlockPos blockpos = pPos.below();
            BlockState blockstate = pLevel.getBlockState(blockpos);
            if (blockstate.is(pState.getBlock()) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER) {
                BlockState blockstate1 = blockstate.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
                pLevel.setBlock(blockpos, blockstate1, 35);
                pLevel.levelEvent(pPlayer, 2001, blockpos, Block.getId(blockstate));
            }
        }
    }

    @Override
    protected int getBonemealAgeIncrease(Level pLevel) {
        return Mth.nextInt(pLevel.random, 1, 2);
    }

    @Override
    public boolean isValidBonemealTarget(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state, boolean isClient) {
        if (canGrow((Level) level, state, pos)) {
            return state.getBlock() == this;
        }
        return false;
    }
}
