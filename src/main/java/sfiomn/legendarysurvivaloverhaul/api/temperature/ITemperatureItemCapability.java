package sfiomn.legendarysurvivaloverhaul.api.temperature;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public interface ITemperatureItemCapability
{
	public float getWorldTemperatureLevel();

	public void setWorldTemperatureLevel(float temperature);

	public boolean shouldUpdate(long currentTick);

	public void updateWorldTemperature(Level world, Entity holder, long currentTick);
}
