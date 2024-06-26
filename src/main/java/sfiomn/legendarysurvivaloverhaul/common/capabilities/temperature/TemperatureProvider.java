package sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.minecraftforge.common.util.LazyOptional;

public class TemperatureProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag>
{
	public static Capability<TemperatureCapability> TEMPERATURE_CAPABILITY = CapabilityManager.get(new CapabilityToken<TemperatureCapability>() { });
	private final LazyOptional<TemperatureCapability> instance = LazyOptional.of(this::getInstance);
	private TemperatureCapability temperatureCapability = null;

	private TemperatureCapability getInstance() {
		if (this.temperatureCapability == null) {
			this.temperatureCapability = new TemperatureCapability();
		}
		return this.temperatureCapability;
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction)
	{
		if (capability == TEMPERATURE_CAPABILITY)
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
