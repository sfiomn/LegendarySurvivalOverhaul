package sfiomn.legendarysurvivaloverhaul.util.internal;

import net.minecraft.entity.player.PlayerEntity;
import sfiomn.legendarysurvivaloverhaul.api.wetness.IWetnessUtil;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

public class WetnessUtilInternal implements IWetnessUtil {
    @Override
    public void addWetness(PlayerEntity player, int wetness) {
        if(!Config.Baked.wetnessEnabled)
            return;

        WetnessCapability cap = CapabilityUtil.getWetnessCapability(player);
        cap.addWetness(wetness);
    }

    @Override
    public void deactivateWetness(PlayerEntity player) {
        if(!Config.Baked.wetnessEnabled)
            return;

        WetnessCapability cap = CapabilityUtil.getWetnessCapability(player);
        cap.setWetnessTickTimer(-1);
        cap.setDirty();
    }

    @Override
    public void activateWetness(PlayerEntity player) {
        if(!Config.Baked.wetnessEnabled)
            return;

        WetnessCapability cap = CapabilityUtil.getWetnessCapability(player);
        if (cap.getWetnessTickTimer() == -1) {
            cap.setWetnessTickTimer(0);
            cap.setDirty();
        }
    }

    @Override
    public boolean isWetnessActive(PlayerEntity player) {
        if(!Config.Baked.wetnessEnabled)
            return false;

        return CapabilityUtil.getWetnessCapability(player).getWetnessTickTimer() != -1;
    }
}
