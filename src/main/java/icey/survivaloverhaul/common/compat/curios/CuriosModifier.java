package icey.survivaloverhaul.common.compat.curios;

import java.util.List;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.api.config.json.temperature.JsonArmorIdentity;
import icey.survivaloverhaul.api.temperature.ModifierBase;
import icey.survivaloverhaul.api.temperature.TemperatureUtil;
import icey.survivaloverhaul.config.json.JsonConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.util.ICuriosHelper;

public class CuriosModifier extends ModifierBase
{
	private ICuriosHelper helper;
	
	public CuriosModifier()
	{
		super();
		this.setRegistryName(Main.MOD_ID, "compat/curios");
	}
	
	@Override
	public float getPlayerInfluence(PlayerEntity player)
	{
		if (!Main.curiosLoaded)
			return 0.0f;
		
		try
		{
			return getUncaughtPlayerInfluence(player);
		}
		catch (Exception e)
		{
			Main.LOGGER.error("An error has occured with Curios compatability, disabling modifier", e);
			Main.curiosLoaded = false;
			
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
		
		if (lazyOptional.isPresent())
		{
			IItemHandler itemHandler = lazyOptional.resolve().get();
			
			float sum = 0.0f;
			
			for (int i = 0; i < itemHandler.getSlots(); i++)
			{
				ItemStack stack = itemHandler.getStackInSlot(i);
				
				if (!stack.isEmpty())
				{
					sum += processStackJson(stack);
					sum += TemperatureUtil.getArmorTemperatureTag(stack);
				}
			}
			
			return sum;
		}
		else
			return 0.0f;
	}
	
	private float processStackJson(ItemStack stack)
	{
		List<JsonArmorIdentity> identity = JsonConfig.armorTemperatures.get(stack.getItem().getRegistryName().toString());
		
		if (identity != null)
		{
			for (JsonArmorIdentity jai : identity)
			{
				if (jai == null)
						continue;
				
				if (jai.matches(stack))
				{
					return jai.temperature;
				}
			}
		}
		
		return 0.0f;
	}
}
