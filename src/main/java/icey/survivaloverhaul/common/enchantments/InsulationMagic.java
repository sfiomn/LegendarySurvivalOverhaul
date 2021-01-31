package icey.survivaloverhaul.common.enchantments;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.api.DamageSources;
import icey.survivaloverhaul.config.Config;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.DamageSource;

public class InsulationMagic extends GenericMagic
{	
	private static EquipmentSlotType[] slots = new EquipmentSlotType[] {EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};
	private MagicType MT;
	/**
	 * @param name of enchant
	 */
	public InsulationMagic(String name, MagicType MT) 
	{
		super(Rarity.RARE, EnchantmentType.ARMOR, slots, new EnchantOptions(5));
		this.setRegistryName(Main.MOD_ID, name);
		this.MT = MT;
	}
	
	public enum MagicType
	{
		Both(1),
		Heat(2),
		Cool(3);
		private int Type;
		
		public int getType() {
			return Type;
		}

		MagicType(int i) 
		{
			Type = i;
		}
	}
	
	public static float CalcLevelEffect(int lvl) 
	{
		float sum = 0;
		switch (lvl) 
		{
			case 1:
				sum = (float) (Config.Baked.enchantmentMultiplier * 3);
				break;
			case 2:
				sum = (float) (Config.Baked.enchantmentMultiplier * 4);
				break;
			case 3:
				sum = (float) (Config.Baked.enchantmentMultiplier * 5);
				break;
			case 4:
				sum = (float) (Config.Baked.enchantmentMultiplier * 7);
				break;
			case 5:
				sum = (float) (Config.Baked.enchantmentMultiplier * 10);
				break;
		}
		return sum;
	}
	@Override
	public int calcModifierDamage(int level, DamageSource source) 
	{
		int modifier;
		switch(level) 
		{
		case 4:
			modifier = 1;
			break;
		case 5:
			modifier = 2;
			break;
		default:
			modifier = 0;
		}
		
		if (source == DamageSources.HYPOTHERMIA || source == DamageSources.HYPERTHERMIA) 
			return modifier;
		return 0;
	}
}
