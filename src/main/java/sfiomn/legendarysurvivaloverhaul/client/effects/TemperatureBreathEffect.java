package sfiomn.legendarysurvivaloverhaul.client.effects;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.client.sounds.DynamicPositionSound;
import sfiomn.legendarysurvivaloverhaul.registry.EffectRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.ParticleTypeRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

public class TemperatureBreathEffect {
    private static int delay;
    private static int particleTicks;
    private static boolean reset;

    public static void tickPlay(PlayerEntity player) {
        if (player == null || !player.isAlive() || player.isSpectator() || player.isCreative()) {
            return;
        }

        TemperatureEnum temperatureEnum = CapabilityUtil.getTempCapability(player).getTemperatureEnum();

        if ((temperatureEnum != TemperatureEnum.FROSTBITE || player.hasEffect(EffectRegistry.COLD_RESISTANCE.get())) &&
                (temperatureEnum != TemperatureEnum.HEAT_STROKE || player.hasEffect(EffectRegistry.HEAT_RESISTANCE.get()))) {
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
                Vector3d eyePosition = player.getEyePosition(1f).subtract(0D, 0.2, 0D);
                if (player.isCrouching())
                    eyePosition = eyePosition.subtract(0D, 0.25, 0D);
                Vector3d trajectory = player.getLookAngle().yRot((player.getRandom().nextFloat() - 0.5f) * 0.2f).normalize();
                Vector3d origin = eyePosition.add(trajectory.scale(0.5));
                player.level.addParticle(ParticleTypeRegistry.COLD_BREATH.get(), origin.x, origin.y, origin.z, trajectory.x, trajectory.y, trajectory.z);
            }
        }
    }

    public static void reset(PlayerEntity player) {
        if (!reset) {
            delay = 200 + player.getRandom().nextInt(200);
            reset = true;
        }
    }
}
