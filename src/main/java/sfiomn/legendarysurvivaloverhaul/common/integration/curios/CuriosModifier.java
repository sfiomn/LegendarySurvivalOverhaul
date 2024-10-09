package sfiomn.legendarysurvivaloverhaul.common.integration.curios;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;
import sfiomn.legendarysurvivaloverhaul.registry.AttributeRegistry;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.event.CurioAttributeModifierEvent;
import top.theillusivec4.curios.api.type.ISlotType;

import java.util.UUID;

public class CuriosModifier
{
	public static final CurioAttributeBuilder HEATING_TEMPERATURE = new CurioAttributeBuilder(AttributeRegistry.HEATING_TEMPERATURE.get(), "attribute." + LegendarySurvivalOverhaul.MOD_ID + ".heating_temperature");
	public static final CurioAttributeBuilder COOLING_TEMPERATURE = new CurioAttributeBuilder(AttributeRegistry.COOLING_TEMPERATURE.get(), "attribute." + LegendarySurvivalOverhaul.MOD_ID + ".cooling_temperature");
	public static final CurioAttributeBuilder HEAT_RESISTANCE = new CurioAttributeBuilder(AttributeRegistry.HEAT_RESISTANCE.get(), "attribute." + LegendarySurvivalOverhaul.MOD_ID + ".heat_resistance");
	public static final CurioAttributeBuilder COLD_RESISTANCE = new CurioAttributeBuilder(AttributeRegistry.COLD_RESISTANCE.get(), "attribute." + LegendarySurvivalOverhaul.MOD_ID + ".cold_resistance");
	public static final CurioAttributeBuilder THERMAL_RESISTANCE = new CurioAttributeBuilder(AttributeRegistry.THERMAL_RESISTANCE.get(), "attribute." + LegendarySurvivalOverhaul.MOD_ID + ".thermal_resistance");

	public CuriosModifier() {}

	public static void addAttribute(CurioAttributeModifierEvent event) {
		if (!LegendarySurvivalOverhaul.curiosLoaded)
			return;

		ResourceLocation itemRegistryName = ForgeRegistries.ITEMS.getKey(event.getItemStack().getItem());

		if (itemRegistryName != null && JsonConfig.itemTemperatures.containsKey(itemRegistryName.toString())) {
			JsonTemperatureResistance tempConfig = JsonConfig.itemTemperatures.get(itemRegistryName.toString());

			for (ISlotType slot : CuriosApi.getItemStackSlots(event.getItemStack(), FMLLoader.getDist() == Dist.CLIENT).values()) {
				if (slot.getIdentifier().equals(event.getSlotContext().identifier())) {
					UUID itemUuid = UUID.nameUUIDFromBytes(itemRegistryName.toString().getBytes());
					HEATING_TEMPERATURE.addModifier(event, itemUuid, Math.max(tempConfig.temperature, 0));
					COOLING_TEMPERATURE.addModifier(event, itemUuid, Math.min(tempConfig.temperature, 0));
					HEAT_RESISTANCE.addModifier(event, itemUuid, tempConfig.heatResistance);
					COLD_RESISTANCE.addModifier(event, itemUuid, tempConfig.coldResistance);
					THERMAL_RESISTANCE.addModifier(event, itemUuid, tempConfig.thermalResistance);
				}
			}
		}
	}
}
