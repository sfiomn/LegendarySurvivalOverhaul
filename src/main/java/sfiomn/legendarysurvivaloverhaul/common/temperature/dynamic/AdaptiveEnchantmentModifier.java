package sfiomn.legendarysurvivaloverhaul.common.temperature.dynamic;

import sfiomn.legendarysurvivaloverhaul.api.temperature.DynamicModifierBase;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.EnchantRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public class AdaptiveEnchantmentModifier extends DynamicModifierBase
{
	public AdaptiveEnchantmentModifier()
	{
		super();
	}
	
	@Override
	public float applyDynamicPlayerInfluence(PlayerEntity player, float currentTemperature)
	{
		float value = 0.0f;
		
		value += checkArmorSlot(player.getItemBySlot(EquipmentSlotType.HEAD), currentTemperature);
		value += checkArmorSlot(player.getItemBySlot(EquipmentSlotType.CHEST), currentTemperature);
		value += checkArmorSlot(player.getItemBySlot(EquipmentSlotType.LEGS), currentTemperature);
		value += checkArmorSlot(player.getItemBySlot(EquipmentSlotType.FEET), currentTemperature);
		
		return value;
	}
	
	private float checkArmorSlot(ItemStack stack, float currentTemperature)
	{
		if (stack.isEmpty())
				return 0.0f;
		
		float sum = 0.0f;
		
		int adaptiveLevel = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.ADAPTIVE_BARRIER.get(), stack);
		
		if (adaptiveLevel > 0) 
		{
			int diff = (int) (currentTemperature - middleTemperature);
			if (currentTemperature > middleTemperature)
			{
				sum -= Math.min(adaptiveLevel * Config.Baked.enchantmentMultiplier, Math.abs(diff));
			}
			else if (currentTemperature < middleTemperature)
			{
				sum += Math.min(adaptiveLevel * Config.Baked.enchantmentMultiplier, Math.abs(diff));
			}
		}
		
		return sum;
	}
}
