package icey.survivaloverhaul.common.capability.stamina;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class StaminaStorage implements IStorage<StaminaCapability>
{
	@Override
	public INBT writeNBT(Capability<StaminaCapability> capability, StaminaCapability instance, Direction side)
	{
		return instance.save();
	}
	
	@Override
	public void readNBT(Capability<StaminaCapability> capability, StaminaCapability instance, Direction side, INBT nbt)
	{
		if (nbt instanceof CompoundNBT)
		{
			instance.load((CompoundNBT) nbt);
		}
	}
}