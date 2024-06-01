package sfiomn.legendarysurvivaloverhaul.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.Difficulty;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;

import java.util.*;

public final class DamageUtil
{
	// Utility class for dealing with damaging players based on their
	// difficulty/mod settings
	
	private DamageUtil() {}
	
	public static boolean isModDangerous(World world)
	{
		// can the mod do damage?
        return world.getDifficulty() != Difficulty.PEACEFUL;
    }
	
	public static boolean healthAboveDifficulty(World world, PlayerEntity player)
	{
		// Kill the player in HARD mode, let him have 1 health in NORMAL mode
		
		Difficulty difficulty = world.getDifficulty();
        return difficulty == Difficulty.HARD ||
                (difficulty == Difficulty.NORMAL && player.getHealth() > 1f) ||
                ((difficulty == Difficulty.EASY || difficulty == Difficulty.PEACEFUL) && player.getHealth() > 10f);
	}

	public static List<BodyPartEnum> getBodyPartsFromDamageSource(DamageSource damageSource) {
		switch (damageSource.msgId) {
			case "fall":
			case "hotFloor":
				return new ArrayList<>(Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT));
			case "fallingBlock":
			case "flyIntoWall":
			case "anvil":
				return Collections.singletonList(BodyPartEnum.HEAD);
			case "lightningBolt":
			case "onFire":
			case "lava":
				return new ArrayList<>(Arrays.asList(BodyPartEnum.values()));
			case "inFire":
				return new ArrayList<>(Arrays.asList(BodyPartEnum.LEFT_FOOT, BodyPartEnum.RIGHT_FOOT, BodyPartEnum.LEFT_LEG, BodyPartEnum.RIGHT_LEG));
			case "drown":
			case "starve":
				return Collections.singletonList(BodyPartEnum.CHEST);
			default:
				return Collections.emptyList();
		}
	}
}
