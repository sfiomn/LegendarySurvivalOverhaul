package icey.survivaloverhaul.api.temperature;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class DynamicModifierBase extends ForgeRegistryEntry<DynamicModifierBase> implements ITemperatureDynamicModifier
{
	protected final float defaultTemperature;
	
	public DynamicModifierBase()
	{
		this.defaultTemperature = (TemperatureEnum.NORMAL.getUpperBound() + TemperatureEnum.COLD.getUpperBound()) / 2;
	}
	
	@Override
	public float applyDynamicPlayerInfluence(PlayerEntity player, float currentTemperature)
	{
		return currentTemperature;
	}

	@Override
	public float applyDynamicWorldInfluence(World world, BlockPos pos, float currentTemperature)
	{
		return currentTemperature;
	}

}
