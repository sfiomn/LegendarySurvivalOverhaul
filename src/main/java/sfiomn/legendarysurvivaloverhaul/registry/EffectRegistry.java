package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraftforge.eventbus.api.IEventBus;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.effects.*;
import net.minecraft.item.Items;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionBrewing;
import net.minecraft.potion.Potions;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EffectRegistry
{
	public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, LegendarySurvivalOverhaul.MOD_ID);
	public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTION_TYPES, LegendarySurvivalOverhaul.MOD_ID);
	
	public static final RegistryObject<Effect> FROSTBITE = EFFECTS.register("frostbite", FrostbiteEffect::new);
	public static final RegistryObject<Effect> HEAT_STROKE = EFFECTS.register("heat_stroke", HeatStrokeEffect::new);
	public static final RegistryObject<Effect> COLD_RESISTANCE = EFFECTS.register("cold_resist", ColdResistanceEffect::new);
	public static final RegistryObject<Effect> HEAT_RESISTANCE = EFFECTS.register("heat_resist", HeatResistanceEffect::new);
	
	public static final RegistryObject<Effect> EXHAUSTION = EFFECTS.register("exhaustion", () -> new ExhaustionEffect());

	public static final RegistryObject<Potion> HEAT_RESISTANCE_POTION = POTIONS.register("heat_resistance", () -> new Potion("heat_resistance", new EffectInstance(HEAT_RESISTANCE.get(), 3600, 0)));
	public static final RegistryObject<Potion> HEAT_RESISTANCE_POTION_LONG = POTIONS.register("heat_resistance_long", () -> new Potion("heat_resistance_long", new EffectInstance(HEAT_RESISTANCE.get(), 9600, 0)));
	public static final RegistryObject<Potion> COLD_RESISTANCE_POTION = POTIONS.register("cold_resistance", () -> new Potion("cold_resistance", new EffectInstance(COLD_RESISTANCE.get(), 3600, 0)));
	public static final RegistryObject<Potion> COLD_RESISTANCE_POTION_LONG = POTIONS.register("cold_resistance_long", () -> new Potion("cold_resistance_long", new EffectInstance(COLD_RESISTANCE.get(), 9600, 0)));
	
	public static void registerPotionRecipes()
	{
		PotionBrewing.addMix(Potions.AWKWARD, ItemRegistry.STONE_FERN_LEAF.get(), HEAT_RESISTANCE_POTION.get());
		PotionBrewing.addMix(HEAT_RESISTANCE_POTION.get(), Items.REDSTONE, HEAT_RESISTANCE_POTION_LONG.get());
		PotionBrewing.addMix(COLD_RESISTANCE_POTION.get(), Items.REDSTONE, COLD_RESISTANCE_POTION_LONG.get());
	}
	
	public static void register (IEventBus eventBus){
		EFFECTS.register(eventBus);
		POTIONS.register(eventBus);
	}
}
