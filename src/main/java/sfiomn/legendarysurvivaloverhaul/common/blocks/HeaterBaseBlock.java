package sfiomn.legendarysurvivaloverhaul.common.blocks;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.common.blockentities.AbstractThermalBlockEntity;
import sfiomn.legendarysurvivaloverhaul.registry.BlockEntityRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.BlockRegistry;

public class HeaterBaseBlock extends ThermalBlock {

    public static final Properties properties = getProperties();

    private static final VoxelShape FEET = Block.box(0.0d, 0.0d, 0.0d, 16.0d, 6.0d, 16.0d);
    private static final VoxelShape BASE = Block.box(1.0d, 6.0d, 1.0d, 15.0d, 16.0d, 15.0d);

    private static final VoxelShape XZ_AXIS_AABB = Shapes.or(FEET, BASE);

    public HeaterBaseBlock(ThermalTypeEnum thermalType) {
        super(thermalType, properties);
    }

    public static BlockBehaviour.Properties getProperties()
    {
        return BlockBehaviour.Properties
                .of()
                .mapColor(MapColor.METAL)
                .sound(SoundType.METAL)
                .strength(3f, 10f)
                .noOcclusion()
                .lightLevel((lightLevel) -> lightLevel.getValue(BlockStateProperties.LIT) ? 13 : 0);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        return XZ_AXIS_AABB;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState p_153213_, BlockEntityType<T> entityType) {
        return level.isClientSide ? null : createTickerHelper(entityType, BlockEntityRegistry.HEATER_BLOCK_ENTITY.get(), AbstractThermalBlockEntity::serverTick);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {

        Level level = context.getLevel();
        BlockPos topPos = context.getClickedPos().above();
        return level.getBlockState(topPos).canBeReplaced(context) && level.getWorldBorder().isWithinBounds(topPos)
                ? this.defaultBlockState().setValue(FACING, context.getHorizontalDirection())
                : null;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
    {
        super.onRemove(state, level, pos, newState, isMoving);
        if(!state.is(newState.getBlock()) && level.getBlockState(pos.above()).getBlock() instanceof HeaterTopBlock)
        {
            level.removeBlock(pos.above(), false);
        }
    }


    @SuppressWarnings("deprecation")
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (level.isEmptyBlock(pos.above())) {
            level.setBlock(pos.above(), BlockRegistry.HEATER_TOP.get().defaultBlockState().setValue(HeaterTopBlock.FACING, state.getValue(FACING)), 2);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
        if (state.getValue(LIT)) {
            float chance = 0.5f;
            float chance_flame = 0.4f;

            //  Middle of the block
            double posX = pos.getX();
            double posY = pos.getY();
            double posZ = pos.getZ();

            if (rand.nextFloat() < chance) {
                level.playLocalSound(posX + 0.5d, posY + 0.5d, posZ + 0.5d, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }

            if (rand.nextFloat() < chance) {
                //  Particle spawns half block around the center of the block : [0.25 - 0.75]
                float xr = rand.nextFloat() / 2 + 0.25f;
                float zr = rand.nextFloat() / 2 + 0.25f;

                //  Particle moves 1/20 block any direction : rand range [0.0 - 0.05] -> [-0.025 - 0.025]
                float xm = rand.nextFloat() / 20 - 0.025f;
                float zm = rand.nextFloat() / 20 - 0.025f;

                //  Particles moves always up : [0.02 - 0.03]
                float ym = rand.nextFloat() / 10 + 0.02f;

                level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, false, posX + xr, pos.above().getY() + 0.8d, posZ + zr, xm, ym, zm);
            }

            if (rand.nextFloat() < chance_flame) {
                //  Particle spawns around the center of the block : [0.33 - 0.66]
                float zr = rand.nextFloat() / 3 + 0.33f;
                float yr = rand.nextFloat() / 6 + 0.5f;
                level.addParticle(ParticleTypes.SMOKE, false, posX + 0.05, posY + yr, posZ + zr, 0, 0, 0);
                level.addParticle(ParticleTypes.FLAME, false, posX + 0.05, posY + yr, posZ + zr, 0, 0, 0);
            }
            if (rand.nextFloat() < chance_flame) {
                //  Particle spawns around the center of the block : [0.33 - 0.66]
                float zr = rand.nextFloat() / 3 + 0.33f;
                float yr = rand.nextFloat() / 6 + 0.5f;
                level.addParticle(ParticleTypes.SMOKE, false, posX + 0.95, posY + yr, posZ + zr, 0, 0, 0);
                level.addParticle(ParticleTypes.FLAME, false, posX + 0.95, posY + yr, posZ + zr, 0, 0, 0);
            }
            if (rand.nextFloat() < chance_flame) {
                //  Particle spawns around the center of the block : [0.33 - 0.66]
                float xr = rand.nextFloat() / 3 + 0.33f;
                float yr = rand.nextFloat() / 6 + 0.5f;
                level.addParticle(ParticleTypes.SMOKE, false, posX + xr, posY + yr, posZ + 0.05, 0, 0, 0);
                level.addParticle(ParticleTypes.FLAME, false, posX + xr, posY + yr, posZ + 0.05, 0, 0, 0);
            }
            if (rand.nextFloat() < chance_flame) {
                //  Particle spawns around the center of the block : [0.33 - 0.66]
                float xr = rand.nextFloat() / 3 + 0.33f;
                float yr = rand.nextFloat() / 6 + 0.5f;
                level.addParticle(ParticleTypes.SMOKE, false, posX + xr, posY + yr, posZ + 0.95, 0, 0, 0);
                level.addParticle(ParticleTypes.FLAME, false, posX + xr, posY + yr, posZ + 0.95, 0, 0, 0);
            }
        }
        super.animateTick(state, level, pos, rand);
    }
}
