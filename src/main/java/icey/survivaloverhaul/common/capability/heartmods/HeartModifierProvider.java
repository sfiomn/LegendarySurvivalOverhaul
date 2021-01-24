package icey.survivaloverhaul.common.capability.heartmods;

import icey.survivaloverhaul.Main;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class HeartModifierProvider implements ICapabilitySerializable<INBT>
{
	private LazyOptional<HeartModifierCapability> instance = LazyOptional.of(Main.HEART_MOD_CAP::getDefaultInstance);
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
	{
		return Main.HEART_MOD_CAP.orEmpty(cap, instance);
	}
	
	@Override
	public INBT serializeNBT()
	{
		return Main.HEART_MOD_CAP.getStorage().writeNBT(Main.HEART_MOD_CAP, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
	}
	
	@Override
	public void deserializeNBT(INBT nbt)
	{
		Main.HEART_MOD_CAP.getStorage().readNBT(Main.HEART_MOD_CAP, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
	}

}
