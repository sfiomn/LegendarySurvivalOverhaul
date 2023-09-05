package sfiomn.legendarysurvivaloverhaul.api.temperature;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public interface ITemperatureItemCapability
{
	public float getWorldTemperatureLevel();
	public void setWorldTemperatureLevel(float temperature);
	public boolean shouldUpdate(long currentTick);
	public void updateWorldTemperature(World world, Entity holder, long currentTick);
}
