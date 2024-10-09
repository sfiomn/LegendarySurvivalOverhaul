package sfiomn.legendarysurvivaloverhaul.common.integration.origins;

import io.github.edwinmindcraft.origins.api.OriginsAPI;
import io.github.edwinmindcraft.origins.api.origin.Origin;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureImmunityEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.api.wetness.WetnessUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;

public class OriginsUtil {
    public static ResourceKey<Origin> BLAZEBORN = ResourceKey.create(OriginsAPI.getOriginsRegistry().key(), new ResourceLocation(OriginsAPI.MODID, "blazeborn"));
    public static ResourceKey<Origin> MERLING = ResourceKey.create(OriginsAPI.getOriginsRegistry().key(), new ResourceLocation(OriginsAPI.MODID, "merling"));
    public static ResourceKey<Origin> PHANTOM = ResourceKey.create(OriginsAPI.getOriginsRegistry().key(), new ResourceLocation(OriginsAPI.MODID, "phantom"));
    public static ResourceKey<Origin> AVIAN = ResourceKey.create(OriginsAPI.getOriginsRegistry().key(), new ResourceLocation(OriginsAPI.MODID, "avian"));
    public static ResourceKey<Origin> ELYTRIAN = ResourceKey.create(OriginsAPI.getOriginsRegistry().key(), new ResourceLocation(OriginsAPI.MODID, "elytrian"));
    public static ResourceKey<Origin> SHULK = ResourceKey.create(OriginsAPI.getOriginsRegistry().key(), new ResourceLocation(OriginsAPI.MODID, "shulk"));

    public static boolean isOrigin(Player player, ResourceKey<Origin> origin) {
        return LegendarySurvivalOverhaul.originsLoaded &&
                player.getCapability(OriginsAPI.ORIGIN_CONTAINER).isPresent() &&
                player.getCapability(OriginsAPI.ORIGIN_CONTAINER).resolve().isPresent() &&
                player.getCapability(OriginsAPI.ORIGIN_CONTAINER).resolve().get().getOrigins().containsValue(origin);
    }

    public static void assignOriginsFeatures(Player player) {
        player.getCapability(OriginsAPI.ORIGIN_CONTAINER).ifPresent(
                origins -> {
                    if (origins.getOrigins().containsValue(MERLING)) {
                        removeThirstEffect(player);
                    } else if (origins.getOrigins().containsValue(SHULK)) {
                        addExtraThirstExhaustion(player, Config.Baked.extraThirstExhaustionShulk);
                    } else if (origins.getOrigins().containsValue(PHANTOM)) {
                        addExtraThirstExhaustion(player, Config.Baked.extraThirstExhaustionPhantom);
                    }

                    adaptWetnessDeactivation(player,
                            origins.getOrigins().containsValue(MERLING) ||
                                    origins.getOrigins().containsValue(BLAZEBORN));

                    adaptHighAltitudeImmunity(player,
                            origins.getOrigins().containsValue(AVIAN) ||
                                    origins.getOrigins().containsValue(ELYTRIAN));

                    adaptOnFireImmunity(player, origins.getOrigins().containsValue(BLAZEBORN));
                }
        );
    }

    private static void adaptWetnessDeactivation(Player player, boolean shouldDeactivate) {
        if (shouldDeactivate) {
            if (WetnessUtil.isWetnessActive(player))
                WetnessUtil.deactivateWetness(player);
        } else {
            if (!WetnessUtil.isWetnessActive(player))
                WetnessUtil.activateWetness(player);
        }
    }

    private static void adaptHighAltitudeImmunity(Player player, boolean shouldAdd) {
        if (shouldAdd)
            TemperatureUtil.addImmunity(player, TemperatureImmunityEnum.HIGH_ALTITUDE);
        else
            TemperatureUtil.removeImmunity(player, TemperatureImmunityEnum.HIGH_ALTITUDE);
    }

    private static void adaptOnFireImmunity(Player player, boolean shouldAdd) {
        if (shouldAdd)
            TemperatureUtil.addImmunity(player, TemperatureImmunityEnum.ON_FIRE);
        else
            TemperatureUtil.removeImmunity(player, TemperatureImmunityEnum.ON_FIRE);
    }

    private static void removeThirstEffect(Player player) {
        if (player.hasEffect(MobEffectRegistry.THIRST.get()))
            player.removeEffect(MobEffectRegistry.THIRST.get());
    }

    private static void addExtraThirstExhaustion(Player player, double extraExhaustion) {
        ThirstUtil.addExhaustion(player, (float) extraExhaustion);
    }
}
