package sfiomn.legendarysurvivaloverhaul.client.sounds;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.RandomUtils;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.registry.EffectRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

public class TemperatureBreathSound {
    private static int delay;
    private static boolean reset;

    static {
        delay = 200 + RandomUtils.nextInt(0, 200);
    }

    public static void tickPlay(PlayerEntity player) {
        if (player == null || !player.isAlive() || player.isSpectator() || player.isCreative() ||
                !player.level.getBlockState(new BlockPos(player.getEyePosition(1f))).isAir()) {
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
                Minecraft.getInstance().getSoundManager().play(new DynamicPositionSound(SoundRegistry.SHIVERING.get(), player));
            } else
                Minecraft.getInstance().getSoundManager().play(new DynamicPositionSound(SoundRegistry.PANTING.get(), player));
        }
    }

    public static void reset(PlayerEntity player) {
        if (!reset) {
            delay = 200 + player.getRandom().nextInt(200);
            reset = true;
        }
    }
}
