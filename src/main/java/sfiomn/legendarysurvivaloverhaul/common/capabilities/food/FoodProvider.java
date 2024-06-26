package sfiomn.legendarysurvivaloverhaul.common.capabilities.food;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FoodProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag>
{
	public static Capability<FoodCapability> FOOD_CAPABILITY = CapabilityManager.get(new CapabilityToken<FoodCapability>() { });
	private final LazyOptional<FoodCapability> instance = LazyOptional.of(this::getInstance);
	private FoodCapability foodCapability = null;

	private FoodCapability getInstance() {
		if (this.foodCapability == null) {
			this.foodCapability = new FoodCapability();
		}
		return this.foodCapability;
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction)
	{
		if (capability == FOOD_CAPABILITY)
			return instance.cast();
		return LazyOptional.empty();
	}

	@Override
	public CompoundTag serializeNBT()
	{
		return new CompoundTag();
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
	}
}
