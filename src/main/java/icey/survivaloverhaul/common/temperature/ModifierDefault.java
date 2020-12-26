package icey.survivaloverhaul.common.temperature;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ModifierDefault extends ModifierBase
{
	public ModifierDefault()
	{
		super("Default");
	}
	
	@Override
	public float getWorldInfluence(World world, BlockPos pos)
	{
		return defaultTemperature;
	}
}
