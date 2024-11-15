package sfiomn.legendarysurvivaloverhaul.common.integration.curios;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;

import java.util.UUID;

import static sfiomn.legendarysurvivaloverhaul.util.internal.TemperatureUtilInternal.*;

public class CuriosModifier
{
	public CuriosModifier() {}

	public static void addAttribute(PlayerEntity player, ResourceLocation itemRegistryName) {
		if (!LegendarySurvivalOverhaul.curiosLoaded)
			return;

		if (itemRegistryName != null && JsonConfig.itemTemperatures.containsKey(itemRegistryName.toString())) {
			JsonTemperatureResistance tempConfig = JsonConfig.itemTemperatures.get(itemRegistryName.toString());

			UUID itemUuid = UUID.nameUUIDFromBytes(itemRegistryName.toString().getBytes());
			HEATING_TEMPERATURE.addModifier(player, itemUuid, Math.max(tempConfig.temperature, 0));
			COOLING_TEMPERATURE.addModifier(player, itemUuid, Math.min(tempConfig.temperature, 0));
			HEAT_RESISTANCE.addModifier(player, itemUuid, tempConfig.heatResistance);
			COLD_RESISTANCE.addModifier(player, itemUuid, tempConfig.coldResistance);
			THERMAL_RESISTANCE.addModifier(player, itemUuid, tempConfig.thermalResistance);
		}
	}

	public static void removeAttribute(PlayerEntity player, ResourceLocation itemRegistryName) {
		if (!LegendarySurvivalOverhaul.curiosLoaded)
			return;

		if (itemRegistryName != null) {
			UUID itemUuid = UUID.nameUUIDFromBytes(itemRegistryName.toString().getBytes());
			HEATING_TEMPERATURE.removeModifier(player, itemUuid);
			COOLING_TEMPERATURE.removeModifier(player, itemUuid);
			HEAT_RESISTANCE.removeModifier(player, itemUuid);
			COLD_RESISTANCE.removeModifier(player, itemUuid);
			THERMAL_RESISTANCE.removeModifier(player, itemUuid);
		}
	}
}
