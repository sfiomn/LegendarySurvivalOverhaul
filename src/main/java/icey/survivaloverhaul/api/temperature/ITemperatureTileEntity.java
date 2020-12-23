package icey.survivaloverhaul.api.temperature;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@SuppressWarnings("unused")
public interface ITemperatureTileEntity
{
	public float getInfluence(BlockPos targetPos, double distance);
}
