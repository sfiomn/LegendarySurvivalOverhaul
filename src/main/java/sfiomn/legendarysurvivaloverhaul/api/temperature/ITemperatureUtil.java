package sfiomn.legendarysurvivaloverhaul.api.temperature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ItemAttributeModifierEvent;

import java.util.UUID;

public interface ITemperatureUtil
{
	public float getPlayerTargetTemperature(Player player);
	
	public float getWorldTemperature(Level world, BlockPos pos);
	
	public float clampTemperature(float temperature);

	public void applyItemAttributeModifiers(ItemAttributeModifierEvent event);

	public TemperatureEnum getTemperatureEnum(float temperature);

	public boolean hasImmunity(Player player, TemperatureImmunityEnum immunity);

	public void addImmunity(Player player, TemperatureImmunityEnum immunity);

	public void removeImmunity(Player player, TemperatureImmunityEnum immunity);

	public void addTemperatureModifier(Player player, double temperature, UUID uuid);

	public void addHeatResistanceModifier(Player player, double temperature, UUID uuid);

	public void addColdResistanceModifier(Player player, double temperature, UUID uuid);

	public void addThermalResistanceModifier(Player player, double temperature, UUID uuid);

	public void setArmorCoatTag(final ItemStack stack, String temperatureType);

	public String getArmorCoatTag(final ItemStack stack);

	public void removeArmorCoatTag(final ItemStack stack);
}
