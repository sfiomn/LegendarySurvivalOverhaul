package icey.survivaloverhaul.setup;

import java.lang.reflect.Field;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.common.items.*;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BannerPatternItem;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemRegistry
{
	// public static final Item EXAMPLE_ITEM = new ItemGeneric("example_item");
	public static final Item CANTEEN = new ItemCanteen();
	
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
