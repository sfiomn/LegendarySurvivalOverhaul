package icey.survivaloverhaul.common.temperature;

import icey.survivaloverhaul.api.temperature.ITemperatureDynamicModifier;
import icey.survivaloverhaul.api.temperature.TemperatureStateEnum;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class ModifierDynamicBase extends ForgeRegistryEntry<ModifierDynamicBase> implements ITemperatureDynamicModifier
{
	protected final float defaultTemperature;
	
	public ModifierDynamicBase()
	{
		this.defaultTemperature = (TemperatureStateEnum.NORMAL.getUpperBound() + TemperatureStateEnum.COLD.getUpperBound()) / 2;
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
