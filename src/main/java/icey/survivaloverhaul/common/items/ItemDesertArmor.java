package icey.survivaloverhaul.common.items;

import icey.survivaloverhaul.Main;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ItemDesertArmor extends ArmorItem
{
	public ItemDesertArmor(IArmorMaterial material, EquipmentSlotType slot)
	{
		super(material, slot, new Item.Properties().group(ItemGroup.COMBAT));
	}
	
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type)
	{
		return Main.MOD_ID + ":textures/models/armor/desert_armor_" + (slot == EquipmentSlotType.LEGS ? "2" : "1") + ".png";
	}
}
