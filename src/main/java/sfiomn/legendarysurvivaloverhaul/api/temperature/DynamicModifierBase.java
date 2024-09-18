package sfiomn.legendarysurvivaloverhaul.api.temperature;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class DynamicModifierBase extends ForgeRegistryEntry<DynamicModifierBase>
{
	
	public DynamicModifierBase()
	{
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
