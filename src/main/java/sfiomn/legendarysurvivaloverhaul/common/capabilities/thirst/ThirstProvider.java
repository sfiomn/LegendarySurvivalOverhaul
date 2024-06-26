package sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public class ThirstProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag>
{
	public static Capability<ThirstCapability> THIRST_CAPABILITY = CapabilityManager.get(new CapabilityToken<ThirstCapability>() { });
	private final LazyOptional<ThirstCapability> instance = LazyOptional.of(this::getInstance);
	private ThirstCapability thirstCapability = null;

	private ThirstCapability getInstance() {
		if (this.thirstCapability == null) {
			this.thirstCapability = new ThirstCapability();
		}
		return this.thirstCapability;
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, Direction side)
	{
		if (capability == THIRST_CAPABILITY)
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
