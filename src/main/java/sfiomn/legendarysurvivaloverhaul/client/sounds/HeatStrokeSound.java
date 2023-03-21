package sfiomn.legendarysurvivaloverhaul.client.sounds;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureCapability;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

@OnlyIn(Dist.CLIENT)
public class HeatStrokeSound extends TemperatureEffectSound {
    private final TemperatureCapability tempCap;

    public HeatStrokeSound(PlayerEntity player) {
        super(player, SoundRegistry.HEAT_STROKE.get());
        tempCap = CapabilityUtil.getTempCapability(player);
    }

    protected boolean shouldPlaySound() {
        return tempCap.getTemperatureLevel() >= TemperatureEnum.HEAT_STROKE.getMiddle();
    }
}
