package sfiomn.legendarysurvivaloverhaul.api.thirst;

import sfiomn.legendarysurvivaloverhaul.config.Config;

// Stolen shamelessly from Charles445's SimpleDifficulty mod
// https://github.com/Charles445/SimpleDifficulty/blob/v0.3.4/src/main/java/com/charles445/simpledifficulty/api/thirst/ThirstEnum.java
public enum HydrationEnum
{
	NORMAL	("normal", Config.Baked.hydrationWater, Config.Baked.saturationWater, Config.Baked.dirtyWater),
	RAIN	("rain", Config.Baked.hydrationRain,	Config.Baked.saturationRain, Config.Baked.dirtyRain),
	POTION	("potion", Config.Baked.hydrationPotion, Config.Baked.saturationPotion, Config.Baked.dirtyPotion),
	PURIFIED("purified", Config.Baked.hydrationPurified, Config.Baked.saturationPurified,Config.Baked.dirtyPurified);

	private String name;
	private int hydration;
	private float saturation;
	private float dirty;

	private HydrationEnum(String name, int hydration, float saturation, float dirty)
	{
		this.name = name;
		this.hydration = hydration;
		this.saturation=saturation;
		this.dirty=dirty;
	}

	public String getName()
	{
		return this.name;
	}

	public int getHydration()
	{
		return hydration;
	}

	public float getSaturation()
	{
		return saturation;
	}

	public float getDirtiness()
	{
		return dirty;
	}

	public String toString()
	{
		return this.getName();
	}
}
