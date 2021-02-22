package icey.survivaloverhaul.api;

import icey.survivaloverhaul.Main;
import net.minecraft.util.DamageSource;

public class DamageSources
{
	public static final DamageSource ELECTROCUTION = new DamageSource(Main.MOD_ID + ".electrocution").setDamageBypassesArmor().setDamageIsAbsolute();
	public static final DamageSource HYPOTHERMIA = new DamageSource(Main.MOD_ID + ".hypothermia").setDamageBypassesArmor();
	public static final DamageSource HYPERTHERMIA = new DamageSource(Main.MOD_ID + ".hyperthermia").setDamageBypassesArmor();
}
