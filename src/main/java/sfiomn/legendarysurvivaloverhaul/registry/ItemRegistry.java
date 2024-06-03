package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.item.CoatEnum;
import sfiomn.legendarysurvivaloverhaul.common.items.*;
import sfiomn.legendarysurvivaloverhaul.common.items.armor.ArmorMaterialBase;
import sfiomn.legendarysurvivaloverhaul.common.items.armor.DesertArmorItem;
import sfiomn.legendarysurvivaloverhaul.common.items.armor.SnowArmorItem;
import sfiomn.legendarysurvivaloverhaul.common.items.drink.*;
import sfiomn.legendarysurvivaloverhaul.common.items.heal.*;
import sfiomn.legendarysurvivaloverhaul.itemgroup.ModItemGroup;

public class ItemRegistry
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, LegendarySurvivalOverhaul.MOD_ID);
	
	public static final ArmorMaterialBase CLOTH_ARMOR_MATERIAL = new ArmorMaterialBase("snow", 5.75f, new int[] { 1, 1, 2, 1}, 17, SoundEvents.ARMOR_EQUIP_LEATHER, 0, 0.0f, null);
	public static final ArmorMaterialBase DESERT_ARMOR_MATERIAL = new ArmorMaterialBase("desert", 5.75f, new int[] { 1, 1, 2, 1}, 19, SoundEvents.ARMOR_EQUIP_LEATHER, 0, 0.0f, null);

	public static final RegistryObject<Item> THERMOMETER = ITEMS.register("thermometer", () -> new ThermometerItem(new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP)));
	public static final RegistryObject<Item> SEASONAL_CALENDAR = ITEMS.register("seasonal_calendar", () -> new SeasonalCalendarItem(new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP)));

	public static final RegistryObject<Item> CLOTH_HELMET = ITEMS.register("snow_helmet", () -> new SnowArmorItem(CLOTH_ARMOR_MATERIAL, EquipmentSlotType.HEAD));
	public static final RegistryObject<Item> CLOTH_CHEST = ITEMS.register("snow_chestplate", () -> new SnowArmorItem(CLOTH_ARMOR_MATERIAL, EquipmentSlotType.CHEST));
	public static final RegistryObject<Item> CLOTH_LEGGINGS = ITEMS.register("snow_leggings", () -> new SnowArmorItem(CLOTH_ARMOR_MATERIAL, EquipmentSlotType.LEGS));
	public static final RegistryObject<Item> CLOTH_BOOTS = ITEMS.register("snow_boots", () -> new SnowArmorItem(CLOTH_ARMOR_MATERIAL, EquipmentSlotType.FEET));

	public static final RegistryObject<Item> DESERT_HELMET = ITEMS.register("desert_helmet", () -> new DesertArmorItem(DESERT_ARMOR_MATERIAL, EquipmentSlotType.HEAD));
	public static final RegistryObject<Item> DESERT_CHEST = ITEMS.register("desert_chestplate", () -> new DesertArmorItem(DESERT_ARMOR_MATERIAL, EquipmentSlotType.CHEST));
	public static final RegistryObject<Item> DESERT_LEGGINGS = ITEMS.register("desert_leggings", () -> new DesertArmorItem(DESERT_ARMOR_MATERIAL, EquipmentSlotType.LEGS));
	public static final RegistryObject<Item> DESERT_BOOTS = ITEMS.register("desert_boots", () -> new DesertArmorItem(DESERT_ARMOR_MATERIAL, EquipmentSlotType.FEET));

	public static final RegistryObject<Item> COOLING_COAT_1 = ITEMS.register("cooling_coat_1", () -> new CoatItem(CoatEnum.COOLING_1, new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP).rarity(Rarity.COMMON)));
	public static final RegistryObject<Item> COOLING_COAT_2 = ITEMS.register("cooling_coat_2", () -> new CoatItem(CoatEnum.COOLING_2, new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> COOLING_COAT_3 = ITEMS.register("cooling_coat_3", () -> new CoatItem(CoatEnum.COOLING_3, new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> HEATING_COAT_1 = ITEMS.register("heating_coat_1", () -> new CoatItem(CoatEnum.HEATING_1, new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP).rarity(Rarity.COMMON)));
	public static final RegistryObject<Item> HEATING_COAT_2 = ITEMS.register("heating_coat_2", () -> new CoatItem(CoatEnum.HEATING_2, new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> HEATING_COAT_3 = ITEMS.register("heating_coat_3", () -> new CoatItem(CoatEnum.HEATING_3, new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> THERMAL_COAT_1 = ITEMS.register("thermal_coat_1", () -> new CoatItem(CoatEnum.THERMAL_1, new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP).rarity(Rarity.COMMON)));
	public static final RegistryObject<Item> THERMAL_COAT_2 = ITEMS.register("thermal_coat_2", () -> new CoatItem(CoatEnum.THERMAL_2, new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> THERMAL_COAT_3 = ITEMS.register("thermal_coat_3", () -> new CoatItem(CoatEnum.THERMAL_3, new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP).rarity(Rarity.EPIC)));

	public static final RegistryObject<Item> COLD_STRING = ITEMS.register("cold_string", () -> new Item(new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP).rarity(Rarity.COMMON)));
	public static final RegistryObject<Item> WARM_STRING = ITEMS.register("warm_string", () -> new Item(new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP).rarity(Rarity.COMMON)));

	public static final RegistryObject<Item> SUN_FERN = ITEMS.register("sun_fern", () -> new BlockItem(BlockRegistry.SUN_FERN.get(), new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP)));
	public static final RegistryObject<Item> ICE_FERN = ITEMS.register("ice_fern", () -> new BlockItem(BlockRegistry.ICE_FERN.get(), new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP)));

	// Thirst
	public static final RegistryObject<Item> CANTEEN = ITEMS.register("canteen", () -> new CanteenItem(new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP).rarity(Rarity.COMMON)));
	public static final RegistryObject<Item> LARGE_CANTEEN = ITEMS.register("large_canteen", () -> new LargeCanteenItem(new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP).rarity(Rarity.UNCOMMON)));

	public static final RegistryObject<Item> APPLE_JUICE = ITEMS.register("apple_juice", () -> new JuiceItem(new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP)));
	public static final RegistryObject<Item> BEETROOT_JUICE = ITEMS.register("beetroot_juice", () -> new JuiceItem(new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP)));
	public static final RegistryObject<Item> CACTUS_JUICE = ITEMS.register("cactus_juice", () -> new JuiceItem(new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP)));
	public static final RegistryObject<Item> CARROT_JUICE = ITEMS.register("carrot_juice", () -> new JuiceItem(new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP)));
	public static final RegistryObject<Item> CHORUS_FRUIT_JUICE = ITEMS.register("chorus_fruit_juice", () -> new JuiceItem(new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP)));
	public static final RegistryObject<Item> GOLDEN_APPLE_JUICE = ITEMS.register("golden_apple_juice", () -> new JuiceItem(new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> GOLDEN_CARROT_JUICE = ITEMS.register("golden_carrot_juice", () -> new JuiceItem(new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> GLISTERING_MELON_JUICE = ITEMS.register("glistering_melon_juice", () -> new JuiceItem(new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> MELON_JUICE = ITEMS.register("melon_juice", () -> new JuiceItem(new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP)));
	public static final RegistryObject<Item> PUMPKIN_JUICE = ITEMS.register("pumpkin_juice", () -> new JuiceItem(new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP)));
	public static final RegistryObject<Item> PURIFIED_WATER_BOTTLE = ITEMS.register("purified_water_bottle", () -> new PurifiedWaterBottleItem(new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP)));
	public static final RegistryObject<Item> WATER_PLANT_BAG = ITEMS.register("water_plant_bag", () -> new DrinkItem(new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP)));

	// Heart fruit
	public static final RegistryObject<Item> HEART_FRUIT = ITEMS.register("heart_fruit", () -> new HeartFruitItem(new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP).rarity(Rarity.RARE)));

	// Body Healing
	public static final RegistryObject<Item> HEALING_HERBS = ITEMS.register("healing_herbs", () -> new HealingHerbsItem(new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP)));
	public static final RegistryObject<Item> PLASTER = ITEMS.register("plaster", () -> new PlasterItem(new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP)));
	public static final RegistryObject<Item> BANDAGE = ITEMS.register("bandage", () -> new BandageItem(new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP)));
	public static final RegistryObject<Item> TONIC = ITEMS.register("tonic", () -> new TonicItem(new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> MEDIKIT = ITEMS.register("medikit", () -> new MedikitItem(new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> MORPHINE = ITEMS.register("morphine", () -> new MorphineItem(new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP)));

	public static void register(IEventBus eventBus){
		ITEMS.register(eventBus);
	}
}