package sfiomn.legendarysurvivaloverhaul.registry;

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
import sfiomn.legendarysurvivaloverhaul.common.effects.*;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public class EffectRegistry
{
	public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, LegendarySurvivalOverhaul.MOD_ID);
	public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTION_TYPES, LegendarySurvivalOverhaul.MOD_ID);

	public static final RegistryObject<Effect> THIRST = EFFECTS.register("thirst", ThirstEffect::new);
	public static final RegistryObject<Potion> THIRST_POTION = POTIONS.register("thirst", () -> new Potion("thirst", new EffectInstance(THIRST.get(), 3600, 0, false, true, false)));
	public static final RegistryObject<Potion> THIRST_POTION_LONG = POTIONS.register("thirst_long", () -> new Potion("thirst_long", new EffectInstance(THIRST.get(), 9600, 0, false, true, false)));

	public static final RegistryObject<Effect> FROSTBITE = EFFECTS.register("frostbite", FrostbiteEffect::new);
	public static final RegistryObject<Effect> COLD_HUNGER = EFFECTS.register("cold_hunger", ColdHungerEffect::new);
	public static final RegistryObject<Effect> HEAT_STROKE = EFFECTS.register("heat_stroke", HeatStrokeEffect::new);
	public static final RegistryObject<Effect> HEAT_THIRST = EFFECTS.register("heat_thirst", HeatThirstEffect::new);
	public static final RegistryObject<Effect> COLD_RESISTANCE = EFFECTS.register("cold_resist", ColdResistanceEffect::new);
	public static final RegistryObject<Effect> HEAT_RESISTANCE = EFFECTS.register("heat_resist", HeatResistanceEffect::new);
	public static final RegistryObject<Potion> HEAT_RESISTANCE_POTION = POTIONS.register("heat_resistance", () -> new Potion("heat_resistance", new EffectInstance(HEAT_RESISTANCE.get(), 3600, 0, false, Config.Baked.showPotionEffectParticles, true)));
	public static final RegistryObject<Potion> HEAT_RESISTANCE_POTION_LONG = POTIONS.register("heat_resistance_long", () -> new Potion("heat_resistance_long", new EffectInstance(HEAT_RESISTANCE.get(), 9600, 0, false, Config.Baked.showPotionEffectParticles, true)));
	public static final RegistryObject<Potion> COLD_RESISTANCE_POTION = POTIONS.register("cold_resistance", () -> new Potion("cold_resistance", new EffectInstance(COLD_RESISTANCE.get(), 3600, 0, false, Config.Baked.showPotionEffectParticles, true)));
	public static final RegistryObject<Potion> COLD_RESISTANCE_POTION_LONG = POTIONS.register("cold_resistance_long", () -> new Potion("cold_resistance_long", new EffectInstance(COLD_RESISTANCE.get(), 9600, 0, false, Config.Baked.showPotionEffectParticles, true)));

	public static final RegistryObject<Effect> HOT_FOOD = EFFECTS.register("hot_food", HotFoodEffect::new);
	public static final RegistryObject<Effect> HOT_DRINk = EFFECTS.register("hot_drink", HotDrinkEffect::new);
	public static final RegistryObject<Effect> COLD_FOOD = EFFECTS.register("cold_food", ColdFoodEffect::new);
	public static final RegistryObject<Effect> COLD_DRINK = EFFECTS.register("cold_drink", ColdDrinkEffect::new);

	public static final RegistryObject<Effect> PAINKILLER = EFFECTS.register("painkiller", PainKillerEffect::new);

	public static final RegistryObject<Effect> HARD_FALLING = EFFECTS.register("hard_falling", HardFallingEffect::new);
	public static final RegistryObject<Effect> VULNERABILITY = EFFECTS.register("vulnerability", VulnerabilityEffect::new);
	public static final RegistryObject<Effect> HEADACHE = EFFECTS.register("headache", HeadacheEffect::new);

	public static void registerBrewingRecipes()
	{
		addBrewingRecipe(Potions.AWKWARD, ItemRegistry.SUN_FERN.get(), HEAT_RESISTANCE_POTION.get());
		addBrewingRecipe(Potions.AWKWARD, ItemRegistry.ICE_FERN.get(), COLD_RESISTANCE_POTION.get());
		addBrewingRecipe(HEAT_RESISTANCE_POTION.get(), Items.REDSTONE, HEAT_RESISTANCE_POTION_LONG.get());
		addBrewingRecipe(COLD_RESISTANCE_POTION.get(), Items.REDSTONE, COLD_RESISTANCE_POTION_LONG.get());
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
