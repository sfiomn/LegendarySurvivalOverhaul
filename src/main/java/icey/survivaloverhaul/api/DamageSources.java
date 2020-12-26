package icey.survivaloverhaul.api;

import net.minecraft.util.DamageSource;

public class DamageSources
{
	public static final DamageSource DEHYDRATION = new DamageSource("so_dehydration").setDamageBypassesArmor().setDamageIsAbsolute();
	public static final DamageSource ELECTROCUTION = new DamageSource("so_electrocution").setDamageBypassesArmor().setDamageIsAbsolute();
	public static final DamageSource HYPOTHERMIA = new DamageSource("so_hypothermia").setDamageBypassesArmor();
	public static final DamageSource HYPERTHERMIA = new DamageSource("so_hyperthermia").setDamageBypassesArmor();
}
