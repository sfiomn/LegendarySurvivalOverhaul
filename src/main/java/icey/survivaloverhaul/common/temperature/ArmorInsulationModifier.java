package icey.survivaloverhaul.common.temperature;

import java.util.List;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.api.config.json.temperature.JsonArmorIdentity;
import icey.survivaloverhaul.api.temperature.ModifierBase;
import icey.survivaloverhaul.api.temperature.TemperatureEnum;
import icey.survivaloverhaul.api.temperature.TemperatureUtil;
import icey.survivaloverhaul.config.json.JsonConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public class ArmorInsulationModifier extends ModifierBase
{
	public ArmorInsulationModifier()
	{
		super();
		this.setRegistryName(Main.MOD_ID, "armor_insulation");
	}
	
	@Override
	public float getPlayerInfluence(PlayerEntity player)
	{
		int worldTemperature = TemperatureUtil.getWorldTemperature(player.world, player.getPosition());
		int diff = TemperatureEnum.NORMAL.getMiddle() - worldTemperature;
		
		diff *= checkArmorSlot(player.getItemStackFromSlot(EquipmentSlotType.HEAD));
		diff *= checkArmorSlot(player.getItemStackFromSlot(EquipmentSlotType.CHEST));
		diff *= checkArmorSlot(player.getItemStackFromSlot(EquipmentSlotType.LEGS));
		diff *= checkArmorSlot(player.getItemStackFromSlot(EquipmentSlotType.FEET));
		
		return -diff;
	}
	
	private float checkArmorSlot(ItemStack stack)
	{
		if (stack.isEmpty())
				return 1.0f;
		
		return (float) Math.sqrt(Math.sqrt(processStackJson(stack)));
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
					return jtm.insulation;
				}
			}
		}
		
		return 0.0f;
	}
}
