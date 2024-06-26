package sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public class WetnessProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag>
{
	public static Capability<WetnessCapability> WETNESS_CAPABILITY = CapabilityManager.get(new CapabilityToken<WetnessCapability>() { });
	private final LazyOptional<WetnessCapability> instance = LazyOptional.of(this::getInstance);
	private WetnessCapability wetnessCapability = null;

	private WetnessCapability getInstance() {
		if (this.wetnessCapability == null) {
			this.wetnessCapability = new WetnessCapability();
		}
		return this.wetnessCapability;
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, Direction side)
	{
		if (capability == WETNESS_CAPABILITY)
			return instance.cast();
		return LazyOptional.empty();
	}

	@Override
	public CompoundTag serializeNBT()
	{
		return getInstance().writeNBT();
	}

	@Override
	public void deserializeNBT(CompoundTag tag) {
		getInstance().readNBT(tag);
	}
}
