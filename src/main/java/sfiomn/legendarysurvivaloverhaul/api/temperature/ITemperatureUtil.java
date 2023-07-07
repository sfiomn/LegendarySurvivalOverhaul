package sfiomn.legendarysurvivaloverhaul.api.temperature;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;

public interface ITemperatureUtil
{
	public float getPlayerTargetTemperature(PlayerEntity player);
	
	public float getWorldTemperature(World world, BlockPos pos);
	
	public float clampTemperature(float temperature);
	
	public TemperatureEnum getTemperatureEnum(float temperature);
	
	public void setArmorCoatTag(final ItemStack stack, String temperatureType);
	
	public String getArmorCoatTag(final ItemStack stack);
	
	public void removeArmorCoatTag(final ItemStack stack);
}
