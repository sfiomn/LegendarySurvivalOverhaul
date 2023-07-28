package sfiomn.legendarysurvivaloverhaul.api.thirst;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;

public interface IThirstCapability
{
	public float getThirstExhaustion();
	public int getThirstLevel();
	public float getThirstSaturation();
	public int getThirstTickTimer();
	public int getThirstDamageCounter();

	public void setThirstExhaustion(float exhaustion);
	public void setThirstLevel(int thirst);
	public void setThirstSaturation(float saturation);
	public void setThirstTickTimer(int ticktimer);
	public void setThirstDamageCounter(int damagecounter);

	public void addThirstExhaustion(float exhaustion);
	public void addThirstLevel(int thirst);
	public void addThirstSaturation(float saturation);
	public void addThirstTickTimer(int ticktimer);
	public void addThirstDamageCounter(int damagecounter);

	/**
	 * Check whether the thirst level is at maximum or not
	 * <br>
	 * @return boolean thirst is at maximum
	 */
	public boolean isThirstLevelAtMax();

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
	 * (Don't use this!) <br>
	 * Runs a tick update for the player's thirst capability
	 * @param player
	 * @param world
	 * @param phase
	 */
	public void tickUpdate(PlayerEntity player, World world, TickEvent.Phase phase);


	/**
	 * (Don't use this!) <br>
	 * Gets the current tick of the packet timer
	 * @return int packetTimer
	 */
	public int getPacketTimer();
}
