package sfiomn.legendarysurvivaloverhaul.api;

import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import net.minecraft.util.DamageSource;

public class DamageSources
{
	public static final DamageSource ELECTROCUTION = new DamageSource(LegendarySurvivalOverhaul.MOD_ID + ".electrocution").bypassArmor().bypassMagic();
	public static final DamageSource HYPOTHERMIA = new DamageSource(LegendarySurvivalOverhaul.MOD_ID + ".hypothermia").bypassArmor();
	public static final DamageSource HYPERTHERMIA = new DamageSource(LegendarySurvivalOverhaul.MOD_ID + ".hyperthermia").bypassArmor();
}
