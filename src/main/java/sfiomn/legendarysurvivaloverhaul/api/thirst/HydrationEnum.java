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
