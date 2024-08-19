package sfiomn.legendarysurvivaloverhaul.client.effects;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.client.sounds.DynamicPositionSound;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.ParticleTypeRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

public class TemperatureBreathEffect {
    private static int delay;
    private static int particleTicks;
    private static boolean reset;

    public static void tickPlay(Player player) {
        if (player == null || !player.isAlive() ||
            player.isSpectator() || player.isCreative() ||
            !player.level().getBlockState(BlockPos.containing(player.getEyePosition())).isAir()) {
            return;
        }

        TemperatureEnum temperatureEnum = CapabilityUtil.getTempCapability(player).getTemperatureEnum();

        if ((temperatureEnum != TemperatureEnum.FROSTBITE || player.hasEffect(MobEffectRegistry.COLD_RESISTANCE.get())) &&
                (temperatureEnum != TemperatureEnum.HEAT_STROKE || player.hasEffect(MobEffectRegistry.HEAT_RESISTANCE.get()))) {
            reset(player);
            return;
        }

        reset = false;

        if (delay-- <= 0) {
            delay = 200 + player.getRandom().nextInt(200);
            if (temperatureEnum == TemperatureEnum.FROSTBITE) {
                particleTicks = 40;
                Minecraft.getInstance().getSoundManager().play(new DynamicPositionSound(SoundRegistry.SHIVERING.get(), player));
            } else
                Minecraft.getInstance().getSoundManager().play(new DynamicPositionSound(SoundRegistry.PANTING.get(), player));
        }

        if (particleTicks > 0) {
            particleTicks--;
            if (particleTicks % 2 == 0) {
                Vec3 eyePosition = player.getEyePosition().subtract(0D, 0.2, 0D);
                if (player.isCrouching())
                    eyePosition = eyePosition.subtract(0D, 0.25, 0D);
                Vec3 trajectory = player.getLookAngle().yRot((player.getRandom().nextFloat() - 0.5f) * 0.2f).normalize();
                Vec3 origin = eyePosition.add(trajectory.scale(0.5));
                player.level().addParticle(ParticleTypeRegistry.COLD_BREATH.get(), origin.x, origin.y, origin.z, trajectory.x, trajectory.y, trajectory.z);
            }
        }
    }

    public static void reset(Player player) {
        if (!reset) {
            delay = 200 + player.getRandom().nextInt(200);
            reset = true;
        }
    }
}
