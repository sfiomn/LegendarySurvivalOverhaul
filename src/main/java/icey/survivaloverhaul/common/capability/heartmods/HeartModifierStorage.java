package icey.survivaloverhaul.common.capability.heartmods;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class HeartModifierStorage implements IStorage<HeartModifierCapability>
{
	@Override
	public INBT writeNBT(Capability<HeartModifierCapability> capability, HeartModifierCapability instance, Direction side)
	{
		return instance.save();
	}
	
	@Override
	public void readNBT(Capability<HeartModifierCapability> capability, HeartModifierCapability instance, Direction side, INBT nbt)
	{
		if (nbt instanceof CompoundNBT)
		{
			instance.load((CompoundNBT) nbt);
		}
	}

}
