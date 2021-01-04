package icey.survivaloverhaul.common.temperature;

import icey.survivaloverhaul.Main;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ModifierDefault extends ModifierBase
{
	public ModifierDefault()
	{
		super();
		this.setRegistryName(Main.MOD_ID, "default");
	}
	
	@Override
	public float getWorldInfluence(World world, BlockPos pos)
	{
		return defaultTemperature;
	}
}
