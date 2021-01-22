package icey.survivaloverhaul.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.Difficulty;

public class DamageUtil
{
	// Utility class for dealing with damaging players based on their
	// difficulty/mod settings
	
	public static boolean isModDangerous(World world)
	{
		// can the mod do thermal damage?
		
		if(world.getDifficulty() != Difficulty.PEACEFUL)
		{
			return true;
		}
		
		return false;
	}
	
	public static boolean healthAboveDifficulty(World world, PlayerEntity player)
	{
		// does the world's current difficulty and player's current health allow for environmental damage?
		
		Difficulty difficulty = world.getDifficulty();
		if(difficulty == Difficulty.HARD ||
				(difficulty == Difficulty.NORMAL && player.getHealth() > 1f) ||
				((difficulty == Difficulty.EASY || difficulty == Difficulty.PEACEFUL) && player.getHealth() > 10f)) 
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
