package sfiomn.legendarysurvivaloverhaul.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.common.blockentities.AbstractThermalBlockEntity;
import sfiomn.legendarysurvivaloverhaul.registry.BlockEntityRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;

public class CoolerBlock extends ThermalBlock {

    public static final Properties properties = getProperties();

    public CoolerBlock(ThermalTypeEnum thermalType) {
        super(thermalType, properties);
    }

    public static Properties getProperties()
    {
        return Properties
                .of()
                .mapColor(MapColor.WOOD)
                .sound(SoundType.WOOD)
                .strength(2f, 10f)
                .noOcclusion();
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState p_153213_, BlockEntityType<T> entityType) {
        return level.isClientSide ? null : createTickerHelper(entityType, BlockEntityRegistry.COOLER_BLOCK_ENTITY.get(), AbstractThermalBlockEntity::serverTick);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
        if (state.getValue(LIT)) {
            float chanceSound = 0.1f;
            float chanceParticle = 0.25f;
            double posX = pos.getX();
            double posY = pos.getY();
            double posZ = pos.getZ();
            if (rand.nextFloat() < chanceSound) {
                level.playLocalSound(posX + 0.5d, posY + 0.5d, posZ + 0.5d, SoundRegistry.COOLER_BLOCK.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }

            if (rand.nextFloat() < chanceParticle) {

                //  Spawn particles at the top of the cooler, randomly along the surface
                float xr = rand.nextFloat();
                float zr = rand.nextFloat();

                level.addParticle(ParticleTypes.ITEM_SNOWBALL, false, posX + xr, posY + 1.0d, posZ + zr, 0, 0, 0);
            }
        }
        super.animateTick(state, level, pos, rand);
    }
}
