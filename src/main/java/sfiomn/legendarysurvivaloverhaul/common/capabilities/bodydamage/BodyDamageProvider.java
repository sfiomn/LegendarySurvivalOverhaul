package sfiomn.legendarysurvivaloverhaul.common.capabilities.bodydamage;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.food.FoodCapability;

public class BodyDamageProvider implements ICapabilitySerializable<INBT>
{
	private final LazyOptional<BodyDamageCapability> instance = LazyOptional.of(LegendarySurvivalOverhaul.BODY_DAMAGE_CAP::getDefaultInstance);
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
	{
		return LegendarySurvivalOverhaul.BODY_DAMAGE_CAP.orEmpty(cap, instance);
	}
	
	@Override
	public INBT serializeNBT()
	{
		return LegendarySurvivalOverhaul.BODY_DAMAGE_CAP.getStorage().writeNBT(LegendarySurvivalOverhaul.BODY_DAMAGE_CAP, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
	}
	
	@Override
	public void deserializeNBT(INBT nbt)
	{
		LegendarySurvivalOverhaul.BODY_DAMAGE_CAP.getStorage().readNBT(LegendarySurvivalOverhaul.BODY_DAMAGE_CAP, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
	}
}
