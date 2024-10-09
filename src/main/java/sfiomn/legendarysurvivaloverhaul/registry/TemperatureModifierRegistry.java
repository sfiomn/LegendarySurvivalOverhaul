package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.AttributeModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.DynamicModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.common.integration.origins.OriginsDynamicModifier;
import sfiomn.legendarysurvivaloverhaul.common.integration.origins.OriginsModifier;
import sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsModifier;
import sfiomn.legendarysurvivaloverhaul.common.integration.terrafirmacraft.TerraFirmaCraftModifier;
import sfiomn.legendarysurvivaloverhaul.common.temperature.*;
import sfiomn.legendarysurvivaloverhaul.common.temperature.attribute.CoatModifier;
import sfiomn.legendarysurvivaloverhaul.common.temperature.attribute.ItemModifier;
import sfiomn.legendarysurvivaloverhaul.common.temperature.dynamic.*;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class TemperatureModifierRegistry
{
	public static final ResourceLocation MODIFIERS_RESOURCE = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "temperature_modifiers");
	public static final ResourceLocation DYNAMIC_MODIFIERS_RESOURCE = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "dynamic_temperature_modifiers");
	public static final ResourceLocation ITEM_ATTRIBUTE_MODIFIERS_RESOURCE = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "item_attribute_temperature_modifiers");

	public static final DeferredRegister<ModifierBase> MODIFIERS = DeferredRegister.create(MODIFIERS_RESOURCE, LegendarySurvivalOverhaul.MOD_ID);
	public static final DeferredRegister<DynamicModifierBase> DYNAMIC_MODIFIERS = DeferredRegister.create(DYNAMIC_MODIFIERS_RESOURCE, LegendarySurvivalOverhaul.MOD_ID);
	public static final DeferredRegister<AttributeModifierBase> ITEM_ATTRIBUTE_MODIFIERS = DeferredRegister.create(ITEM_ATTRIBUTE_MODIFIERS_RESOURCE, LegendarySurvivalOverhaul.MOD_ID);

	public static final Supplier<IForgeRegistry<ModifierBase>> MODIFIERS_REGISTRY = MODIFIERS.makeRegistry(RegistryBuilder::new);
	public static final Supplier<IForgeRegistry<DynamicModifierBase>> DYNAMIC_MODIFIERS_REGISTRY = DYNAMIC_MODIFIERS.makeRegistry(RegistryBuilder::new);
	public static final Supplier<IForgeRegistry<AttributeModifierBase>> ITEM_ATTRIBUTE_MODIFIERS_REGISTRY = ITEM_ATTRIBUTE_MODIFIERS.makeRegistry(RegistryBuilder::new);

	// Base Modifiers
	public static final RegistryObject<ModifierBase> DIMENSION = MODIFIERS.register("dimension", DimensionModifier::new);
	public static final RegistryObject<ModifierBase> BIOME = MODIFIERS.register("biome", BiomeModifier::new);
	public static final RegistryObject<ModifierBase> TIME = MODIFIERS.register("time", TimeModifier::new);
	public static final RegistryObject<ModifierBase> ALTITUDE = MODIFIERS.register("altitude", AltitudeModifier::new);
	public static final RegistryObject<ModifierBase> SPRINT = MODIFIERS.register("sprint", SprintModifier::new);
	public static final RegistryObject<ModifierBase> BLOCKS = MODIFIERS.register("blocks", BlockModifier::new);
	public static final RegistryObject<ModifierBase> ENTITY = MODIFIERS.register("entity", EntityModifier::new);
	public static final RegistryObject<ModifierBase> ON_FIRE = MODIFIERS.register("on_fire", OnFireModifier::new);
	public static final RegistryObject<ModifierBase> WEATHER = MODIFIERS.register("weather", WeatherModifier::new);
	public static final RegistryObject<ModifierBase> PLAYER_HUDDLING = MODIFIERS.register("player_huddling", PlayerHuddlingModifier::new);
	public static final RegistryObject<ModifierBase> WETNESS = MODIFIERS.register("wetness", WetModifier::new);
	public static final RegistryObject<ModifierBase> TEMPERATURE_ATTRIBUTE = MODIFIERS.register("temperature_attribute", TemperatureAttributeModifier::new);
	
	// Mod Compat
	public static final RegistryObject<ModifierBase> SERENE_SEASONS = MODIFIERS.register("integration/serene_seasons", SereneSeasonsModifier::new);
	public static final RegistryObject<ModifierBase> TERRA_FIRMA_CRAFT = MODIFIERS.register("integration/terra_firma_craft", TerraFirmaCraftModifier::new);
	public static final RegistryObject<ModifierBase> ORIGINS = MODIFIERS.register("integration/origins", OriginsModifier::new);

	public static final RegistryObject<DynamicModifierBase> RESISTANCE_ATTRIBUTE = DYNAMIC_MODIFIERS.register("resistance_attribute", ResistanceAttributeModifier::new);
	public static final RegistryObject<DynamicModifierBase> ORIGINS_RESISTANCE = DYNAMIC_MODIFIERS.register("integration/origins_resistance", OriginsDynamicModifier::new);

	public static final RegistryObject<AttributeModifierBase> ITEM = ITEM_ATTRIBUTE_MODIFIERS.register("item", ItemModifier::new);
	public static final RegistryObject<AttributeModifierBase> COAT = ITEM_ATTRIBUTE_MODIFIERS.register("coat", CoatModifier::new);

	public static void register(IEventBus eventBus){
		MODIFIERS.register(eventBus);
		DYNAMIC_MODIFIERS.register(eventBus);
		ITEM_ATTRIBUTE_MODIFIERS.register(eventBus);
	}
}
