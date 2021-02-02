package icey.survivaloverhaul.common.enchantments;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.config.Config;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

public class InsulationMagic extends GenericMagic
{	
	private final static EquipmentSlotType[] slots = new EquipmentSlotType[] {EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};
	private MagicType MT;
	/**
	 * @param name of enchant
	 */
	public InsulationMagic(String name, MagicType MT) 
	{
		super(Rarity.RARE, EnchantmentType.ARMOR, slots, new EnchantOptions(3));
		this.setRegistryName(Main.MOD_ID, name);
		this.MT = MT;
	}
	
	public MagicType getMagicType()
	{
		return this.MT;
	}
	
	@Override
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
	
	@Override
	protected boolean canApplyTogether(Enchantment ench) 
	{
		if (ench instanceof InsulationMagic)
		{
			InsulationMagic magic = (InsulationMagic) ench;
			
			if (this.getMagicType() != magic.getMagicType())
			{
				return false;
			}
		}
		
		return true;
	}
}
