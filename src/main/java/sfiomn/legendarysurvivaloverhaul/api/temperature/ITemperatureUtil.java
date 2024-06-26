package sfiomn.legendarysurvivaloverhaul.api.temperature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ITemperatureUtil
{
	public float getPlayerTargetTemperature(Player player);
	
	public float getWorldTemperature(Level world, BlockPos pos);
	
	public float clampTemperature(float temperature);
	
	public TemperatureEnum getTemperatureEnum(float temperature);
	
	public void setArmorCoatTag(final ItemStack stack, String temperatureType);
	
	public String getArmorCoatTag(final ItemStack stack);
	
	public void removeArmorCoatTag(final ItemStack stack);
}
