package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffect;
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
import sfiomn.legendarysurvivaloverhaul.config.Config;

public class MobEffectRegistry {

	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, LegendarySurvivalOverhaul.MOD_ID);
	public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, LegendarySurvivalOverhaul.MOD_ID);

	public static final RegistryObject<MobEffect> THIRST = EFFECTS.register("thirst", ThirstEffect::new);
	public static final RegistryObject<Potion> THIRST_POTION = POTIONS.register("thirst", () -> new Potion("thirst", new MobEffectInstance(THIRST.get(), 3600, 0, false, true, false)));
	public static final RegistryObject<Potion> THIRST_POTION_LONG = POTIONS.register("thirst_long", () -> new Potion("thirst_long", new MobEffectInstance(THIRST.get(), 9600, 0, false, true, false)));

	public static final RegistryObject<MobEffect> FROSTBITE = EFFECTS.register("frostbite", FrostbiteEffect::new);
	public static final RegistryObject<MobEffect> COLD_HUNGER = EFFECTS.register("cold_hunger", ColdHungerEffect::new);
	public static final RegistryObject<MobEffect> HEAT_STROKE = EFFECTS.register("heat_stroke", HeatStrokeEffect::new);
	public static final RegistryObject<MobEffect> HEAT_THIRST = EFFECTS.register("heat_thirst", HeatThirstEffect::new);
	public static final RegistryObject<MobEffect> COLD_RESISTANCE = EFFECTS.register("cold_resist", ColdResistanceEffect::new);
	public static final RegistryObject<MobEffect> HEAT_RESISTANCE = EFFECTS.register("heat_resist", HeatResistanceEffect::new);
	public static final RegistryObject<Potion> HEAT_RESISTANCE_POTION = POTIONS.register("heat_resistance", () -> new Potion("heat_resistance", new MobEffectInstance(HEAT_RESISTANCE.get(), 3600, 0, false, Config.Baked.showPotionEffectParticles, true)));
	public static final RegistryObject<Potion> HEAT_RESISTANCE_POTION_LONG = POTIONS.register("heat_resistance_long", () -> new Potion("heat_resistance_long", new MobEffectInstance(HEAT_RESISTANCE.get(), 9600, 0, false, Config.Baked.showPotionEffectParticles, true)));
	public static final RegistryObject<Potion> COLD_RESISTANCE_POTION = POTIONS.register("cold_resistance", () -> new Potion("cold_resistance", new MobEffectInstance(COLD_RESISTANCE.get(), 3600, 0, false, Config.Baked.showPotionEffectParticles, true)));
	public static final RegistryObject<Potion> COLD_RESISTANCE_POTION_LONG = POTIONS.register("cold_resistance_long", () -> new Potion("cold_resistance_long", new MobEffectInstance(COLD_RESISTANCE.get(), 9600, 0, false, Config.Baked.showPotionEffectParticles, true)));

	public static final RegistryObject<MobEffect> HOT_FOOD = EFFECTS.register("hot_food", HotFoodEffect::new);
	public static final RegistryObject<MobEffect> HOT_DRINk = EFFECTS.register("hot_drink", HotDrinkEffect::new);
	public static final RegistryObject<MobEffect> COLD_FOOD = EFFECTS.register("cold_food", ColdFoodEffect::new);
	public static final RegistryObject<MobEffect> COLD_DRINK = EFFECTS.register("cold_drink", ColdDrinkEffect::new);

	public static final RegistryObject<MobEffect> PAINKILLER = EFFECTS.register("painkiller", PainKillerEffect::new);

	public static final RegistryObject<MobEffect> HARD_FALLING = EFFECTS.register("hard_falling", HardFallingEffect::new);
	public static final RegistryObject<MobEffect> VULNERABILITY = EFFECTS.register("vulnerability", VulnerabilityEffect::new);
	public static final RegistryObject<MobEffect> HEADACHE = EFFECTS.register("headache", HeadacheEffect::new);

	public static void registerBrewingRecipes()
	{
		addBrewingRecipe(Potions.AWKWARD, ItemRegistry.SUN_FERN.get(), HEAT_RESISTANCE_POTION.get());
		addBrewingRecipe(HEAT_RESISTANCE_POTION.get(), Items.REDSTONE, HEAT_RESISTANCE_POTION_LONG.get());

		addBrewingRecipe(Potions.AWKWARD, ItemRegistry.ICE_FERN.get(), COLD_RESISTANCE_POTION.get());
		addBrewingRecipe(COLD_RESISTANCE_POTION.get(), Items.REDSTONE, COLD_RESISTANCE_POTION_LONG.get());
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
