package sfiomn.legendarysurvivaloverhaul.common.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

public class InsulationMagic extends GenericMagic
{	
	private final static EquipmentSlotType[] slots = new EquipmentSlotType[] {EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};
	private final MagicType magicType;

	public InsulationMagic(MagicType magicType, Rarity rarity, EnchantOptions Options) 
	{
		super(rarity, EnchantmentType.ARMOR, slots, Options);
		this.magicType = magicType;
	}
	
	public MagicType getMagicType()
	{
		return this.magicType;
	}
	
	@Override
	public boolean isTreasureOnly()
	{
		return this.magicType == MagicType.Both;
	}
	
	public enum MagicType
	{
		Both(1),
		Heat(2),
		Cool(3);
		private final int Type;
		
		public int getType() {
			return Type;
		}

		MagicType(int i) 
		{
			Type = i;
		}
	}
	
	@Override
	protected boolean checkCompatibility(Enchantment ench)
	{
		if (ench instanceof InsulationMagic)
		{
			InsulationMagic magic = (InsulationMagic) ench;

			return this.getMagicType() == magic.getMagicType();
		}
		
		return true;
	}
}
