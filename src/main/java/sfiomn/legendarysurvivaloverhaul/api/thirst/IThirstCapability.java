package sfiomn.legendarysurvivaloverhaul.api.thirst;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;

public interface IThirstCapability
{
	public float getThirstExhaustion();
	public int getHydrationLevel();
	public float getSaturationLevel();
	public int getThirstTickTimer();
	public int getThirstDamageCounter();

	public void setThirstExhaustion(float exhaustion);
	public void setHydrationLevel(int thirst);
	public void setThirstSaturation(float saturation);
	public void setThirstTickTimer(int ticktimer);
	public void setThirstDamageCounter(int damagecounter);

	public void addThirstExhaustion(float exhaustion);
	public void addHydrationLevel(int thirst);
	public void addSaturationLevel(float saturation);
	public void addThirstTickTimer(int ticktimer);
	public void addThirstDamageCounter(int damagecounter);

	/**
	 * Check whether the hydration level is at maximum or not
	 * <br>
	 * @return boolean hydration is at maximum
	 */
	public boolean isHydrationLevelAtMax();

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
	public void tickUpdate(Player player, Level world, TickEvent.Phase phase);


	/**
	 * (Don't use this!) <br>
	 * Gets the current tick of the packet timer
	 * @return int packetTimer
	 */
	public int getPacketTimer();
}
