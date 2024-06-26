package sfiomn.legendarysurvivaloverhaul.common.capabilities.bodydamage;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BodyDamageProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag>
{
	public static Capability<BodyDamageCapability> BODY_DAMAGE_CAPABILITY = CapabilityManager.get(new CapabilityToken<BodyDamageCapability>() { });
	private final LazyOptional<BodyDamageCapability> instance = LazyOptional.of(this::getInstance);
	private BodyDamageCapability bodyDamageCapability = null;

	private BodyDamageCapability getInstance() {
		if (this.bodyDamageCapability == null) {
			this.bodyDamageCapability = new BodyDamageCapability();
		}
		return this.bodyDamageCapability;
	}
	
	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction)
	{
		if (capability == BODY_DAMAGE_CAPABILITY)
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
