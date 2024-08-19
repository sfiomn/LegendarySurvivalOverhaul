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

	public int getHydration() {
        switch (this) {
			case RAIN:
				return Config.Baked.hydrationRain;
            case POTION:
				return Config.Baked.hydrationPotion;
            case PURIFIED:
				return Config.Baked.hydrationPurified;
            default:
				return Config.Baked.hydrationWater;
        }
	}

	public double getSaturation() {
        switch (this) {
            case RAIN:
				return Config.Baked.saturationRain;
            case POTION:
				return Config.Baked.saturationPotion;
            case PURIFIED:
				return Config.Baked.saturationPurified;
            default:
				return Config.Baked.saturationWater;
        }
	}

	public double getEffectChance() {
        switch (this) {
            case RAIN:
				return Config.Baked.effectChanceRain;
            case POTION:
				return Config.Baked.effectChancePotion;
            case PURIFIED:
				return Config.Baked.effectChancePurified;
            default:
				return Config.Baked.effectChanceWater;
        }
	}

	public String getEffectName() {
        switch (this) {
            case RAIN:
				return Config.Baked.effectRain;
            case POTION:
				return Config.Baked.effectPotion;
            case PURIFIED:
				return Config.Baked.effectPurified;
            default:
				return Config.Baked.effectWater;
        }
	}

	public int getEffectDuration() {
		switch (this) {
			case RAIN:
				return Config.Baked.effectDurationRain;
			case POTION:
				return Config.Baked.effectDurationPotion;
			case PURIFIED:
				return Config.Baked.effectDurationPurified;
			default:
				return Config.Baked.effectDurationWater;
		}
	}

	public static HydrationEnum getByName(String name) {
		for (HydrationEnum hydrationEnum: HydrationEnum.values()) {
			if (hydrationEnum.getName().equals(name))
				return hydrationEnum;
		}
		return null;
	}

	public String toString()
	{
		return this.getName();
	}
}
