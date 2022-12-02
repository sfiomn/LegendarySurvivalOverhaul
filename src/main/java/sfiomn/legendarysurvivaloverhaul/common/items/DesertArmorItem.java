package sfiomn.legendarysurvivaloverhaul.common.items;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

public class DesertArmorItem extends ArmorItem
{
	public DesertArmorItem(IArmorMaterial material, EquipmentSlotType slot)
	{
		super(material, slot, new Item.Properties().tab(ItemGroup.TAB_BREWING));
	}
	
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type)
	{
		return LegendarySurvivalOverhaul.MOD_ID + ":textures/models/armor/desert_armor_" + (slot == EquipmentSlotType.LEGS ? "2" : "1") + ".png";
	}
}
