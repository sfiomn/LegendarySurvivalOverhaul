package sfiomn.legendarysurvivaloverhaul.api.temperature;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.RegistryObject;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;

public enum TemporaryModifierGroupEnum
{
	FOOD("food", MobEffectRegistry.HOT_FOOD, MobEffectRegistry.COLD_FOOD),
	DRINK("drink", MobEffectRegistry.HOT_DRINk, MobEffectRegistry.COLD_DRINK);
	
	public final String group;
	public final RegistryObject<MobEffect> hotEffect;
	public final RegistryObject<MobEffect> coldEffect;
	
	TemporaryModifierGroupEnum(String group, RegistryObject<MobEffect> hotEffect, RegistryObject<MobEffect> coldEffect)
	{
		this.group = group;
		this.hotEffect = hotEffect;
		this.coldEffect = coldEffect;
	}
}
