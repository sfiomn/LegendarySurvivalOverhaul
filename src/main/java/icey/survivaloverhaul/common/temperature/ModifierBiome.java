package icey.survivaloverhaul.common.temperature;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;

public class ModifierBiome extends ModifierBase
{

	public ModifierBiome()
	{
		super("Biome");
	}
	
	@Override
	public float getWorldInfluence(World world, BlockPos pos)
	{
		Vector3i[] posOffsets = 
			{
					new Vector3i(10, 0, 0),
					new Vector3i(-10, 0, 0),
					new Vector3i(0, 0, 10),
					new Vector3i(0, 0, -10),
					new Vector3i(7, 0, 7),
					new Vector3i(7, 0, -7),
					new Vector3i(-7, 0, 7),
					new Vector3i(-7, 0, -7)
			};
		
		float biomeAverage = getTempForBiome(world.getBiome(pos));
		
		for (Vector3i entry : posOffsets)
		{
			biomeAverage += getTempForBiome(world.getBiome(pos.add(entry)));
		}
		
		biomeAverage /= 9.0f;
		
		return applyUndergroundEffect(normalizeToPosNeg(biomeAverage) * 1.0f, world, pos);
	}
}
