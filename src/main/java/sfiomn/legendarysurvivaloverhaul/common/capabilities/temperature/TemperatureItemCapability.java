package sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
    public void updateWorldTemperature(Level world, Entity holder, long currentTick) {
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

    public CompoundTag writeNBT()
    {
        CompoundTag compound = new CompoundTag();

        compound.putFloat("temperature", this.temperature);

        return compound;
    }

    public void readNBT(CompoundTag compound)
    {
        this.init();
        if (compound.contains("temperature"))
            this.setWorldTemperatureLevel(compound.getFloat("temperature"));
    }

    public static class TemperatureItemProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag>
    {
        public static Capability<TemperatureItemCapability> TEMPERATURE_ITEM_CAPABILITY = CapabilityManager.get(new CapabilityToken<TemperatureItemCapability>() { });
        private final LazyOptional<TemperatureItemCapability> instance = LazyOptional.of(this::getInstance);
        private TemperatureItemCapability temperatureItemCapability = null;

        private TemperatureItemCapability getInstance() {
            if (this.temperatureItemCapability == null) {
                this.temperatureItemCapability = new TemperatureItemCapability();
            }
            return this.temperatureItemCapability;
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction)
        {
            if (capability == TEMPERATURE_ITEM_CAPABILITY)
                return instance.cast();
            return LazyOptional.empty();
        }

        @Override
        public CompoundTag serializeNBT() {
            return getInstance().writeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag tag) {
            getInstance().readNBT(tag);
        }
    }
}
