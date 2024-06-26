package sfiomn.legendarysurvivaloverhaul.api.temperature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class DynamicModifierBase extends Registry<DynamicModifierBase>
{
	protected final float middleTemperature;
	
	public DynamicModifierBase()
	{
		this.middleTemperature = (TemperatureEnum.NORMAL.getUpperBound() + (float) TemperatureEnum.COLD.getUpperBound()) / 2;
	}
	
	public float applyDynamicPlayerInfluence(Player player, float currentTemperature)
	{
		return 0.0f;
	}
	
	public float applyDynamicWorldInfluence(Level world, BlockPos pos, float currentTemperature)
	{
		return 0.0f;
	}
	
	@Override
	public String toString()
	{
		return this.getRegistryName().toString();
	}
}
