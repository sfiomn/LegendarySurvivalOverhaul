package icey.survivaloverhaul.common.temperature;

import java.util.List;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.api.config.json.temperature.JsonArmorIdentity;
import icey.survivaloverhaul.api.temperature.ModifierBase;
import icey.survivaloverhaul.api.temperature.TemperatureEnum;
import icey.survivaloverhaul.api.temperature.TemperatureUtil;
import icey.survivaloverhaul.common.enchantments.InsulationMagic;
import icey.survivaloverhaul.config.Config;
import icey.survivaloverhaul.config.json.JsonConfig;
import icey.survivaloverhaul.registry.EnchantRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public class ArmorModifier extends ModifierBase
{
	public ArmorModifier()
	{
		super();
		this.setRegistryName(Main.MOD_ID, "armor");
	}
	
	@Override
	public float getPlayerInfluence(PlayerEntity player)
	{
		float value = 0.0f;
		// ideally want to get players temperature / don't know if this classified as frequently if it is will use world.getTemp - birb
		int temp = TemperatureUtil.getWorldTemperature(player.world, player.getPosition());
		value += checkArmorSlot(player.getItemStackFromSlot(EquipmentSlotType.HEAD), temp);
		value += checkArmorSlot(player.getItemStackFromSlot(EquipmentSlotType.CHEST), temp);
		value += checkArmorSlot(player.getItemStackFromSlot(EquipmentSlotType.LEGS), temp);
		value += checkArmorSlot(player.getItemStackFromSlot(EquipmentSlotType.FEET), temp);
		return value;
	}
	
	private float checkArmorSlot(ItemStack stack, int temp)
	{
		if (stack.isEmpty())
				return 0.0f;
		
		float sum = 0.0f;
		int adaptiveLevel = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.ModEnchants.ADAPTIVE_BARRIER, stack);
		int coolingLevel = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.ModEnchants.COLD_BARRIER, stack);
		int heatingLevel = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.ModEnchants.THERMAL_BARRIER, stack);
		if (adaptiveLevel > 0) 
		{
			if (temp <= TemperatureEnum.FROSTBITE.getUpperBound()) 
			{
				sum += InsulationMagic.CalcLevelEffect(adaptiveLevel);
			}
			else if (temp >= TemperatureEnum.HEAT_STROKE.getLowerBound())
			{
				sum -= InsulationMagic.CalcLevelEffect(adaptiveLevel);
			}
		}
		else
			if (coolingLevel > 0 && temp <= TemperatureEnum.FROSTBITE.getUpperBound())
			sum -= InsulationMagic.CalcLevelEffect(coolingLevel);
		else if (heatingLevel > 0 && temp >= TemperatureEnum.HEAT_STROKE.getLowerBound())
			sum += InsulationMagic.CalcLevelEffect(heatingLevel);
		sum += processStackJson(stack);
		sum += TemperatureUtil.getArmorTemperatureTag(stack);
		return sum;
	}
	
	private float processStackJson(ItemStack stack)
	{
		List<JsonArmorIdentity> identity = JsonConfig.armorTemperatures.get(stack.getItem().getRegistryName().toString());
		
		if (identity != null)
		{
			for (JsonArmorIdentity jtm : identity)
			{
				if (jtm == null)
						continue;
				
				if (jtm.matches(stack))
				{
					return jtm.temperature;
				}
			}
		}
		
		return 0.0f;
	}
}
