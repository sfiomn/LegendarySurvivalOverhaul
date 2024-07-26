package sfiomn.legendarysurvivaloverhaul.common.integration.curios;

import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonTemperature;
import sfiomn.legendarysurvivaloverhaul.api.item.CoatEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Objects;

public class CuriosModifier extends ModifierBase
{
	public CuriosModifier()
	{
		super();
	}
	
	@Override
	public float getPlayerInfluence(Player player)
	{
		if (!LegendarySurvivalOverhaul.curiosLoaded)
			return 0.0f;
		
		try
		{
			return getUncaughtPlayerInfluence(player);
		}
		catch (Exception e)
		{
			LegendarySurvivalOverhaul.LOGGER.error("An error has occured with Curios compatability, disabling modifier", e);
			LegendarySurvivalOverhaul.curiosLoaded = false;
			
			return 0.0f;
		}
	}
	
	public float getUncaughtPlayerInfluence(Player player)
	{
		LazyOptional<ICuriosItemHandler> lazyOptional = CuriosApi.getCuriosInventory(player);
		
		if (lazyOptional.isPresent() && lazyOptional.resolve().isPresent())
		{
			IItemHandlerModifiable itemHandler = lazyOptional.resolve().get().getEquippedCurios();
			
			float sum = 0.0f;
			
			for (int i = 0; i < itemHandler.getSlots(); i++)
			{
				ItemStack stack = itemHandler.getStackInSlot(i);
				
				if (!stack.isEmpty())
				{
					sum += processStackJson(stack);
					String coatId = TemperatureUtil.getArmorCoatTag(stack);
					CoatEnum coat = CoatEnum.getFromId(coatId);
					if (coat == null)
						continue;
					if (coat.type().equals("cooling")) {
						sum -= (float) coat.modifier();
					} else if (coat.type().equals("heating")) {
						sum += (float) coat.modifier();
					}
				}
			}
			
			return sum;
		}
		else
			return 0.0f;
	}
	
	private float processStackJson(ItemStack stack)
	{
		ResourceLocation itemRegistryName = ForgeRegistries.ITEMS.getKey(stack.getItem());
		JsonTemperature jsonTemperature = null;
		if (itemRegistryName != null)
			jsonTemperature = JsonConfig.itemTemperatures.get(itemRegistryName.toString());
		
		if (jsonTemperature != null)
		{
			return jsonTemperature.temperature;
		}
		
		return 0.0f;
	}
}
