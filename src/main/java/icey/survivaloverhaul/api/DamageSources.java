package icey.survivaloverhaul.api;

import icey.survivaloverhaul.Main;
import net.minecraft.util.DamageSource;

public class DamageSources
{
	public static final DamageSource DEHYDRATION = new DamageSource(Main.MOD_ID + ".dehydration").setDamageBypassesArmor().setDamageIsAbsolute();
	public static final DamageSource BOILING = new DamageSource(Main.MOD_ID + ".boiling").setFireDamage();
	public static final DamageSource INTERNAL_BOILING = new DamageSource(Main.MOD_ID + ".internal_boiling").setFireDamage().setDamageBypassesArmor();
	public static final DamageSource ELECTROCUTION = new DamageSource(Main.MOD_ID + ".electrocution").setDamageBypassesArmor().setDamageIsAbsolute();
	public static final DamageSource HYPOTHERMIA = new DamageSource(Main.MOD_ID + ".hypothermia").setDamageBypassesArmor();
	public static final DamageSource HYPERTHERMIA = new DamageSource(Main.MOD_ID + ".hyperthermia").setDamageBypassesArmor();
	public static final DamageSource FALLING_AND_GRABBING_CLIFF = new DamageSource(Main.MOD_ID + ".breaking_arms").setDamageBypassesArmor();
}
