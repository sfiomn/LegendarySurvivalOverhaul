package sfiomn.legendarysurvivaloverhaul.common.capabilities.food;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import sfiomn.legendarysurvivaloverhaul.api.food.IFoodCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;


public class FoodCapability implements IFoodCapability
{

	//Unsaved data
	private Vector3d oldPos;
	private int updateTickTimer; // Update immediately first time around

	public FoodCapability()
	{
		this.init();
	}

	public void init()
	{
		this.oldPos = null;
		this.updateTickTimer = 0;
	}

	public void tickUpdate(PlayerEntity player, World world, TickEvent.Phase phase)
	{
		if(phase == TickEvent.Phase.START)
		{
			return;
		}

		if (oldPos == null)
			oldPos = player.position();

		updateTickTimer++;
		if(updateTickTimer >= 10)
		{
			updateTickTimer = 0;

			// if player is not in pause, trigger the food exhaust, allowing afk player not dying from hunger
			if (oldPos.distanceTo(player.position()) > 1) {
				player.getFoodData().addExhaustion((float) Config.Baked.baseFoodExhaustion);
				oldPos = player.position();
			}
		}
	}
}
