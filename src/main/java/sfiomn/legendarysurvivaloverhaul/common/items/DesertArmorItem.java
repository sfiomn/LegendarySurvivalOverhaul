package sfiomn.legendarysurvivaloverhaul.common.items;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.itemgroup.ModItemGroup;

public class DesertArmorItem extends ArmorItem
{
	public DesertArmorItem(IArmorMaterial material, EquipmentSlotType slot)
	{
		super(material, slot, new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP));
	}
	
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type)
	{
		return LegendarySurvivalOverhaul.MOD_ID + ":textures/models/armor/desert_armor_" + (slot == EquipmentSlotType.LEGS ? "2" : "1") + ".png";
	}
}
