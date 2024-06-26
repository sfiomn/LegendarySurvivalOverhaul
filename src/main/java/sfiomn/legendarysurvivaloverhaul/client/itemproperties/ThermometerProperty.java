package sfiomn.legendarysurvivaloverhaul.client.itemproperties;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureItemCapability;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;


public class ThermometerProperty implements ClampedItemPropertyFunction {

    @OnlyIn(Dist.CLIENT)
    @Override
    public float unclampedCall(@NotNull ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity entity, int i) {
        Level level = clientLevel;
        Entity holder = (entity != null ? entity : itemStack.getFrame());

        if (level == null && holder != null)
        {
            level = holder.level();
        }

        if (level == null)
        {
            return 0.5f;
        }
        else
        {
            try
            {
                TemperatureItemCapability tempItemCap = CapabilityUtil.getTempItemCapability(itemStack);
                if (holder != null && tempItemCap.shouldUpdate(level.getGameTime())) {
                    tempItemCap.updateWorldTemperature(level, holder, level.getGameTime());
                }
                return Mth.positiveModulo(TemperatureUtil.clampTemperature((int) tempItemCap.getWorldTemperatureLevel()) / TemperatureEnum.HEAT_STROKE.getUpperBound(), 1.0333333f);
            }
            catch (NullPointerException e)
            {
                return 0.5f;
            }

        }
    }
}
