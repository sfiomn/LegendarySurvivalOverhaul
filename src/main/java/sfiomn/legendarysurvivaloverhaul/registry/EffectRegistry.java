package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.*;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.crafting.NBTIngredient;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.effects.HydrationFillEffect;
import sfiomn.legendarysurvivaloverhaul.common.effects.*;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public class EffectRegistry
{
	public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, LegendarySurvivalOverhaul.MOD_ID);
	public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTION_TYPES, LegendarySurvivalOverhaul.MOD_ID);

	public static final RegistryObject<Effect> THIRST = EFFECTS.register("thirst", ThirstEffect::new);
	public static final RegistryObject<Potion> THIRST_POTION = POTIONS.register("thirst", () -> new Potion("thirst", new EffectInstance(THIRST.get(), 3600, 0, false, true, false)));
	public static final RegistryObject<Potion> THIRST_POTION_LONG = POTIONS.register("thirst_long", () -> new Potion("thirst_long", new EffectInstance(THIRST.get(), 9600, 0, false, true, false)));
	public static final RegistryObject<Effect> HYDRATION_FILL = EFFECTS.register("hydration_fill", HydrationFillEffect::new);
	public static final RegistryObject<Potion> HYDRATION_FILL_POTION = POTIONS.register("hydration_fill", () -> new Potion("hydration_fill", new EffectInstance(HYDRATION_FILL.get(), 3600, 0, false, true, true)));
	public static final RegistryObject<Potion> HYDRATION_FILL_POTION_LONG = POTIONS.register("hydration_fill_long", () -> new Potion("hydration_fill_long", new EffectInstance(HYDRATION_FILL.get(), 9600, 0, false, true, true)));

	public static final RegistryObject<Effect> FROSTBITE = EFFECTS.register("frostbite", FrostbiteEffect::new);
	public static final RegistryObject<Effect> COLD_HUNGER = EFFECTS.register("cold_hunger", ColdHungerEffect::new);
	public static final RegistryObject<Effect> HEAT_STROKE = EFFECTS.register("heat_stroke", HeatStrokeEffect::new);
	public static final RegistryObject<Effect> HEAT_THIRST = EFFECTS.register("heat_thirst", HeatThirstEffect::new);
	public static final RegistryObject<Effect> COLD_IMMUNITY = EFFECTS.register("cold_immunity", ColdImmunityEffect::new);
	public static final RegistryObject<Effect> HEAT_IMMUNITY = EFFECTS.register("heat_immunity", HeatImmunityEffect::new);
	public static final RegistryObject<Effect> TEMPERATURE_IMMUNITY = EFFECTS.register("temperature_immunity", TemperatureImmunityEffect::new);
	public static final RegistryObject<Potion> HEAT_IMMUNITY_POTION = POTIONS.register("heat_immunity", () -> new Potion("heat_immunity", new EffectInstance(HEAT_IMMUNITY.get(), 1800, 0, false, Config.Baked.showPotionEffectParticles, true)));
	public static final RegistryObject<Potion> HEAT_IMMUNITY_POTION_LONG = POTIONS.register("heat_immunity_long", () -> new Potion("heat_immunity_long", new EffectInstance(HEAT_IMMUNITY.get(), 2400, 0, false, Config.Baked.showPotionEffectParticles, true)));
	public static final RegistryObject<Potion> COLD_IMMUNITY_POTION = POTIONS.register("cold_immunity", () -> new Potion("cold_immunity", new EffectInstance(COLD_IMMUNITY.get(), 1800, 0, false, Config.Baked.showPotionEffectParticles, true)));
	public static final RegistryObject<Potion> COLD_IMMUNITY_POTION_LONG = POTIONS.register("cold_immunity_long", () -> new Potion("cold_immunity_long", new EffectInstance(COLD_IMMUNITY.get(), 2400, 0, false, Config.Baked.showPotionEffectParticles, true)));
	public static final RegistryObject<Potion> TEMPERATURE_IMMUNITY_POTION = POTIONS.register("temperature_immunity", () -> new Potion("temperature_immunity", new EffectInstance(TEMPERATURE_IMMUNITY.get(), 1800, 0, false, Config.Baked.showPotionEffectParticles, true)));
	public static final RegistryObject<Potion> TEMPERATURE_IMMUNITY_POTION_LONG = POTIONS.register("temperature_immunity_long", () -> new Potion("temperature_immunity_long", new EffectInstance(TEMPERATURE_IMMUNITY.get(), 2400, 0, false, Config.Baked.showPotionEffectParticles, true)));

	public static final RegistryObject<Effect> HOT_FOOD = EFFECTS.register("hot_food", () -> new SimpleAttributeEffect(EffectType.BENEFICIAL, 16714764, 1).addAttributeModifier(AttributeRegistry.HEATING_TEMPERATURE.get(), SimpleAttributeEffect.HOT_FOOD_ATTRIBUTE_UUID, 1.0, AttributeModifier.Operation.ADDITION));
	public static final RegistryObject<Effect> HOT_DRINk = EFFECTS.register("hot_drink", () -> new SimpleAttributeEffect(EffectType.BENEFICIAL, 16714764, 1).addAttributeModifier(AttributeRegistry.HEATING_TEMPERATURE.get(), SimpleAttributeEffect.HOT_DRINK_ATTRIBUTE_UUID, 1.0, AttributeModifier.Operation.ADDITION));
	public static final RegistryObject<Effect> COLD_FOOD = EFFECTS.register("cold_food", () -> new SimpleAttributeEffect(EffectType.BENEFICIAL, 1166574, 1).addAttributeModifier(AttributeRegistry.COOLING_TEMPERATURE.get(), SimpleAttributeEffect.COLD_FOOD_ATTRIBUTE_UUID, 1.0, AttributeModifier.Operation.ADDITION));
	public static final RegistryObject<Effect> COLD_DRINK = EFFECTS.register("cold_drink", () -> new SimpleAttributeEffect(EffectType.BENEFICIAL, 1166574, 1).addAttributeModifier(AttributeRegistry.COOLING_TEMPERATURE.get(), SimpleAttributeEffect.COLD_DRINK_ATTRIBUTE_UUID, 1.0, AttributeModifier.Operation.ADDITION));

	public static final RegistryObject<Effect> PAINKILLER = EFFECTS.register("painkiller", PainKillerEffect::new);

	public static final RegistryObject<Effect> HARD_FALLING = EFFECTS.register("hard_falling", HardFallingEffect::new);
	public static final RegistryObject<Effect> VULNERABILITY = EFFECTS.register("vulnerability", VulnerabilityEffect::new);
	public static final RegistryObject<Effect> HEADACHE = EFFECTS.register("headache", HeadacheEffect::new);

	public static void registerBrewingRecipes()
	{
		addBrewingRecipe(Potions.AWKWARD, ItemRegistry.SUN_FERN.get(), HEAT_IMMUNITY_POTION.get());
		addBrewingRecipe(Potions.AWKWARD, ItemRegistry.ICE_FERN.get(), COLD_IMMUNITY_POTION.get());
		addBrewingRecipe(HEAT_IMMUNITY_POTION.get(), Items.REDSTONE, HEAT_IMMUNITY_POTION_LONG.get());
		addBrewingRecipe(COLD_IMMUNITY_POTION.get(), Items.REDSTONE, COLD_IMMUNITY_POTION_LONG.get());
	}

	private static void addBrewingRecipe(Potion potionInput, Item ingredient, Potion potionResult)
	{
		BrewingRecipeRegistry.addRecipe(NBTIngredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), potionInput)), NBTIngredient.of(new ItemStack(ingredient)), PotionUtils.setPotion(new ItemStack(Items.POTION), potionResult));
	}
	
	public static void register (IEventBus eventBus){
		EFFECTS.register(eventBus);
		POTIONS.register(eventBus);
	}
}
