package sfiomn.legendarysurvivaloverhaul.common.capabilities.heartmods;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.minecraftforge.common.util.LazyOptional;

public class HeartModifierProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag>
{
	public static Capability<HeartModifierCapability> HEART_MODIFIER_CAPABILITY = CapabilityManager.get(new CapabilityToken<HeartModifierCapability>() { });
	private final LazyOptional<HeartModifierCapability> instance = LazyOptional.of(this::getInstance);
	private HeartModifierCapability heartModifierCapability = null;

	private HeartModifierCapability getInstance() {
		if (this.heartModifierCapability == null) {
			this.heartModifierCapability = new HeartModifierCapability();
		}
		return this.heartModifierCapability;
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction)
	{
		if (capability == HEART_MODIFIER_CAPABILITY)
			return instance.cast();
		return LazyOptional.empty();
	}

	@Override
	public CompoundTag serializeNBT()
	{
		return getInstance().writeNBT();
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		getInstance().readNBT(nbt);
	}
}
