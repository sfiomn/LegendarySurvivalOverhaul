package sfiomn.legendarysurvivaloverhaul.common.integration.origins;

import io.github.apace100.origins.Origins;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginRegistry;
import io.github.apace100.origins.registry.forge.ModComponentsArchitecturyImpl;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureImmunityEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.api.wetness.WetnessUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.EffectRegistry;

public class OriginsUtil {
    public static ResourceLocation BLAZEBORN = new ResourceLocation(Origins.MODID, "blazeborn");
    public static ResourceLocation MERLING = new ResourceLocation(Origins.MODID, "merling");
    public static ResourceLocation PHANTOM = new ResourceLocation(Origins.MODID, "phantom");
    public static ResourceLocation AVIAN = new ResourceLocation(Origins.MODID, "avian");
    public static ResourceLocation ELYTRIAN = new ResourceLocation(Origins.MODID, "elytrian");
    public static ResourceLocation SHULK = new ResourceLocation(Origins.MODID, "shulk");

    public static boolean isOrigin(PlayerEntity player, ResourceLocation origin) {
        return LegendarySurvivalOverhaul.originsLoaded &&
                player.getCapability(ModComponentsArchitecturyImpl.ORIGIN_COMPONENT_CAPABILITY).isPresent() &&
                player.getCapability(ModComponentsArchitecturyImpl.ORIGIN_COMPONENT_CAPABILITY).resolve().isPresent() &&
                player.getCapability(ModComponentsArchitecturyImpl.ORIGIN_COMPONENT_CAPABILITY).resolve().get().getOrigins().containsValue(OriginRegistry.get(origin));
    }

    public static void assignOriginsFeatures(PlayerEntity player) {
        player.getCapability(ModComponentsArchitecturyImpl.ORIGIN_COMPONENT_CAPABILITY).ifPresent(
                origins -> {
                    if (origins.getOrigins().containsValue(OriginRegistry.get(MERLING))) {
                        removeThirstEffect(player);
                    } else if (origins.getOrigins().containsValue(OriginRegistry.get(SHULK))) {
                        addExtraThirstExhaustion(player, Config.Baked.extraThirstExhaustionShulk);
                    } else if (origins.getOrigins().containsValue(OriginRegistry.get(PHANTOM))) {
                        addExtraThirstExhaustion(player, Config.Baked.extraThirstExhaustionPhantom);
                    }

                    adaptWetnessDeactivation(player,
                            origins.getOrigins().containsValue(OriginRegistry.get(MERLING)) ||
                                    origins.getOrigins().containsValue(OriginRegistry.get(BLAZEBORN)));

                    adaptHighAltitudeImmunity(player,
                            origins.getOrigins().containsValue(OriginRegistry.get(AVIAN)) ||
                                    origins.getOrigins().containsValue(OriginRegistry.get(ELYTRIAN)));

                    adaptOnFireImmunity(player, origins.getOrigins().containsValue(OriginRegistry.get(BLAZEBORN)));
                }
        );
    }

    private static void adaptWetnessDeactivation(PlayerEntity player, boolean shouldDeactivate) {
        if (shouldDeactivate) {
            if (WetnessUtil.isWetnessActive(player))
                WetnessUtil.deactivateWetness(player);
        } else {
            if (!WetnessUtil.isWetnessActive(player))
                WetnessUtil.activateWetness(player);
        }
    }

    private static void adaptHighAltitudeImmunity(PlayerEntity player, boolean shouldAdd) {
        if (shouldAdd)
            TemperatureUtil.addImmunity(player, TemperatureImmunityEnum.HIGH_ALTITUDE);
        else
            TemperatureUtil.removeImmunity(player, TemperatureImmunityEnum.HIGH_ALTITUDE);
    }

    private static void adaptOnFireImmunity(PlayerEntity player, boolean shouldAdd) {
        if (shouldAdd)
            TemperatureUtil.addImmunity(player, TemperatureImmunityEnum.ON_FIRE);
        else
            TemperatureUtil.removeImmunity(player, TemperatureImmunityEnum.ON_FIRE);
    }

    private static void removeThirstEffect(PlayerEntity player) {
        if (player.hasEffect(EffectRegistry.THIRST.get()))
            player.removeEffect(EffectRegistry.THIRST.get());
    }

    private static void addExtraThirstExhaustion(PlayerEntity player, double extraExhaustion) {
        ThirstUtil.addExhaustion(player, (float) extraExhaustion);
    }
}
