package icey.survivaloverhaul.common.enchantments;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.config.Config;
import icey.survivaloverhaul.registry.EnchantRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

public class InsulationMagic extends GenericMagic
{	
	private static EquipmentSlotType[] slots = new EquipmentSlotType[] {EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};
	private MagicType MT;
	/**
	 * @param name of enchant
	 */
	public InsulationMagic(String name, MagicType MT) 
	{
		super(Rarity.RARE, EnchantmentType.WEARABLE, slots, new EnchantOptions(5));
		this.setRegistryName(Main.MOD_ID, name);
		this.MT = MT;
	}
	
	public boolean isTreasureEnchantment()
	{
		if (this.MT == MagicType.Both)
			return true;
		else
			return false;
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
	
	public static float calcLevelEffect(int lvl) 
	{
		float sum = 0;
		switch (lvl) 
		{
			case 1:
				sum = (float) (Config.Baked.enchantmentMultiplier * 5);
				break;
			case 2:
				sum = (float) (Config.Baked.enchantmentMultiplier * 6);
				break;
			case 3:
				sum = (float) (Config.Baked.enchantmentMultiplier * 7);
				break;
			case 4:
				sum = (float) (Config.Baked.enchantmentMultiplier * 10);
				break;
			case 5:
				sum = (float) (Config.Baked.enchantmentMultiplier * 15);
				break;
		}
		return sum;
	}
	
	protected boolean canApplyTogether(Enchantment ench) 
	{
		if (this.MT == MagicType.Both)
		{
			if (ench.getRegistryName() == EnchantRegistry.ModEnchants.COLD_BARRIER.getRegistryName() || ench.getRegistryName() == EnchantRegistry.ModEnchants.THERMAL_BARRIER.getRegistryName())
			{
				return false;
			}
		}
		else if (this.MT == MagicType.Cool)
		{
			if (ench.getRegistryName() == EnchantRegistry.ModEnchants.ADAPTIVE_BARRIER.getRegistryName() || ench.getRegistryName() == EnchantRegistry.ModEnchants.THERMAL_BARRIER.getRegistryName())
			{
				return false;
			}
		}
		else if (this.MT == MagicType.Heat)
		{
			if (ench.getRegistryName() == EnchantRegistry.ModEnchants.COLD_BARRIER.getRegistryName() || ench.getRegistryName() == EnchantRegistry.ModEnchants.ADAPTIVE_BARRIER.getRegistryName())
			{
				return false;
			}
		}
		
		return true;
	}
}
