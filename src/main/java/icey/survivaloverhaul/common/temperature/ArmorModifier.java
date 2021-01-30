package icey.survivaloverhaul.common.temperature;

import java.util.List;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.api.config.json.temperature.JsonArmorIdentity;
import icey.survivaloverhaul.api.temperature.ModifierBase;
import icey.survivaloverhaul.api.temperature.TemperatureUtil;
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
		value += checkArmorSlot(player.getItemStackFromSlot(EquipmentSlotType.HEAD));
		value += checkArmorSlot(player.getItemStackFromSlot(EquipmentSlotType.CHEST));
		value += checkArmorSlot(player.getItemStackFromSlot(EquipmentSlotType.LEGS));
		value += checkArmorSlot(player.getItemStackFromSlot(EquipmentSlotType.FEET));
		return value;
	}
	
	private float checkArmorSlot(ItemStack stack)
	{
		if (stack.isEmpty())
				return 0.0f;
		
		float sum = 0.0f;
		
		int coolingLevel = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.ModEnchants.COLD_BARRIER, stack);
		int heatingLevel = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.ModEnchants.THERMAL_BARRIER, stack);
		
		if (coolingLevel > 0)
			sum -= coolingLevel * Config.Baked.enchantmentMultiplier;
		else if (heatingLevel > 0)
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
