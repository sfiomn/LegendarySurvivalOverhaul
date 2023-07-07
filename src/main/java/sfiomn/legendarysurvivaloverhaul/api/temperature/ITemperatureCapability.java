package sfiomn.legendarysurvivaloverhaul.api.temperature;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;

public interface ITemperatureCapability
{
	public float getTemperatureLevel();
	public float getTargetTemperatureLevel();
	public int getTemperatureTickTimer();
	public TemperatureEnum getTemperatureEnum();
	
	public void setTemperatureLevel(float temperature);
	public void setTargetTemperatureLevel(float targetTemperature);
	public void setTemperatureTickTimer(int tickTimer);
	
	public void addTemperatureLevel(float temperature);
	public void addTemperatureTickTimer(int tickTimer);
	
	/**
	 * (Don't use this!) <br>
	 * Runs a tick update for the player's temperature capability
	 * @param player
	 * @param world
	 * @param phase
	 */
	public void tickUpdate(PlayerEntity player, World world, TickEvent.Phase phase);
	
	/**
	 * (Don't use this!) <br>
	 * Checks if the capability needs an update
	 * @return boolean has temperature changed
	 */
	public boolean isDirty();
	/**
	 * (Don't use this!) <br>
	 * Sets the capability as updated
	 */
	public void setClean();
	
	/**
	 * (Don't use this!) <br>
	 * Gets the current tick of the packet timer
	 * @return int packetTimer
	 */
	public int getPacketTimer();
}
