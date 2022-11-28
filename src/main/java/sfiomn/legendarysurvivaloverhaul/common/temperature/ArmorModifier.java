package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonArmorIdentity;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;
import sfiomn.legendarysurvivaloverhaul.registry.EnchantRegistry;

import java.util.List;

public class ArmorModifier extends ModifierBase
{
	public ArmorModifier()
	{
		super();
	}
	
	@Override
	public float getPlayerInfluence(PlayerEntity player)
	{
		float value = 0.0f;
		
		value += checkArmorSlot(player.getItemBySlot(EquipmentSlotType.HEAD));
		value += checkArmorSlot(player.getItemBySlot(EquipmentSlotType.CHEST));
		value += checkArmorSlot(player.getItemBySlot(EquipmentSlotType.LEGS));
		value += checkArmorSlot(player.getItemBySlot(EquipmentSlotType.FEET));
		
		return value;
	}
	
	private float checkArmorSlot(ItemStack stack)
	{
		if (stack.isEmpty())
				return 0.0f;
		
		float sum = 0.0f;
		int coolingLevel = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.COLD_BARRIER.get(), stack);
		int heatingLevel = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.THERMAL_BARRIER.get(), stack);
		
		if (coolingLevel > 0 )
			sum -= coolingLevel * Config.Baked.enchantmentMultiplier;
		if (heatingLevel > 0)
			sum += heatingLevel * Config.Baked.enchantmentMultiplier;
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
