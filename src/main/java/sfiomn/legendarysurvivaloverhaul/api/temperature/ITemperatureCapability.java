package sfiomn.legendarysurvivaloverhaul.api.temperature;

import com.google.common.collect.ImmutableMap;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;

public interface ITemperatureCapability
{
	public int getTemperatureLevel();
	public int getTemperatureTickTimer();
	public TemperatureEnum getTemperatureEnum();
	public ImmutableMap<String, TemporaryModifier> getTemporaryModifiers();
	
	public void setTemperatureLevel(int temperature);
	public void setTemperatureTickTimer(int tickTimer);
	public void setTemporaryModifier(String name, float temp, int duration);
	
	public void addTemperatureLevel(int temperature);
	public void addTemperatureTickTimer(int tickTimer);
	
	public void clearTemporaryModifiers();
	
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
