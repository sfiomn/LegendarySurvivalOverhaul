package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.crafting.StrictNBTIngredient;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.effects.*;

public class MobEffectRegistry {

	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, LegendarySurvivalOverhaul.MOD_ID);
	public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, LegendarySurvivalOverhaul.MOD_ID);

	public static final RegistryObject<MobEffect> THIRST = EFFECTS.register("thirst", ThirstEffect::new);
	public static final RegistryObject<Potion> THIRST_POTION = POTIONS.register("thirst", () -> new Potion("thirst", new MobEffectInstance(THIRST.get(), 3600, 0, false, true, true)));
	public static final RegistryObject<Potion> THIRST_POTION_LONG = POTIONS.register("thirst_long", () -> new Potion("thirst_long", new MobEffectInstance(THIRST.get(), 9600, 0, false, true, true)));

	public static final RegistryObject<MobEffect> FROSTBITE = EFFECTS.register("frostbite", FrostbiteEffect::new);
	public static final RegistryObject<MobEffect> COLD_HUNGER = EFFECTS.register("cold_hunger", ColdHungerEffect::new);
	public static final RegistryObject<MobEffect> HEAT_STROKE = EFFECTS.register("heat_stroke", HeatStrokeEffect::new);
	public static final RegistryObject<MobEffect> HEAT_THIRST = EFFECTS.register("heat_thirst", HeatThirstEffect::new);
	public static final RegistryObject<MobEffect> COLD_IMMUNITY = EFFECTS.register("cold_immunity", ColdImmunityEffect::new);
	public static final RegistryObject<MobEffect> HEAT_IMMUNITY = EFFECTS.register("heat_immunity", HeatImmunityEffect::new);
	public static final RegistryObject<MobEffect> TEMPERATURE_IMMUNITY = EFFECTS.register("temperature_immunity", HeatImmunityEffect::new);
	public static final RegistryObject<Potion> HEAT_IMMUNITY_POTION = POTIONS.register("heat_immunity", () -> new Potion("heat_immunity", new MobEffectInstance(HEAT_IMMUNITY.get(), 1800, 0, false, true, true)));
	public static final RegistryObject<Potion> HEAT_IMMUNITY_POTION_LONG = POTIONS.register("heat_resistance_long", () -> new Potion("heat_immunity_long", new MobEffectInstance(HEAT_IMMUNITY.get(), 2400, 0, false, true, true)));
	public static final RegistryObject<Potion> COLD_IMMUNITY_POTION = POTIONS.register("cold_immunity", () -> new Potion("cold_immunity", new MobEffectInstance(COLD_IMMUNITY.get(), 1800, 0, false, true, true)));
	public static final RegistryObject<Potion> COLD_IMMUNITY_POTION_LONG = POTIONS.register("cold_immunity_long", () -> new Potion("cold_immunity_long", new MobEffectInstance(COLD_IMMUNITY.get(), 2400, 0, false, true, true)));
	public static final RegistryObject<Potion> TEMPERATURE_IMMUNITY_POTION = POTIONS.register("temperature_immunity", () -> new Potion("temperature_immunity", new MobEffectInstance(TEMPERATURE_IMMUNITY.get(), 1800, 0, false, true, true)));
	public static final RegistryObject<Potion> TEMPERATURE_IMMUNITY_POTION_LONG = POTIONS.register("temperature_immunity_long", () -> new Potion("temperature_immunity_long", new MobEffectInstance(TEMPERATURE_IMMUNITY.get(), 2400, 0, false, true, true)));

	public static final RegistryObject<MobEffect> HOT_FOOD = EFFECTS.register("hot_food", () -> new SimpleAttributeEffect(MobEffectCategory.BENEFICIAL, 16714764, 1).addAttributeModifier(AttributeRegistry.HEATING_TEMPERATURE.get(), SimpleAttributeEffect.HOT_FOOD_ATTRIBUTE_UUID, 1.0, AttributeModifier.Operation.ADDITION));
	public static final RegistryObject<MobEffect> HOT_DRINk = EFFECTS.register("hot_drink", () -> new SimpleAttributeEffect(MobEffectCategory.BENEFICIAL, 16714764, 1).addAttributeModifier(AttributeRegistry.HEATING_TEMPERATURE.get(), SimpleAttributeEffect.HOT_DRINK_ATTRIBUTE_UUID, 1.0, AttributeModifier.Operation.ADDITION));
	public static final RegistryObject<MobEffect> COLD_FOOD = EFFECTS.register("cold_food", () -> new SimpleAttributeEffect(MobEffectCategory.BENEFICIAL, 1166574, 1).addAttributeModifier(AttributeRegistry.COOLING_TEMPERATURE.get(), SimpleAttributeEffect.COLD_FOOD_ATTRIBUTE_UUID, 1.0, AttributeModifier.Operation.ADDITION));
	public static final RegistryObject<MobEffect> COLD_DRINK = EFFECTS.register("cold_drink", () -> new SimpleAttributeEffect(MobEffectCategory.BENEFICIAL, 1166574, 1).addAttributeModifier(AttributeRegistry.COOLING_TEMPERATURE.get(), SimpleAttributeEffect.COLD_DRINK_ATTRIBUTE_UUID, 1.0, AttributeModifier.Operation.ADDITION));

	public static final RegistryObject<MobEffect> PAINKILLER = EFFECTS.register("painkiller", PainKillerEffect::new);

	public static final RegistryObject<MobEffect> HARD_FALLING = EFFECTS.register("hard_falling", HardFallingEffect::new);
	public static final RegistryObject<MobEffect> VULNERABILITY = EFFECTS.register("vulnerability", VulnerabilityEffect::new);
	public static final RegistryObject<MobEffect> HEADACHE = EFFECTS.register("headache", HeadacheEffect::new);

	public static void registerBrewingRecipes()
	{
		addBrewingRecipe(Potions.AWKWARD, ItemRegistry.SUN_FERN.get(), HEAT_IMMUNITY_POTION.get());
		addBrewingRecipe(HEAT_IMMUNITY_POTION.get(), Items.REDSTONE, HEAT_IMMUNITY_POTION_LONG.get());

		addBrewingRecipe(Potions.AWKWARD, ItemRegistry.ICE_FERN.get(), COLD_IMMUNITY_POTION.get());
		addBrewingRecipe(COLD_IMMUNITY_POTION.get(), Items.REDSTONE, COLD_IMMUNITY_POTION_LONG.get());
	}

	private static void addBrewingRecipe(Potion potionInput, Item ingredient, Potion potionResult)
	{
		BrewingRecipeRegistry.addRecipe(StrictNBTIngredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), potionInput)), StrictNBTIngredient.of(new ItemStack(ingredient)), PotionUtils.setPotion(new ItemStack(Items.POTION), potionResult));
	}
	
	public static void register (IEventBus eventBus){
		EFFECTS.register(eventBus);
		POTIONS.register(eventBus);
	}
}
