package sfiomn.legendarysurvivaloverhaul.common.items.armor;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

public class SnowArmorItem extends ArmorItem
{
	public SnowArmorItem(ArmorMaterial material, ArmorItem.Type slot)
	{
		super(material, slot, new Item.Properties());
	}
	
	public String getArmorTexture(ItemStack stack, Entity entity, ArmorItem.Type slot, String type)
	{
		return LegendarySurvivalOverhaul.MOD_ID + ":textures/models/armor/snow_armor_" + (slot == Type.LEGGINGS ? "2" : "1") + ".png";
	}
}
