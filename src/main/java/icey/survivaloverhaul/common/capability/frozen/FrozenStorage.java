package icey.survivaloverhaul.common.capability.frozen;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class FrozenStorage implements IStorage<FrozenCapability>
{
	@Override
	public INBT writeNBT(Capability<FrozenCapability> capability, FrozenCapability instance, Direction side)
	{
		return instance.save();
	}
	
	@Override
	public void readNBT(Capability<FrozenCapability> capability, FrozenCapability instance, Direction side, INBT nbt)
	{
		if (nbt instanceof CompoundNBT)
		{
			instance.load((CompoundNBT) nbt);
		}
	}
}
