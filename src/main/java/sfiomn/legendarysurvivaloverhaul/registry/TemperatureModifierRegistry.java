package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.DynamicModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.common.integration.curios.CuriosModifier;
import sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsModifier;
import sfiomn.legendarysurvivaloverhaul.common.integration.terrafirmacraft.TerraFirmaCraftModifier;
import sfiomn.legendarysurvivaloverhaul.common.temperature.*;
import sfiomn.legendarysurvivaloverhaul.common.temperature.dynamic.*;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class TemperatureModifierRegistry
{
	public static final ResourceLocation MODIFIERS_RESOURCE = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "temperature_modifiers");
	public static final ResourceLocation DYNAMIC_MODIFIERS_RESOURCE = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "dynamic_temperature_modifiers");

	public static final DeferredRegister<ModifierBase> MODIFIERS = DeferredRegister.create(MODIFIERS_RESOURCE, LegendarySurvivalOverhaul.MOD_ID);
	public static final DeferredRegister<DynamicModifierBase> DYNAMIC_MODIFIERS = DeferredRegister.create(DYNAMIC_MODIFIERS_RESOURCE, LegendarySurvivalOverhaul.MOD_ID);

	public static final Supplier<IForgeRegistry<ModifierBase>> MODIFIERS_REGISTRY = MODIFIERS.makeRegistry(RegistryBuilder::new);
	public static final Supplier<IForgeRegistry<DynamicModifierBase>> DYNAMIC_MODIFIERS_REGISTRY = DYNAMIC_MODIFIERS.makeRegistry(RegistryBuilder::new);

	// Base Modifiers
	public static final RegistryObject<ModifierBase> DEFAULT = MODIFIERS.register("default", DimensionModifier::new);
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
	public static final RegistryObject<ModifierBase> TERRA_FIRMA_CRAFT = MODIFIERS.register("integration/terra_firma_craft", TerraFirmaCraftModifier::new);
	public static final RegistryObject<ModifierBase> CURIOS = MODIFIERS.register("integration/curios", CuriosModifier::new);

	public static final RegistryObject<DynamicModifierBase> ADAPTIVE_COAT = DYNAMIC_MODIFIERS.register("adaptive_coat", AdaptiveCoatModifier::new);

	public static void register(IEventBus eventBus){
		MODIFIERS.register(eventBus);
		DYNAMIC_MODIFIERS.register(eventBus);
	}
}
