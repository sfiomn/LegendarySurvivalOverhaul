package icey.survivaloverhaul.registry;

import java.lang.reflect.Field;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.common.items.*;
import icey.survivaloverhaul.common.items.armor.ArmorMaterialBase;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemRegistry
{
	// public static final Item EXAMPLE_ITEM = new ItemGeneric("example_item");
	public static final ArmorMaterialBase CLOTH_ARMOR_MATERIAL = new ArmorMaterialBase("snow", 5.75f, new int[] { 1, 1, 2, 1}, 17, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0, 0.0f, null);
	
	public static final Item MERCURY_PASTE = new ItemGeneric("mercury_paste", ItemGroup.MISC);
	
	public static final Item STONE_FERN_LEAF = new ItemGeneric("stone_fern_leaf", ItemGroup.BREWING);
	public static final Item INFERNAL_FERN_LEAF = new ItemGeneric("infernal_fern_leaf", ItemGroup.BREWING);
	
	public static final ArmorItem CLOTH_HELMET = new ItemSnowArmor(CLOTH_ARMOR_MATERIAL, EquipmentSlotType.HEAD);
	public static final ArmorItem CLOTH_CHEST = new ItemSnowArmor(CLOTH_ARMOR_MATERIAL, EquipmentSlotType.CHEST);
	public static final ArmorItem CLOTH_LEGS = new ItemSnowArmor(CLOTH_ARMOR_MATERIAL, EquipmentSlotType.LEGS);
	public static final ArmorItem CLOTH_BOOTS = new ItemSnowArmor(CLOTH_ARMOR_MATERIAL, EquipmentSlotType.FEET);
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) 
	{
		// Code taken from https://github.com/Alex-the-666/Ice_and_Fire/tree/1.16.3
		try {
			for (Field f : ItemRegistry.class.getDeclaredFields()) {
				Object obj = f.get(null);
				if (obj instanceof Item) {
					if(((Item) obj).getRegistryName() != null){
						event.getRegistry().register((Item) obj);
					}
				} else if (obj instanceof Item[]) {
					for (Item item : (Item[]) obj) {
						if(item.getRegistryName() != null){
							event.getRegistry().register(item);
						}
					}
				}
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
