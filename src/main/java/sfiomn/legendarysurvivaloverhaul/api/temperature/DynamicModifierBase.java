package sfiomn.legendarysurvivaloverhaul.api.temperature;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class DynamicModifierBase extends ForgeRegistryEntry<DynamicModifierBase>
{
	protected final float defaultTemperature;
	
	public DynamicModifierBase()
	{
		this.defaultTemperature = (TemperatureEnum.NORMAL.getUpperBound() + (float) TemperatureEnum.COLD.getUpperBound()) / 2;
	}
	
	public float applyDynamicPlayerInfluence(PlayerEntity player, float currentTemperature)
	{
		return 0.0f;
	}
	
	public float applyDynamicWorldInfluence(World world, BlockPos pos, float currentTemperature)
	{
		return 0.0f;
	}
	
	@Override
	public String toString()
	{
		return this.getRegistryName().toString();
	}
}
