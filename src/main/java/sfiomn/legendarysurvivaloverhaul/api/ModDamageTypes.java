package sfiomn.legendarysurvivaloverhaul.api;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

public class ModDamageTypes
{
	public static final ResourceKey<DamageType> HYPOTHERMIA = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID + ".hypothermia.json"));
	public static final ResourceKey<DamageType> HYPERTHERMIA = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID + ".hyperthermia"));
	public static final ResourceKey<DamageType> DEHYDRATION = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID + ".dehydration"));
}
