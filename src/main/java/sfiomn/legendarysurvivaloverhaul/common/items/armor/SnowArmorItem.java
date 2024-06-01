package sfiomn.legendarysurvivaloverhaul.common.items.armor;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.itemgroup.ModItemGroup;

public class SnowArmorItem extends ArmorItem
{
	public SnowArmorItem(IArmorMaterial material, EquipmentSlotType slot)
	{
		super(material, slot, new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP));
	}
	
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type)
	{
		return LegendarySurvivalOverhaul.MOD_ID + ":textures/models/armor/snow_armor_" + (slot == EquipmentSlotType.LEGS ? "2" : "1") + ".png";
	}
}
