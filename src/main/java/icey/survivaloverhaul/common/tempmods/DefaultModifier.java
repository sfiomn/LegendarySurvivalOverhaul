package icey.survivaloverhaul.common.tempmods;

import icey.survivaloverhaul.Main;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DefaultModifier extends ModifierBase
{
	public DefaultModifier()
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
