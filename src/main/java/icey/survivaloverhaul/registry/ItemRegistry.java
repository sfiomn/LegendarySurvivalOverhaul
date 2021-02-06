package icey.survivaloverhaul.registry;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.common.items.*;
import icey.survivaloverhaul.common.items.armor.ArmorMaterialBase;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Main.MOD_ID);
	
	public static final ArmorMaterialBase CLOTH_ARMOR_MATERIAL = new ArmorMaterialBase("snow", 5.75f, new int[] { 1, 1, 2, 1}, 17, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0, 0.0f, null);
	public static final ArmorMaterialBase DESERT_ARMOR_MATERIAL = new ArmorMaterialBase("desert", 5.75f, new int[] { 1, 1, 2, 1}, 19, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0, 0.0f, null);
	
	public static final RegistryObject<Item> MERCURY_PASTE = ITEMS.register("mercury_paste", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
	public static final RegistryObject<Item> THERMOMETER = ITEMS.register("thermometer", () -> new Item(new Item.Properties().group(ItemGroup.TOOLS)));
	
	public static final RegistryObject<Item> STONE_FERN_LEAF = ITEMS.register("stone_fern_leaf", () -> new Item(new Item.Properties().group(ItemGroup.BREWING)));
	public static final RegistryObject<Item> INFERNAL_FERN_LEAF = ITEMS.register("infernal_fern_leaf", () -> new Item(new Item.Properties().group(ItemGroup.BREWING)));

	public static final RegistryObject<Item> CLOTH_HELMET = ITEMS.register("snow_helmet", () -> new ItemSnowArmor(CLOTH_ARMOR_MATERIAL, EquipmentSlotType.HEAD));
	public static final RegistryObject<Item> CLOTH_CHEST = ITEMS.register("snow_chestplate", () -> new ItemSnowArmor(CLOTH_ARMOR_MATERIAL, EquipmentSlotType.CHEST));
	public static final RegistryObject<Item> CLOTH_LEGGINGS = ITEMS.register("snow_leggings", () -> new ItemSnowArmor(CLOTH_ARMOR_MATERIAL, EquipmentSlotType.LEGS));
	public static final RegistryObject<Item> CLOTH_BOOTS = ITEMS.register("snow_boots", () -> new ItemSnowArmor(CLOTH_ARMOR_MATERIAL, EquipmentSlotType.FEET));

	public static final RegistryObject<Item> DESERT_HELMET = ITEMS.register("desert_helmet", () -> new ItemSnowArmor(DESERT_ARMOR_MATERIAL, EquipmentSlotType.HEAD));
	public static final RegistryObject<Item> DESERT_CHEST = ITEMS.register("desert_chestplate", () -> new ItemSnowArmor(DESERT_ARMOR_MATERIAL, EquipmentSlotType.CHEST));
	public static final RegistryObject<Item> DESERT_LEGGINGS = ITEMS.register("desert_leggings", () -> new ItemSnowArmor(DESERT_ARMOR_MATERIAL, EquipmentSlotType.LEGS));
	public static final RegistryObject<Item> DESERT_BOOTS = ITEMS.register("desert_boots", () -> new ItemSnowArmor(DESERT_ARMOR_MATERIAL, EquipmentSlotType.FEET));
	
	public static final RegistryObject<Item> HEART_FRUIT = ITEMS.register("heart_fruit", () -> new ItemHeartFruit());
	
	// Block Items
	public static final RegistryObject<Item> CINNABAR_ORE_ITEM = ITEMS.register("cinnabar_ore", () -> new BlockItem(BlockRegistry.CINNABAR_ORE.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
	
	public static final RegistryObject<Item> COOLING_COIL_ITEM = ITEMS.register("cooling_coil", () -> new BlockItem(BlockRegistry.COOLING_COIL.get(), new Item.Properties().group(ItemGroup.REDSTONE)));
	public static final RegistryObject<Item> HEATING_COIL_ITEM = ITEMS.register("heating_coil", () -> new BlockItem(BlockRegistry.HEATING_COIL.get(), new Item.Properties().group(ItemGroup.REDSTONE)));
}