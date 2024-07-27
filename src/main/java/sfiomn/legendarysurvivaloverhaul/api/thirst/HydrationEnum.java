package sfiomn.legendarysurvivaloverhaul.api.thirst;

import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.config.Config;

// Stolen shamelessly from Charles445's SimpleDifficulty mod
// https://github.com/Charles445/SimpleDifficulty/blob/v0.3.4/src/main/java/com/charles445/simpledifficulty/api/thirst/ThirstEnum.java
public enum HydrationEnum
{
	NORMAL	("normal", Config.Baked.hydrationWater, Config.Baked.saturationWater, Config.Baked.effectChanceWater, Config.Baked.effectWater),
	RAIN	("rain", Config.Baked.hydrationRain,	Config.Baked.saturationRain, Config.Baked.effectChanceRain, Config.Baked.effectRain),
	POTION	("potion", Config.Baked.hydrationPotion, Config.Baked.saturationPotion, Config.Baked.effectChancePotion, Config.Baked.effectPotion),
	PURIFIED("purified", Config.Baked.hydrationPurified, Config.Baked.saturationPurified,Config.Baked.effectChancePurified, Config.Baked.effectPurified);

	private final String name;
	private final int hydration;
	private final double saturation;
	private final double effectChance;
	private final String effectName;

	private HydrationEnum(String name, int hydration, double saturation, double effectChance, String effectName)
	{
		this.name = name;
		this.hydration = hydration;
		this.saturation=saturation;
		this.effectChance =effectChance;
		this.effectName =effectName;
	}

	public String getName()
	{
		return this.name;
	}

	public int getHydration()
	{
		return hydration;
	}

	public double getSaturation()
	{
		return saturation;
	}

	public double getEffectChance()
	{
		return effectChance;
	}

	public String getEffectName()
	{
		return effectName;
	}

	public Effect getEffectIfApplicable() {
		if (this.getEffectChance() > 0 && this.getEffectName().isEmpty())
			return ForgeRegistries.POTIONS.getValue(new ResourceLocation(this.getEffectName()));
		return null;
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
