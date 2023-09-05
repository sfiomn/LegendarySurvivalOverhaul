package sfiomn.legendarysurvivaloverhaul.common.capabilities.food;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class FoodStorage implements IStorage<FoodCapability>
{
	@Override
	public INBT writeNBT(Capability<FoodCapability> capability, FoodCapability instance, Direction side)
	{
		return new CompoundNBT();
	}
	
	@Override
	public void readNBT(Capability<FoodCapability> capability, FoodCapability instance, Direction side, INBT nbt)
	{
	}
}