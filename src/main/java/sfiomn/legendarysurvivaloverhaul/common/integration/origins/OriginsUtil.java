package sfiomn.legendarysurvivaloverhaul.common.integration.origins;

import io.github.apace100.origins.Origins;
import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginRegistry;
import io.github.apace100.origins.registry.ModComponents;
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
    public static Origin BLAZEBORN = OriginRegistry.get(new ResourceLocation(Origins.MODID, "blazeborn"));
    public static Origin MERLING = OriginRegistry.get(new ResourceLocation(Origins.MODID, "merling"));
    public static Origin PHANTOM = OriginRegistry.get(new ResourceLocation(Origins.MODID, "phantom"));
    public static Origin AVIAN = OriginRegistry.get(new ResourceLocation(Origins.MODID, "avian"));
    public static Origin ELYTRIAN = OriginRegistry.get(new ResourceLocation(Origins.MODID, "elytrian"));
    public static Origin SHULK = OriginRegistry.get(new ResourceLocation(Origins.MODID, "shulk"));

    public static boolean isOrigin(Player player, Origin origin) {
        return LegendarySurvivalOverhaul.originsLoaded &&
                ModComponents.ORIGIN.get(player).getOrigins().containsValue(origin);
    }

    public static void assignOriginsFeatures(Player player) {
        OriginComponent component = ModComponents.ORIGIN.get(player);
        
        if (component.getOrigins().containsValue(MERLING)) {
            removeThirstEffect(player);
        } else if (component.getOrigins().containsValue(SHULK)) {
            addExtraThirstExhaustion(player, Config.Baked.extraThirstExhaustionShulk);
        } else if (component.getOrigins().containsValue(PHANTOM)) {
            addExtraThirstExhaustion(player, Config.Baked.extraThirstExhaustionPhantom);
        }

        adaptWetnessDeactivation(player,
                component.getOrigins().containsValue(MERLING) ||
                        component.getOrigins().containsValue(BLAZEBORN));

        adaptHighAltitudeImmunity(player,
                component.getOrigins().containsValue(AVIAN) ||
                        component.getOrigins().containsValue(ELYTRIAN));

        adaptOnFireImmunity(player, component.getOrigins().containsValue(BLAZEBORN));
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
