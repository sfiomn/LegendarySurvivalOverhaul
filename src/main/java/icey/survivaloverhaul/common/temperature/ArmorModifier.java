package icey.survivaloverhaul.common.temperature;

import java.util.List;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.api.config.json.JsonTemperatureIdentity;
import icey.survivaloverhaul.api.temperature.TemperatureUtil;
import icey.survivaloverhaul.config.json.JsonConfig;
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
		
		sum += processStackJson(stack);
		sum += TemperatureUtil.getArmorTemperatureTag(stack);
		
		return sum;
	}
	
	private float processStackJson(ItemStack stack)
	{
		List<JsonTemperatureIdentity> armorList = JsonConfig.armorTemperatures.get(stack.getItem().getRegistryName().toString());
		
		if (armorList != null)
		{
			for (JsonTemperatureIdentity jtm : armorList)
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
