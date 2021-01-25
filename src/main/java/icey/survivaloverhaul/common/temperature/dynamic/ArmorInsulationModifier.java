package icey.survivaloverhaul.common.temperature.dynamic;

import java.util.List;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.api.config.json.temperature.JsonArmorIdentity;
import icey.survivaloverhaul.api.temperature.DynamicModifierBase;
import icey.survivaloverhaul.api.temperature.TemperatureUtil;
import icey.survivaloverhaul.config.json.JsonConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ArmorInsulationModifier extends DynamicModifierBase
{
	public ArmorInsulationModifier()
	{
		super();
		this.setRegistryName(Main.MOD_ID, "armor_insulation");
	}
	
	@Override
	public float applyDynamicPlayerInfluence(PlayerEntity player, float currentTemperature)
	{
		BlockPos pos = player.getPosition();
		World world = player.getEntityWorld();

		int worldTemperature = TemperatureUtil.getWorldTemperature(world, pos);
		
		
		return currentTemperature;
	}
	
	private float checkArmorSlot(ItemStack stack)
	{
		if (stack.isEmpty())
				return 1.0f;
		
		return getArmorInsulation(stack);
	}
	
	private float getArmorInsulation(ItemStack stack)
	{
		List<JsonArmorIdentity> armorList = JsonConfig.armorTemperatures.get(stack.getItem().getRegistryName().toString());
		
		if (armorList != null)
		{
			for (JsonArmorIdentity jtm : armorList)
			{
				if (jtm == null)
						continue;
				
				if (jtm.matches(stack))
				{
					return MathHelper.sqrt(MathHelper.sqrt(jtm.insulation));
				}
			}
		}
		
		return 1.0f;
	}
}
