package sfiomn.legendarysurvivaloverhaul.api.food;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
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
	public void tickUpdate(Player player, Level world, TickEvent.Phase phase);
}
