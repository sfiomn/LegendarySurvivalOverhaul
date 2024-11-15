package sfiomn.legendarysurvivaloverhaul.api.temperature;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ItemAttributeModifierEvent;

import java.util.UUID;

public interface ITemperatureUtil
{
	public float getPlayerTargetTemperature(PlayerEntity player);
	
	public float getWorldTemperature(World world, BlockPos pos);
	
	public float clampTemperature(float temperature);

	public void applyItemAttributeModifiers(ItemAttributeModifierEvent event);
	
	public TemperatureEnum getTemperatureEnum(float temperature);

	public boolean hasImmunity(PlayerEntity player, TemperatureImmunityEnum immunity);

	public void addImmunity(PlayerEntity player, TemperatureImmunityEnum immunity);

	public void removeImmunity(PlayerEntity player, TemperatureImmunityEnum immunity);

	public void addTemperatureModifier(PlayerEntity player, double temperature, UUID uuid);

	public void addHeatResistanceModifier(PlayerEntity player, double temperature, UUID uuid);

	public void addColdResistanceModifier(PlayerEntity player, double temperature, UUID uuid);

	public void addThermalResistanceModifier(PlayerEntity player, double temperature, UUID uuid);
	
	public void setArmorCoatTag(final ItemStack stack, String temperatureType);
	
	public String getArmorCoatTag(final ItemStack stack);
	
	public void removeArmorCoatTag(final ItemStack stack);
}
