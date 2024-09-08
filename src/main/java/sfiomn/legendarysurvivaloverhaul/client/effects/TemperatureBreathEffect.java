package sfiomn.legendarysurvivaloverhaul.client.effects;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.RandomUtils;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.ParticleTypeRegistry;

public class TemperatureBreathEffect {
    private static int delay;
    private static int particleTicks;
    private static boolean reset;
    private static int updateTempTicks;
    private static float temperature;

    static {
        delay = 200 + RandomUtils.nextInt(0, 200);
    }

    public static void tickPlay(Player player) {
        if (player == null || !player.isAlive() || player.isSpectator() || player.isCreative() ||
            !player.level().getBlockState(BlockPos.containing(player.getEyePosition())).isAir()) {
            return;
        }

        if (updateTempTicks-- < 0) {
            updateTempTicks = 20;
            temperature = TemperatureUtil.getWorldTemperature(player.level(), player.blockPosition());
        }

        if (temperature >= Config.Baked.coldBreathEffectThreshold) {
            reset(player);
            return;
        }

        reset = false;

        if (delay-- <= 0) {
            delay = 200 + player.getRandom().nextInt(200);
            particleTicks = 17;
        }

        if (particleTicks > 0) {
            particleTicks--;
            Vec3 eyePosition = player.getEyePosition().subtract(0D, 0.2, 0D);
            if (player.isCrouching())
                eyePosition = eyePosition.subtract(0D, 0.25, 0D);
            for (int i=0; i<3; i++) {
                Vec3 trajectory = player.getLookAngle().yRot((player.level().random.nextFloat() - 0.5f) * 0.1f).normalize();
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
