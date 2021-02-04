package icey.survivaloverhaul.common.capability.frozen;

import icey.survivaloverhaul.Main;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class FrozenProvider implements ICapabilitySerializable<INBT>
{
	private LazyOptional<FrozenCapability> instance = LazyOptional.of(Main.FROZEN_CAP::getDefaultInstance);
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
	{
		return Main.FROZEN_CAP.orEmpty(cap, instance);
	}
	
	@Override
	public INBT serializeNBT()
	{
		return Main.FROZEN_CAP.getStorage().writeNBT(Main.FROZEN_CAP, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
	}
	
	@Override
	public void deserializeNBT(INBT nbt)
	{
		Main.FROZEN_CAP.getStorage().readNBT(Main.FROZEN_CAP, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
	}
}
