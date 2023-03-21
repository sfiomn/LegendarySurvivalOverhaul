package sfiomn.legendarysurvivaloverhaul.common.capabilities.heartmods;

import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class HeartModifierProvider implements ICapabilitySerializable<INBT>
{
	private LazyOptional<HeartModifierCapability> instance = LazyOptional.of(LegendarySurvivalOverhaul.HEART_MOD_CAP::getDefaultInstance);
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
	{
		return LegendarySurvivalOverhaul.HEART_MOD_CAP.orEmpty(cap, instance);
	}
	
	@Override
	public INBT serializeNBT()
	{
		return LegendarySurvivalOverhaul.HEART_MOD_CAP.getStorage().writeNBT(LegendarySurvivalOverhaul.HEART_MOD_CAP, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
	}
	
	@Override
	public void deserializeNBT(INBT nbt)
	{
		LegendarySurvivalOverhaul.HEART_MOD_CAP.getStorage().readNBT(LegendarySurvivalOverhaul.HEART_MOD_CAP, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
	}

}
