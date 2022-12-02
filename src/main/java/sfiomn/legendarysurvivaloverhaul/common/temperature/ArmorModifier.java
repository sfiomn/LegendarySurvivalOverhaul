package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonArmorIdentity;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;

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

		sum += processStackJson(stack);
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
