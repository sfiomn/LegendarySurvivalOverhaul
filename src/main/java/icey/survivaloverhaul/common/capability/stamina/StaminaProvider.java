package icey.survivaloverhaul.common.capability.stamina;

import icey.survivaloverhaul.Main;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class StaminaProvider implements ICapabilitySerializable<INBT>
{
	private LazyOptional<StaminaCapability> instance = LazyOptional.of(Main.STAMINA_CAP::getDefaultInstance);
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
	{
		return Main.STAMINA_CAP.orEmpty(cap, instance);
	}
	
	@Override
	public INBT serializeNBT()
	{
		return Main.STAMINA_CAP.getStorage().writeNBT(Main.STAMINA_CAP, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
	}
	
	@Override
	public void deserializeNBT(INBT nbt)
	{
		Main.STAMINA_CAP.getStorage().readNBT(Main.STAMINA_CAP, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
	}
}
