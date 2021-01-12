package icey.survivaloverhaul.common.items;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.common.items.armor.ArmorMaterialBase;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvents;

public class ItemSnowArmor extends ArmorItem
{
	public ItemSnowArmor(IArmorMaterial material, EquipmentSlotType slot)
	{
		super(material, slot, new Item.Properties().group(ItemGroup.COMBAT));
		
		this.setRegistryName(Main.MOD_ID, "snow_" + slot.getName());
	}
	
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type)
	{
		return Main.MOD_ID + ":textures/models/armor/snow_armor_" + (slot == EquipmentSlotType.LEGS ? "2" : "1") + ".png";
	}
}
