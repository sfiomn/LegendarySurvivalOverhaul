package sfiomn.legendarysurvivaloverhaul.common.capabilities.bodydamage;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class BodyDamageStorage implements IStorage<BodyDamageCapability>
{
	@Override
	public INBT writeNBT(Capability<BodyDamageCapability> capability, BodyDamageCapability instance, Direction side)
	{
		return instance.writeNBT();
	}
	
	@Override
	public void readNBT(Capability<BodyDamageCapability> capability, BodyDamageCapability instance, Direction side, INBT nbt)
	{
		if (nbt instanceof CompoundNBT)
		{
			instance.readNBT((CompoundNBT) nbt);
		}
	}
}