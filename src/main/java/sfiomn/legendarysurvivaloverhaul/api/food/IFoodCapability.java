package sfiomn.legendarysurvivaloverhaul.api.food;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;

public interface IFoodCapability
{
	/**
	 * (Don't use this!) <br>
	 * Runs a tick update for the player's food capability
	 * @param player
	 * @param world
	 * @param phase
	 */
	public void tickUpdate(PlayerEntity player, World world, TickEvent.Phase phase);
}
