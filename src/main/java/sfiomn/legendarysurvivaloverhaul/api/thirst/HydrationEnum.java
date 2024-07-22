package sfiomn.legendarysurvivaloverhaul.api.thirst;

import sfiomn.legendarysurvivaloverhaul.config.Config;

// Stolen shamelessly from Charles445's SimpleDifficulty mod
// https://github.com/Charles445/SimpleDifficulty/blob/v0.3.4/src/main/java/com/charles445/simpledifficulty/api/thirst/ThirstEnum.java
public enum HydrationEnum
{
	NORMAL	("normal"),
	RAIN	("rain"),
	POTION	("potion"),
	PURIFIED("purified");

	private final String name;

	private HydrationEnum(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return this.name;
	}

	public int getHydration()
	{
		return switch (this) {
			case NORMAL -> Config.Baked.hydrationWater;
			case RAIN -> Config.Baked.hydrationRain;
            case POTION -> Config.Baked.hydrationPotion;
            case PURIFIED -> Config.Baked.hydrationPurified;
        };
	}

	public double getSaturation()
	{
		return switch (this) {
			case NORMAL -> Config.Baked.saturationWater;
			case RAIN -> Config.Baked.saturationRain;
			case POTION -> Config.Baked.saturationPotion;
			case PURIFIED -> Config.Baked.saturationPurified;
		};
	}

	public double getDirtiness()
	{
		return switch (this) {
			case NORMAL -> Config.Baked.dirtyWater;
			case RAIN -> Config.Baked.dirtyRain;
			case POTION -> Config.Baked.dirtyPotion;
			case PURIFIED -> Config.Baked.dirtyPurified;
		};
	}

	public String toString()
	{
		return this.getName();
	}
}
