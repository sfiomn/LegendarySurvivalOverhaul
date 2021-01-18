package icey.survivaloverhaul.common.blocks.tileentity;

import icey.survivaloverhaul.api.temperature.ITemperatureTileEntity;
import icey.survivaloverhaul.common.blocks.BlockTemperatureCoil;
import icey.survivaloverhaul.config.Config;
import icey.survivaloverhaul.registry.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

public class CoilTileEntity extends TileEntity implements ITemperatureTileEntity
{
	public CoilTileEntity()
	{
		super(null); // BlockRegistry.ModTileEntities.TEMP_COIL
	}
	
	@Override
	public float getInfluence(BlockPos pos, double distance)
	{
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		
		if(block instanceof BlockTemperatureCoil)
		{
			if (world.getBlockState(pos).get(BlockTemperatureCoil.POWERED))
			{
				float activeTemp = ((BlockTemperatureCoil) block).coilType.getTemperatureMultiplier();
				
				double fullPowerSq = sq(Config.BakedConfigValues.coilFullPowerDistance);
				
				if (distance < fullPowerSq)
				{
					return activeTemp;
				}
				else
				{
					double distanceDiv = sq(Config.BakedConfigValues.maxCoilInfluenceDistance) - fullPowerSq;
					
					if (distanceDiv <= 0d) 
						return 0f;
					
					return activeTemp * Math.max(0.0f, 1.0f - (float)((distance - fullPowerSq) / distanceDiv));
				}
			}
		}
		
		return 0f;
	}
	
	private double sq(double d)
	{
		return d * d;
	}
}
