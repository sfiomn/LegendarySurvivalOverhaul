package sfiomn.legendarysurvivaloverhaul.util;

import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public final class DamageUtil
{
	// Utility class for dealing with damaging players based on their
	// difficulty/mod settings
	
	private DamageUtil() {}
	
	public static boolean isModDangerous(Level level)
	{
		// can the mod do damage?
        return level.getDifficulty() != Difficulty.PEACEFUL;
    }
	
	public static boolean healthAboveDifficulty(Level level, Player player)
	{
		// Kill the player in HARD mode, let him have 2 health in NORMAL mode
		
		Difficulty difficulty = level.getDifficulty();
        return difficulty == Difficulty.HARD ||
                (difficulty == Difficulty.NORMAL && player.getHealth() > 2f) ||
                ((difficulty == Difficulty.EASY || difficulty == Difficulty.PEACEFUL) && player.getHealth() > 10f);
	}
}
