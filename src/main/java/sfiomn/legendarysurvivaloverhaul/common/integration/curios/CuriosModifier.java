package sfiomn.legendarysurvivaloverhaul.common.integration.curios;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonTemperature;
import sfiomn.legendarysurvivaloverhaul.api.item.CoatEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.util.ICuriosHelper;

import java.util.Objects;

public class CuriosModifier extends ModifierBase
{
	private ICuriosHelper helper;
	
	public CuriosModifier()
	{
		super();
	}
	
	@Override
	public float getPlayerInfluence(PlayerEntity player)
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
	
	public float getUncaughtPlayerInfluence(PlayerEntity player)
	{
		if (helper == null)
		{
			helper = CuriosApi.getCuriosHelper();
			return 0.0f;
		}
		
		LazyOptional<IItemHandlerModifiable> lazyOptional = helper.getEquippedCurios(player);
		
		if (lazyOptional.isPresent() && lazyOptional.resolve().isPresent())
		{
			IItemHandler itemHandler = lazyOptional.resolve().get();
			
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
					if (Objects.equals(coat.type(), "cooling")) {
						sum -= (float) coat.modifier();
					} else if (Objects.equals(coat.type(), "heating")) {
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
		ResourceLocation itemRegistryName = stack.getItem().getRegistryName();
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
