package sfiomn.legendarysurvivaloverhaul.api.temperature;

import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import sfiomn.legendarysurvivaloverhaul.registry.EffectRegistry;

public enum TemporaryModifierGroupEnum
{
	FOOD("food", EffectRegistry.HOT_FOOD, EffectRegistry.COLD_FOOD),
	DRINK("drink", EffectRegistry.HOT_DRINk, EffectRegistry.COLD_DRINK);
	
	public final String group;
	public final RegistryObject<Effect> hotEffect;
	public final RegistryObject<Effect> coldEffect;
	
	TemporaryModifierGroupEnum(String group, RegistryObject<Effect> hotEffect, RegistryObject<Effect> coldEffect)
	{
		this.group = group;
		this.hotEffect = hotEffect;
		this.coldEffect = coldEffect;
	}
}
