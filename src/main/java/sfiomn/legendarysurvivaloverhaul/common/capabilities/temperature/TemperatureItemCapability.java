package sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ITemperatureItemCapability;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.util.WorldUtil;

public class TemperatureItemCapability implements ITemperatureItemCapability {
    private float temperature;
    private long updateTick;

    public TemperatureItemCapability() {
        this.init();
    }

    private void init() {
        this.temperature = TemperatureEnum.NORMAL.getMiddle();
        this.updateTick = 0;
    }

    @Override
    public boolean shouldUpdate(long currentTick) {
        return (currentTick - this.updateTick) > 10;
    }

    @Override
    public void updateWorldTemperature(World world, Entity holder, long currentTick) {
        this.updateTick = currentTick;
        this.temperature = WorldUtil.calculateClientWorldEntityTemperature(world, holder);
    }

    @Override
    public float getWorldTemperatureLevel() {
        return this.temperature;
    }

    @Override
    public void setWorldTemperatureLevel(float temperature) {
        this.temperature = temperature;
    }

    public CompoundNBT writeNBT()
    {
        CompoundNBT compound = new CompoundNBT();

        compound.putFloat("temperature", this.temperature);

        return compound;
    }

    public void readNBT(CompoundNBT compound)
    {
        this.init();
        if (compound.contains("temperature"))
            this.setWorldTemperatureLevel(compound.getFloat("temperature"));
    }

    public static class TemperatureItemProvider implements ICapabilityProvider
    {
        private final LazyOptional<TemperatureItemCapability> instance = LazyOptional.of(LegendarySurvivalOverhaul.TEMPERATURE_ITEM_CAP::getDefaultInstance);

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
        {
            return LegendarySurvivalOverhaul.TEMPERATURE_ITEM_CAP.orEmpty(cap, instance);
        }
    }

    public static class Storage implements IStorage<TemperatureItemCapability>
    {
        @Override
        public INBT writeNBT(Capability<TemperatureItemCapability> capability, TemperatureItemCapability instance, Direction side)
        {
            return instance.writeNBT();
        }

        @Override
        public void readNBT(Capability<TemperatureItemCapability> capability, TemperatureItemCapability instance, Direction side, INBT nbt)
        {
            if (nbt instanceof CompoundNBT)
            {
                instance.readNBT((CompoundNBT) nbt);
            }
        }
    }
}
