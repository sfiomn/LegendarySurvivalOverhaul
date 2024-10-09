package sfiomn.legendarysurvivaloverhaul.api.wetness;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;

public interface IWetnessCapability
{
	public int getWetness();
	public int getWetnessTickTimer();

	public void setWetness(int exhaustion);
	public void setWetnessTickTimer(int tickTimer);

	public void addWetness(int wetness);
	public void addWetnessTickTimer(int tickTimer);

	/**
	 * (Don't use this!) <br>
	 * Checks if the capability needs an update
	 * @return boolean has thirst changed
	 */
	public boolean isDirty();

	/**
	 * (Don't use this!) <br>
	 * Sets the capability as updated
	 */
	public void setClean();

	/**
	 * Force the synchronization server - client
	 * of the thirst capability
	 */
	public void setDirty();

	/**
	 * (Don't use this!) <br>
	 * Runs a tick update for the player's thirst capability
	 * @param player
	 * @param world
	 * @param phase
	 */
	public void tickUpdate(Player player, Level world, TickEvent.Phase phase);


	/**
	 * (Don't use this!) <br>
	 * Gets the current tick of the packet timer
	 * @return int packetTimer
	 */
	public int getPacketTimer();
}
