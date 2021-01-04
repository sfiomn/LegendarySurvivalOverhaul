package icey.survivaloverhaul.api.temperature;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ITemperatureModifier
{
	public float getPlayerInfluence(PlayerEntity player);
	
	public float getWorldInfluence(World world, BlockPos pos);
}
