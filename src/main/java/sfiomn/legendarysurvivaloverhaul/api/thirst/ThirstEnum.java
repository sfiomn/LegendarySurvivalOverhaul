package sfiomn.legendarysurvivaloverhaul.api.thirst;

import sfiomn.legendarysurvivaloverhaul.config.Config;

// Stolen shamelessly from Charles445's SimpleDifficulty mod
// https://github.com/Charles445/SimpleDifficulty/blob/v0.3.4/src/main/java/com/charles445/simpledifficulty/api/thirst/ThirstEnum.java
public enum ThirstEnum
{
	NORMAL	("normal", Config.Baked.thirstWater, Config.Baked.saturationWater, Config.Baked.dirtyWater),
	RAIN	("rain", Config.Baked.thirstRain,	Config.Baked.saturationRain, Config.Baked.dirtyRain),
	POTION	("potion", Config.Baked.thirstPotion, Config.Baked.saturationPotion, Config.Baked.dirtyPotion),
	PURIFIED("purified", Config.Baked.thirstPurified, Config.Baked.saturationPurified,Config.Baked.dirtyPurified);

	private String name;
	private int thirst;
	private float saturation;
	private float dirty;

	private ThirstEnum(String name, int thirst, float saturation, float dirty)
	{
		this.name = name;
		this.thirst=thirst;
		this.saturation=saturation;
		this.dirty=dirty;
	}

	public String getName()
	{
		return this.name;
	}

	public int getThirst()
	{
		return thirst;
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
