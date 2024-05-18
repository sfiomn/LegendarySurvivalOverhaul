package sfiomn.legendarysurvivaloverhaul.api.bodydamage;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;

public interface IBodyDamageCapability
{

	/**
	 * (Don't use this!) <br>
	 * Checks if the capability needs an update
	 * @return boolean has localized body damage changed
	 */
	public boolean isDirty();

	/**
	 * (Don't use this!) <br>
	 * Sets the capability as updated
	 */
	public void setClean();

	/**
	 * (Don't use this!) <br>
	 * Runs a tick update for the player's localized body damage capability
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
