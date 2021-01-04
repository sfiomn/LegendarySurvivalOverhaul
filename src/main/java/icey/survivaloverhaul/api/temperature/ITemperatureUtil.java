package icey.survivaloverhaul.api.temperature;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;

public interface ITemperatureUtil
{
	public int getPlayerTargetTemperature(PlayerEntity player);
	
	public int getWorldTemperature(World world, BlockPos pos);
	
	public int clampTemperature(int temperature);
	
	public TemperatureStateEnum getTemperatureEnum(int temp);
	
	public void setArmorTemperatureTag(final ItemStack stack, float temperature);
	
	public float getArmorTemperatureTag(final ItemStack stack);
	
	public void removeArmorTemperatureTag(final ItemStack stack);
}
