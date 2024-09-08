package sfiomn.legendarysurvivaloverhaul.client.effects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import org.apache.commons.lang3.RandomUtils;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.ParticleTypeRegistry;

import java.util.Random;

public class TemperatureBreathEffect {
    private static int delay;
    private static int particleTicks;
    private static boolean reset;
    private static int updateTempTicks;
    private static float temperature;

    static {
        delay = 200 + RandomUtils.nextInt(0, 200);
    }

    public static void tickPlay(PlayerEntity player) {
        if (player == null || !player.isAlive() || player.isSpectator() || player.isCreative() ||
            !player.level.getBlockState(new BlockPos(player.getEyePosition(1f))).isAir()) {
            return;
        }

        if (updateTempTicks-- < 0) {
            updateTempTicks = 20;
            temperature = TemperatureUtil.getWorldTemperature(player.level, player.blockPosition());
        }

        if (temperature >= Config.Baked.coldBreathEffectThreshold) {
            reset(player);
            return;
        }

        reset = false;

        if (delay-- <= 0) {
            delay = 200 + player.getRandom().nextInt(200);
            particleTicks = 20;
        }

        if (particleTicks > 0) {
            particleTicks--;
            Vector3d eyePosition = player.getEyePosition(1f).subtract(0D, 0.2, 0D);
            if (player.isCrouching())
                eyePosition = eyePosition.subtract(0D, 0.25, 0D);
            for (int i = 0; i < 3; i++) {
                Vector3d trajectory = player.getLookAngle().yRot((player.level.random.nextFloat() - 0.5f) * 0.1f).normalize();
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
