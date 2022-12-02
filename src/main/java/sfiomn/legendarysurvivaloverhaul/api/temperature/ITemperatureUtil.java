package sfiomn.legendarysurvivaloverhaul.api.temperature;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;

public interface ITemperatureUtil
{
	public int getPlayerTargetTemperature(PlayerEntity player);
	
	public int getWorldTemperature(World world, BlockPos pos);
	
	public int clampTemperature(int temperature);
	
	public TemperatureEnum getTemperatureEnum(int temp);
	
	public void setArmorPaddingTag(final ItemStack stack, String temperatureType);
	
	public String getArmorPaddingTag(final ItemStack stack);
	
	public void removeArmorPaddingTag(final ItemStack stack);
}
