package sfiomn.legendarysurvivaloverhaul.common.integration.origins;

import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.origin.Origin;
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

    private static boolean hasOriginFromList(Player player, java.util.List<? extends String> originIds) {
        if (!LegendarySurvivalOverhaul.originsLoaded || originIds == null || originIds.isEmpty()) {
            return false;
        }
        
        OriginComponent component = ModComponents.ORIGIN.get(player);
        for (Origin origin : component.getOrigins().values()) {
            ResourceLocation originId = origin.getIdentifier();
            String originIdString = originId.toString();
            if (originIds.contains(originIdString)) {
                return true;
            }
        }
        return false;
    }

    public static boolean canDrinkLava(Player player) {
        return hasOriginFromList(player, Config.Baked.originsWithLavaDrinking);
    }

    public static void assignOriginsFeatures(Player player) {
        if (!LegendarySurvivalOverhaul.originsLoaded) {
            return;
        }
        

        if (hasOriginFromList(player, Config.Baked.originsWithThirstEffectImmunity)) {
            removeThirstEffect(player);
        }
        if (hasOriginFromList(player, Config.Baked.originsWithExtraThirstExhaustion)) {
            addExtraThirstExhaustion(player, Config.Baked.originsExtraThirstExhaustionValue);
        }
        adaptWetnessDeactivation(player, hasOriginFromList(player, Config.Baked.originsWithWetnessImmunity));
        adaptHighAltitudeImmunity(player, hasOriginFromList(player, Config.Baked.originsWithHighAltitudeImmunity));
        adaptOnFireImmunity(player, hasOriginFromList(player, Config.Baked.originsWithOnFireImmunity));
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
