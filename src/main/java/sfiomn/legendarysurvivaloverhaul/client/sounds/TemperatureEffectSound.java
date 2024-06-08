package sfiomn.legendarysurvivaloverhaul.client.sounds;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureCapability;
import sfiomn.legendarysurvivaloverhaul.registry.EffectRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

public class TemperatureEffectSound {
    private static int delay;
    private static boolean reset;

    public static void tickPlay(PlayerEntity player) {
        if (player == null || !player.isAlive() || player.isSpectator() || player.isSpectator()) {
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
            if (temperatureEnum == TemperatureEnum.FROSTBITE)
                Minecraft.getInstance().getSoundManager().play(new DynamicPositionSound(SoundRegistry.SHIVERING.get(), player));
            else
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
