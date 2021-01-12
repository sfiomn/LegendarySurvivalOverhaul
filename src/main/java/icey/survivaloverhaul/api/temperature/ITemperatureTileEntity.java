package icey.survivaloverhaul.api.temperature;

import net.minecraft.util.math.BlockPos;

public interface ITemperatureTileEntity
{
	public float getInfluence(BlockPos pos, double distance);
}
