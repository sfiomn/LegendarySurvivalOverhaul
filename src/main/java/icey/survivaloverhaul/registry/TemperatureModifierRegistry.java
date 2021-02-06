package icey.survivaloverhaul.registry;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.api.temperature.DynamicModifierBase;
import icey.survivaloverhaul.api.temperature.ModifierBase;
import icey.survivaloverhaul.common.compat.curios.CuriosModifier;
import icey.survivaloverhaul.common.compat.sereneseasons.SereneSeasonsModifier;
import icey.survivaloverhaul.common.temperature.*;
import icey.survivaloverhaul.common.temperature.dynamic.*;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class TemperatureModifierRegistry
{
	public static final DeferredRegister<ModifierBase> MODIFIERS = DeferredRegister.create(ModifierBase.class, Main.MOD_ID);
	public static final DeferredRegister<DynamicModifierBase> DYNAMIC_MODIFIERS = DeferredRegister.create(DynamicModifierBase.class, Main.MOD_ID);
	
	public static final RegistryObject<ModifierBase> DEFAULT = MODIFIERS.register("default", DefaultModifier::new);
	public static final RegistryObject<ModifierBase> BIOME = MODIFIERS.register("biome", BiomeModifier::new);
	public static final RegistryObject<ModifierBase> TIME = MODIFIERS.register("time", TimeModifier::new);
	public static final RegistryObject<ModifierBase> ALTITUDE = MODIFIERS.register("altitude", AltitudeModifier::new);
	public static final RegistryObject<ModifierBase> TEMPORARY = MODIFIERS.register("temporary", PlayerTemporaryModifier::new);
	public static final RegistryObject<ModifierBase> SPRINT = MODIFIERS.register("sprint", SprintModifier::new);
	public static final RegistryObject<ModifierBase> BLOCKS = MODIFIERS.register("blocks", BlockModifier::new);
	public static final RegistryObject<ModifierBase> ARMOR = MODIFIERS.register("armor", ArmorModifier::new);
	public static final RegistryObject<ModifierBase> ON_FIRE = MODIFIERS.register("on_fire", OnFireModifier::new);
	public static final RegistryObject<ModifierBase> WEATHER = MODIFIERS.register("weather", WeatherModifier::new);
	public static final RegistryObject<ModifierBase> PLAYER_HUDDLING = MODIFIERS.register("player_huddling", PlayerHuddlingModifier::new);
	public static final RegistryObject<ModifierBase> WETNESS = MODIFIERS.register("wetness", WetModifier::new);
	
	public static final RegistryObject<ModifierBase> SERENE_SEASONS = MODIFIERS.register("compat/serene_seasons", SereneSeasonsModifier::new);
	public static final RegistryObject<ModifierBase> CURIOS = MODIFIERS.register("compat/curios", CuriosModifier::new);
	
	public static final RegistryObject<DynamicModifierBase> ADAPTIVE_ENCHANT = DYNAMIC_MODIFIERS.register("adaptive_enchant", AdaptiveEnchantmentModifier::new);
}
