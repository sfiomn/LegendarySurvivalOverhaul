package icey.survivaloverhaul.registry;

import java.lang.reflect.Field;

import javax.annotation.Nonnull;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.common.effects.*;
import icey.survivaloverhaul.util.ProperBrewingRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EffectRegistry
{
	public static final class ModEffects
	{
		public static final Effect FROSTBITE = new FrostbiteEffect();
		public static final Effect HEAT_STROKE = new HeatStrokeEffect();
		public static final Effect COLD_RESISTANCE = new ColdResistanceEffect();
		public static final Effect HEAT_RESISTANCE = new HeatResistanceEffect();
		
		public static final Effect THIRST = new ThirstEffect();
	}
	
	@SubscribeEvent
	public static void registerEffects(RegistryEvent.Register<Effect> event)
	{
		try
		{
			for (Field f : EffectRegistry.ModEffects.class.getDeclaredFields()) 
			{
				Object obj = f.get(null);
				if (obj instanceof Effect) 
				{
					event.getRegistry().register((Effect) obj);
				} 
				else if (obj instanceof Effect[]) 
				{
					for (Effect effect : (Effect[]) obj) 
					{
						event.getRegistry().register(effect);
					}
				}
			}
		} 
		catch (IllegalAccessException e) 
		{
			throw new RuntimeException(e);
		}
	}
	
	public static final class ModPotions
	{
		public static final Potion HEAT_RESISTANCE_POTION = new Potion(new EffectInstance(ModEffects.HEAT_RESISTANCE, 3600, 0)).setRegistryName(Main.MOD_ID, "heat_resistance");
		public static final Potion HEAT_RESISTANCE_POTION_LONG = new Potion(new EffectInstance(ModEffects.HEAT_RESISTANCE, 9600, 0)).setRegistryName(Main.MOD_ID, "heat_resistance_long");
		public static final Potion COLD_RESISTANCE_POTION = new Potion(new EffectInstance(ModEffects.COLD_RESISTANCE, 3600, 0)).setRegistryName(Main.MOD_ID, "cold_resistance");
		public static final Potion COLD_RESISTANCE_POTION_LONG = new Potion(new EffectInstance(ModEffects.COLD_RESISTANCE, 9600, 0)).setRegistryName(Main.MOD_ID, "cold_resistance_long");
	}
	
	@SubscribeEvent
	public static void registerPotions(RegistryEvent.Register<Potion> event)
	{
		try
		{
			for (Field f : EffectRegistry.ModPotions.class.getDeclaredFields()) 
			{
				Object obj = f.get(null);
				if (obj instanceof Potion) {
					if(((Potion) obj).getRegistryName() != null)
					{
						event.getRegistry().register((Potion) obj);
					}
				}
				else if (obj instanceof Potion[]) 
				{
					for (Potion potion : (Potion[]) obj) 
					{
						if(potion.getRegistryName() != null)
						{
							event.getRegistry().register(potion);
						}
					}
				}
			}
		} 
		catch (IllegalAccessException e) 
		{
			throw new RuntimeException(e);
		}
		
		registerPotionRecipes();
	}
	
	public static ItemStack createPotion(Potion potion)
	{
		return PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), potion);
	}
	
	public static void registerPotionRecipes()
	{
		BrewingRecipeRegistry.addRecipe(
				new ProperBrewingRecipe(
						Ingredient.fromStacks(createPotion(Potions.AWKWARD)),
						Ingredient.fromItems(ItemRegistry.STONE_FERN_LEAF), 
						createPotion(ModPotions.HEAT_RESISTANCE_POTION)));
		BrewingRecipeRegistry.addRecipe(
				new ProperBrewingRecipe(
						Ingredient.fromStacks(createPotion(ModPotions.HEAT_RESISTANCE_POTION)),
						Ingredient.fromItems(Items.REDSTONE), 
						createPotion(ModPotions.HEAT_RESISTANCE_POTION_LONG)));

		BrewingRecipeRegistry.addRecipe(
				new ProperBrewingRecipe(
						Ingredient.fromStacks(createPotion(ModPotions.COLD_RESISTANCE_POTION)),
						Ingredient.fromItems(Items.REDSTONE), 
						createPotion(ModPotions.COLD_RESISTANCE_POTION_LONG)));
	}
	
	
}
