package icey.survivaloverhaul.common.temperature;

import icey.survivaloverhaul.api.temperature.ITemperatureDynamicModifier;
import icey.survivaloverhaul.api.temperature.TemperatureEnum;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ModifierDynamicBase implements ITemperatureDynamicModifier
{
	private final String name;
	protected final float defaultTemperature;
	
	public ModifierDynamicBase(String name)
	{
		this.name = name;
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

	@Override
	public String getName()
	{
		return name;
	}

}
