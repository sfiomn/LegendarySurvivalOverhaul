package sfiomn.legendarysurvivaloverhaul.api.bodydamage;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;

public interface IBodyDamageCapability
{
	public float getBodyPartDamage(BodyPartEnum part);

	public float getBodyPartHealthRatio(BodyPartEnum part);

	public float getBodyPartMaxHealth(BodyPartEnum part);

	public void setBodyPartDamage(BodyPartEnum part, float healthValue);

	public void setBodyPartMaxHealth(BodyPartEnum part, float maxHealthValue);

	public void heal(BodyPartEnum part, float healingValue);

	public void hurt(BodyPartEnum part, float damageValue);

	public void applyHealingItem(BodyPartEnum part, int healingTicks, float healingPerTick);

	public boolean shouldFlash(BodyPartEnum part);

	public boolean hasFlash(BodyPartEnum part);

	public float getRemainingHealingTicks(BodyPartEnum part);

	public float getHealingPerTicks(BodyPartEnum part);

	/**
	 * Check if at least one body part is wounded
	 * @return isWounded or not
	 */
	public boolean isWounded();

	/**
	 * Get the body part ratio related to the malus body part
	 */
	public float getHealthRatioForMalusBodyPart(MalusBodyPartEnum part);

	/**
	 * Force the health body damage sync server - client
	 */
	public void setManualDirty();

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
	 * Gets the current tick of the packet timer
	 * @return int packetTimer
	 */
	public int getPacketTimer();

	/**
	 * (Don't use this!) <br>
	 * Runs a tick update for the player's localized body damage capability
	 * @param player
	 * @param world
	 * @param phase
	 */
	public void tickUpdate(PlayerEntity player, World world, TickEvent.Phase phase);
}
