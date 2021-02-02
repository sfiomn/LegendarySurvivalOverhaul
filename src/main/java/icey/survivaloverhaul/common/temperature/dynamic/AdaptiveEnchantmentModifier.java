package icey.survivaloverhaul.common.temperature.dynamic;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.api.temperature.DynamicModifierBase;
import icey.survivaloverhaul.api.temperature.TemperatureEnum;
import icey.survivaloverhaul.common.enchantments.InsulationMagic;
import icey.survivaloverhaul.registry.EnchantRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public class AdaptiveEnchantmentModifier extends DynamicModifierBase
{
	public AdaptiveEnchantmentModifier()
	{
		super();
		this.setRegistryName(Main.MOD_ID, "adaptive_enchantment");
	}
	
	@Override
	public float applyDynamicPlayerInfluence(PlayerEntity player, float currentTemperature)
	{
		float value = 0.0f;
		
		value += checkArmorSlot(player.getItemStackFromSlot(EquipmentSlotType.HEAD), currentTemperature);
		value += checkArmorSlot(player.getItemStackFromSlot(EquipmentSlotType.CHEST), currentTemperature);
		value += checkArmorSlot(player.getItemStackFromSlot(EquipmentSlotType.LEGS), currentTemperature);
		value += checkArmorSlot(player.getItemStackFromSlot(EquipmentSlotType.FEET), currentTemperature);
		
		return value;
	}
	
	private float checkArmorSlot(ItemStack stack, float temp)
	{
		if (stack.isEmpty())
				return 0.0f;
		
		float sum = 0.0f;
		
		int adaptiveLevel = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.ModEnchants.ADAPTIVE_BARRIER, stack);
		if (adaptiveLevel > 0) 
		{
			if (temp <= TemperatureEnum.FROSTBITE.getUpperBound()) 
			{
				sum += InsulationMagic.calcLevelEffect(adaptiveLevel);
			}
			else if (temp >= TemperatureEnum.HEAT_STROKE.getLowerBound())
			{
				sum -= InsulationMagic.calcLevelEffect(adaptiveLevel);
			}
		}
		
		return sum;
	}
}
