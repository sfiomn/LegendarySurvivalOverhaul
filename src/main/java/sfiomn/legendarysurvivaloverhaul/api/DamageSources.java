package sfiomn.legendarysurvivaloverhaul.api;

import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import net.minecraft.util.DamageSource;

public class DamageSources
{
	public static final DamageSource HYPOTHERMIA = new DamageSource(LegendarySurvivalOverhaul.MOD_ID + ".hypothermia").bypassArmor();
	public static final DamageSource HYPERTHERMIA = new DamageSource(LegendarySurvivalOverhaul.MOD_ID + ".hyperthermia").bypassArmor();
	public static final DamageSource DEHYDRATION = new DamageSource(LegendarySurvivalOverhaul.MOD_ID + ".dehydration").bypassArmor();
}
