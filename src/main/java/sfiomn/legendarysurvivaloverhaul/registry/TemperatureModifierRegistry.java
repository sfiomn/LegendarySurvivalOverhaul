package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraftforge.eventbus.api.IEventBus;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.AttributeModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.DynamicModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.common.integration.origins.OriginsDynamicModifier;
import sfiomn.legendarysurvivaloverhaul.common.integration.origins.OriginsModifier;
import sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsModifier;
import sfiomn.legendarysurvivaloverhaul.common.temperature.*;
import sfiomn.legendarysurvivaloverhaul.common.temperature.attribute.CoatModifier;
import sfiomn.legendarysurvivaloverhaul.common.temperature.attribute.ItemModifier;
import sfiomn.legendarysurvivaloverhaul.common.temperature.dynamic.*;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class TemperatureModifierRegistry
{
	public static final DeferredRegister<ModifierBase> MODIFIERS = DeferredRegister.create(ModifierBase.class, LegendarySurvivalOverhaul.MOD_ID);
	public static final DeferredRegister<DynamicModifierBase> DYNAMIC_MODIFIERS = DeferredRegister.create(DynamicModifierBase.class, LegendarySurvivalOverhaul.MOD_ID);
	public static final DeferredRegister<AttributeModifierBase> ITEM_ATTRIBUTE_MODIFIERS = DeferredRegister.create(AttributeModifierBase.class, LegendarySurvivalOverhaul.MOD_ID);
	
	// Base Modifiers
	public static final RegistryObject<ModifierBase> ALTITUDE = MODIFIERS.register("altitude", AltitudeModifier::new);
	public static final RegistryObject<ModifierBase> ATTRIBUTE = MODIFIERS.register("attribute", AttributeModifier::new);
	public static final RegistryObject<ModifierBase> BIOME = MODIFIERS.register("biome", BiomeModifier::new);
	public static final RegistryObject<ModifierBase> BLOCKS = MODIFIERS.register("blocks", BlockModifier::new);
	public static final RegistryObject<ModifierBase> DIMENSION = MODIFIERS.register("dimension", DimensionModifier::new);
	public static final RegistryObject<ModifierBase> ENTITY = MODIFIERS.register("entity", EntityModifier::new);
	public static final RegistryObject<ModifierBase> ON_FIRE = MODIFIERS.register("on_fire", OnFireModifier::new);
	public static final RegistryObject<ModifierBase> PLAYER_HUDDLING = MODIFIERS.register("player_huddling", PlayerHuddlingModifier::new);
	public static final RegistryObject<ModifierBase> TEMPORARY = MODIFIERS.register("temporary", PlayerTemporaryModifier::new);
	public static final RegistryObject<ModifierBase> SPRINT = MODIFIERS.register("sprint", SprintModifier::new);
	public static final RegistryObject<ModifierBase> TIME = MODIFIERS.register("time", TimeModifier::new);
	public static final RegistryObject<ModifierBase> WEATHER = MODIFIERS.register("weather", WeatherModifier::new);
	public static final RegistryObject<ModifierBase> WETNESS = MODIFIERS.register("wetness", WetModifier::new);

	public static final RegistryObject<AttributeModifierBase> ITEM = ITEM_ATTRIBUTE_MODIFIERS.register("item", ItemModifier::new);
	public static final RegistryObject<AttributeModifierBase> COAT = ITEM_ATTRIBUTE_MODIFIERS.register("coat", CoatModifier::new);

	public static final RegistryObject<DynamicModifierBase> RESISTANCE_ATTRIBUTE = DYNAMIC_MODIFIERS.register("resistance_attribute", ResistanceAttributeModifier::new);

	// Mod Compat
	public static final RegistryObject<ModifierBase> SERENE_SEASONS = MODIFIERS.register("integration/serene_seasons", SereneSeasonsModifier::new);

	public static final RegistryObject<ModifierBase> ORIGINS = MODIFIERS.register("integration/origins", OriginsModifier::new);
	public static final RegistryObject<DynamicModifierBase> ORIGINS_RESISTANCE = DYNAMIC_MODIFIERS.register("integration/origins_resistance", OriginsDynamicModifier::new);

	public static void register(IEventBus eventBus){
		MODIFIERS.register(eventBus);
		DYNAMIC_MODIFIERS.register(eventBus);
		ITEM_ATTRIBUTE_MODIFIERS.register(eventBus);
	}
}
