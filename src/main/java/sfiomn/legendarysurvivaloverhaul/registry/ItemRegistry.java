package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.item.CoatEnum;
import sfiomn.legendarysurvivaloverhaul.common.items.*;
import sfiomn.legendarysurvivaloverhaul.common.items.drink.*;
import sfiomn.legendarysurvivaloverhaul.common.items.heal.*;

import static sfiomn.legendarysurvivaloverhaul.common.items.armor.ArmorMaterialBase.DESERT;
import static sfiomn.legendarysurvivaloverhaul.common.items.armor.ArmorMaterialBase.SNOW;

public class ItemRegistry {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, LegendarySurvivalOverhaul.MOD_ID);

	public static final RegistryObject<Item> THERMOMETER = ITEMS.register("thermometer", () -> new ThermometerItem(new Item.Properties()));
	public static final RegistryObject<Item> SEASONAL_CALENDAR = ITEMS.register("seasonal_calendar", () -> new SeasonalCalendarItem(new Item.Properties()));

	public static final RegistryObject<Item> SNOW_HELMET = ITEMS.register("snow_helmet", () -> new ArmorItem(SNOW, ArmorItem.Type.HELMET, new Item.Properties()));
	public static final RegistryObject<Item> SNOW_CHEST = ITEMS.register("snow_chestplate", () -> new ArmorItem(SNOW, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
	public static final RegistryObject<Item> SNOW_LEGGINGS = ITEMS.register("snow_leggings", () -> new ArmorItem(SNOW, ArmorItem.Type.LEGGINGS, new Item.Properties()));
	public static final RegistryObject<Item> SNOW_BOOTS = ITEMS.register("snow_boots", () -> new ArmorItem(SNOW, ArmorItem.Type.BOOTS, new Item.Properties()));

	public static final RegistryObject<Item> DESERT_HELMET = ITEMS.register("desert_helmet", () -> new ArmorItem(DESERT, ArmorItem.Type.HELMET, new Item.Properties()));
	public static final RegistryObject<Item> DESERT_CHEST = ITEMS.register("desert_chestplate", () -> new ArmorItem(DESERT, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
	public static final RegistryObject<Item> DESERT_LEGGINGS = ITEMS.register("desert_leggings", () -> new ArmorItem(DESERT, ArmorItem.Type.LEGGINGS, new Item.Properties()));
	public static final RegistryObject<Item> DESERT_BOOTS = ITEMS.register("desert_boots", () -> new ArmorItem(DESERT, ArmorItem.Type.BOOTS, new Item.Properties()));

	public static final RegistryObject<Item> COOLING_COAT_1 = ITEMS.register("cooling_coat_1", () -> new CoatItem(CoatEnum.COOLING_1, new Item.Properties().rarity(Rarity.COMMON)));
	public static final RegistryObject<Item> COOLING_COAT_2 = ITEMS.register("cooling_coat_2", () -> new CoatItem(CoatEnum.COOLING_2, new Item.Properties().rarity(Rarity.RARE)));
	public static final RegistryObject<Item> COOLING_COAT_3 = ITEMS.register("cooling_coat_3", () -> new CoatItem(CoatEnum.COOLING_3, new Item.Properties().rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> HEATING_COAT_1 = ITEMS.register("heating_coat_1", () -> new CoatItem(CoatEnum.HEATING_1, new Item.Properties().rarity(Rarity.COMMON)));
	public static final RegistryObject<Item> HEATING_COAT_2 = ITEMS.register("heating_coat_2", () -> new CoatItem(CoatEnum.HEATING_2, new Item.Properties().rarity(Rarity.RARE)));
	public static final RegistryObject<Item> HEATING_COAT_3 = ITEMS.register("heating_coat_3", () -> new CoatItem(CoatEnum.HEATING_3, new Item.Properties().rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> THERMAL_COAT_1 = ITEMS.register("thermal_coat_1", () -> new CoatItem(CoatEnum.THERMAL_1, new Item.Properties().rarity(Rarity.COMMON)));
	public static final RegistryObject<Item> THERMAL_COAT_2 = ITEMS.register("thermal_coat_2", () -> new CoatItem(CoatEnum.THERMAL_2, new Item.Properties().rarity(Rarity.RARE)));
	public static final RegistryObject<Item> THERMAL_COAT_3 = ITEMS.register("thermal_coat_3", () -> new CoatItem(CoatEnum.THERMAL_3, new Item.Properties().rarity(Rarity.EPIC)));

	public static final RegistryObject<Item> COLD_STRING = ITEMS.register("cold_string", () -> new Item(new Item.Properties().rarity(Rarity.COMMON)));
	public static final RegistryObject<Item> WARM_STRING = ITEMS.register("warm_string", () -> new Item(new Item.Properties().rarity(Rarity.COMMON)));

	public static final RegistryObject<Item> SUN_FERN_SEEDS = ITEMS.register("sun_fern_seeds", () -> new ItemNameBlockItem(BlockRegistry.SUN_FERN_CROP.get(), new Item.Properties()));
	public static final RegistryObject<Item> SUN_FERN = ITEMS.register("sun_fern", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> ICE_FERN_SEEDS = ITEMS.register("ice_fern_seeds", () -> new ItemNameBlockItem(BlockRegistry.ICE_FERN_CROP.get(), new Item.Properties()));
	public static final RegistryObject<Item> ICE_FERN = ITEMS.register("ice_fern", () -> new Item(new Item.Properties()));

	// Thirst
	public static final RegistryObject<Item> CANTEEN = ITEMS.register("canteen", () -> new CanteenItem(new Item.Properties().rarity(Rarity.COMMON)));
	public static final RegistryObject<Item> LARGE_CANTEEN = ITEMS.register("large_canteen", () -> new LargeCanteenItem(new Item.Properties().rarity(Rarity.UNCOMMON)));

	public static final RegistryObject<Item> APPLE_JUICE = ITEMS.register("apple_juice", () -> new JuiceItem(new Item.Properties()));
	public static final RegistryObject<Item> BEETROOT_JUICE = ITEMS.register("beetroot_juice", () -> new JuiceItem(new Item.Properties()));
	public static final RegistryObject<Item> CACTUS_JUICE = ITEMS.register("cactus_juice", () -> new JuiceItem(new Item.Properties()));
	public static final RegistryObject<Item> CARROT_JUICE = ITEMS.register("carrot_juice", () -> new JuiceItem(new Item.Properties()));
	public static final RegistryObject<Item> CHORUS_FRUIT_JUICE = ITEMS.register("chorus_fruit_juice", () -> new JuiceItem(new Item.Properties()));
	public static final RegistryObject<Item> GOLDEN_APPLE_JUICE = ITEMS.register("golden_apple_juice", () -> new JuiceItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> GOLDEN_CARROT_JUICE = ITEMS.register("golden_carrot_juice", () -> new JuiceItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> GLISTERING_MELON_JUICE = ITEMS.register("glistering_melon_juice", () -> new JuiceItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> MELON_JUICE = ITEMS.register("melon_juice", () -> new JuiceItem(new Item.Properties()));
	public static final RegistryObject<Item> PUMPKIN_JUICE = ITEMS.register("pumpkin_juice", () -> new JuiceItem(new Item.Properties()));
	public static final RegistryObject<Item> PURIFIED_WATER_BOTTLE = ITEMS.register("purified_water_bottle", () -> new PurifiedWaterBottleItem(new Item.Properties()));
	public static final RegistryObject<Item> WATER_PLANT_BAG = ITEMS.register("water_plant_bag", () -> new DrinkItem(new Item.Properties()));
	public static final RegistryObject<Item> WATER_PLANT_SEEDS = ITEMS.register("water_plant_seeds", () -> new ItemNameBlockItem(BlockRegistry.WATER_PLANT_CROP.get(), new Item.Properties()));

	// Heart fruit
	public static final RegistryObject<Item> HEART_FRUIT = ITEMS.register("heart_fruit", () -> new HeartFruitItem(new Item.Properties().rarity(Rarity.RARE)));

	// Body Healing
	public static final RegistryObject<Item> HEALING_HERBS = ITEMS.register("healing_herbs", () -> new HealingHerbsItem(new Item.Properties()));
	public static final RegistryObject<Item> PLASTER = ITEMS.register("plaster", () -> new PlasterItem(new Item.Properties()));
	public static final RegistryObject<Item> BANDAGE = ITEMS.register("bandage", () -> new BandageItem(new Item.Properties()));
	public static final RegistryObject<Item> TONIC = ITEMS.register("tonic", () -> new TonicItem(new Item.Properties().rarity(Rarity.RARE)));
	public static final RegistryObject<Item> MEDIKIT = ITEMS.register("medikit", () -> new MedikitItem(new Item.Properties().rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> MORPHINE = ITEMS.register("morphine", () -> new MorphineItem(new Item.Properties()));

	public static void register(IEventBus eventBus){
		ITEMS.register(eventBus);
	}
}