package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.item.PaddingEnum;
import sfiomn.legendarysurvivaloverhaul.common.items.*;
import sfiomn.legendarysurvivaloverhaul.common.items.armor.ArmorMaterialBase;

public class ItemRegistry
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, LegendarySurvivalOverhaul.MOD_ID);
	
	public static final ArmorMaterialBase CLOTH_ARMOR_MATERIAL = new ArmorMaterialBase("snow", 5.75f, new int[] { 1, 1, 2, 1}, 17, SoundEvents.ARMOR_EQUIP_LEATHER, 0, 0.0f, null);
	public static final ArmorMaterialBase DESERT_ARMOR_MATERIAL = new ArmorMaterialBase("desert", 5.75f, new int[] { 1, 1, 2, 1}, 19, SoundEvents.ARMOR_EQUIP_LEATHER, 0, 0.0f, null);

	public static final RegistryObject<Item> THERMOMETER = ITEMS.register("thermometer", () -> new ThermometerItem(new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
	
	public static final RegistryObject<Item> STONE_FERN_LEAF = ITEMS.register("stone_fern_leaf", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_BREWING)));
	public static final RegistryObject<Item> INFERNAL_FERN_LEAF = ITEMS.register("infernal_fern_leaf", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_BREWING)));

	public static final RegistryObject<Item> CLOTH_HELMET = ITEMS.register("snow_helmet", () -> new SnowArmorItem(CLOTH_ARMOR_MATERIAL, EquipmentSlotType.HEAD));
	public static final RegistryObject<Item> CLOTH_CHEST = ITEMS.register("snow_chestplate", () -> new SnowArmorItem(CLOTH_ARMOR_MATERIAL, EquipmentSlotType.CHEST));
	public static final RegistryObject<Item> CLOTH_LEGGINGS = ITEMS.register("snow_leggings", () -> new SnowArmorItem(CLOTH_ARMOR_MATERIAL, EquipmentSlotType.LEGS));
	public static final RegistryObject<Item> CLOTH_BOOTS = ITEMS.register("snow_boots", () -> new SnowArmorItem(CLOTH_ARMOR_MATERIAL, EquipmentSlotType.FEET));

	public static final RegistryObject<Item> DESERT_HELMET = ITEMS.register("desert_helmet", () -> new DesertArmorItem(DESERT_ARMOR_MATERIAL, EquipmentSlotType.HEAD));
	public static final RegistryObject<Item> DESERT_CHEST = ITEMS.register("desert_chestplate", () -> new DesertArmorItem(DESERT_ARMOR_MATERIAL, EquipmentSlotType.CHEST));
	public static final RegistryObject<Item> DESERT_LEGGINGS = ITEMS.register("desert_leggings", () -> new DesertArmorItem(DESERT_ARMOR_MATERIAL, EquipmentSlotType.LEGS));
	public static final RegistryObject<Item> DESERT_BOOTS = ITEMS.register("desert_boots", () -> new DesertArmorItem(DESERT_ARMOR_MATERIAL, EquipmentSlotType.FEET));

	public static final RegistryObject<Item> COOLER_PADDING_1 = ITEMS.register("cooler_padding_1", () -> new PaddingItem(PaddingEnum.COOLING_1, new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));
	public static final RegistryObject<Item> COOLER_PADDING_2 = ITEMS.register("cooler_padding_2", () -> new PaddingItem(PaddingEnum.COOLING_2, new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));
	public static final RegistryObject<Item> COOLER_PADDING_3 = ITEMS.register("cooler_padding_3", () -> new PaddingItem(PaddingEnum.COOLING_3, new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));
	public static final RegistryObject<Item> HEATER_PADDING_1 = ITEMS.register("heater_padding_1", () -> new PaddingItem(PaddingEnum.HEATING_1, new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));
	public static final RegistryObject<Item> HEATER_PADDING_2 = ITEMS.register("heater_padding_2", () -> new PaddingItem(PaddingEnum.HEATING_2, new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));
	public static final RegistryObject<Item> HEATER_PADDING_3 = ITEMS.register("heater_padding_3", () -> new PaddingItem(PaddingEnum.HEATING_3, new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));
	public static final RegistryObject<Item> THERMAL_PADDING_1 = ITEMS.register("thermal_padding_1", () -> new PaddingItem(PaddingEnum.THERMAL_1, new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));
	public static final RegistryObject<Item> THERMAL_PADDING_2 = ITEMS.register("thermal_padding_2", () -> new PaddingItem(PaddingEnum.THERMAL_2, new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));
	public static final RegistryObject<Item> THERMAL_PADDING_3 = ITEMS.register("thermal_padding_3", () -> new PaddingItem(PaddingEnum.THERMAL_3, new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));

	public static final RegistryObject<Item> HEART_FRUIT = ITEMS.register("heart_fruit", () -> new HeartFruitItem());
	
	// Block LegendaryTags
	public static final RegistryObject<Item> COOLER_ITEM = ITEMS.register("cooler", () -> new BlockItem(BlockRegistry.COOLER.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE)));
	public static final RegistryObject<Item> HEATER_ITEM = ITEMS.register("heater", () -> new BlockItem(BlockRegistry.HEATER.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE)));
	public static final RegistryObject<Item> SEWING_TABLE_ITEM = ITEMS.register("sewing_table", () -> new BlockItem(BlockRegistry.SEWING_TABLE.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));

	public static void register(IEventBus eventBus){
		ITEMS.register(eventBus);
	}
}