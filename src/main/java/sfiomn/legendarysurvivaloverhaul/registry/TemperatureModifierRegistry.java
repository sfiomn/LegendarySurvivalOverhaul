package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraftforge.eventbus.api.IEventBus;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.DynamicModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.common.integration.curios.CuriosModifier;
import sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsModifier;
import sfiomn.legendarysurvivaloverhaul.common.temperature.*;
import sfiomn.legendarysurvivaloverhaul.common.temperature.dynamic.*;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class TemperatureModifierRegistry
{
	public static final DeferredRegister<ModifierBase> MODIFIERS = DeferredRegister.create(ModifierBase.class, LegendarySurvivalOverhaul.MOD_ID);
	public static final DeferredRegister<DynamicModifierBase> DYNAMIC_MODIFIERS = DeferredRegister.create(DynamicModifierBase.class, LegendarySurvivalOverhaul.MOD_ID);
	
	// Base Modifiers
	public static final RegistryObject<ModifierBase> DIMENSION = MODIFIERS.register("dimension", DimensionModifier::new);
	public static final RegistryObject<ModifierBase> BIOME = MODIFIERS.register("biome", BiomeModifier::new);
	public static final RegistryObject<ModifierBase> TIME = MODIFIERS.register("time", TimeModifier::new);
	public static final RegistryObject<ModifierBase> ALTITUDE = MODIFIERS.register("altitude", AltitudeModifier::new);
	public static final RegistryObject<ModifierBase> TEMPORARY = MODIFIERS.register("temporary", PlayerTemporaryModifier::new);
	public static final RegistryObject<ModifierBase> SPRINT = MODIFIERS.register("sprint", SprintModifier::new);
	public static final RegistryObject<ModifierBase> BLOCKS = MODIFIERS.register("blocks", BlockModifier::new);
	public static final RegistryObject<ModifierBase> ARMOR = MODIFIERS.register("armor", ArmorModifier::new);
	public static final RegistryObject<ModifierBase> HELD_ITEMS = MODIFIERS.register("held_items", HeldItemsModifier::new);
	public static final RegistryObject<ModifierBase> ENTITY = MODIFIERS.register("entity", EntityModifier::new);
	public static final RegistryObject<ModifierBase> ON_FIRE = MODIFIERS.register("on_fire", OnFireModifier::new);
	public static final RegistryObject<ModifierBase> WEATHER = MODIFIERS.register("weather", WeatherModifier::new);
	public static final RegistryObject<ModifierBase> PLAYER_HUDDLING = MODIFIERS.register("player_huddling", PlayerHuddlingModifier::new);
	public static final RegistryObject<ModifierBase> WETNESS = MODIFIERS.register("wetness", WetModifier::new);
	
	// Mod Compat
	public static final RegistryObject<ModifierBase> SERENE_SEASONS = MODIFIERS.register("integration/serene_seasons", SereneSeasonsModifier::new);
	public static final RegistryObject<ModifierBase> CURIOS = MODIFIERS.register("integration/curios", CuriosModifier::new);

	public static final RegistryObject<DynamicModifierBase> ADAPTIVE_COAT = DYNAMIC_MODIFIERS.register("adaptive_coat", AdaptiveCoatModifier::new);

	public static void register(IEventBus eventBus){
		MODIFIERS.register(eventBus);
		DYNAMIC_MODIFIERS.register(eventBus);
	}
}
