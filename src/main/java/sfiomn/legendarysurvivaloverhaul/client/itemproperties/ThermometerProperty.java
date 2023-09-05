package sfiomn.legendarysurvivaloverhaul.client.itemproperties;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureItemCapability;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;


public class ThermometerProperty implements IItemPropertyGetter {

    @OnlyIn(Dist.CLIENT)
    @Override
    public float call(ItemStack stack, ClientWorld clientWorld, LivingEntity entity)
    {
        World world = clientWorld;
        Entity holder = (entity != null ? entity : stack.getFrame());

        if (world == null && holder != null)
        {
            world = holder.level;
        }

        if (world == null)
        {
            return 0.5f;
        }
        else
        {
            try
            {
                TemperatureItemCapability tempItemCap = CapabilityUtil.getTempItemCapability(stack);
                if (holder != null && tempItemCap.shouldUpdate(world.getGameTime())) {
                    tempItemCap.updateWorldTemperature(world, holder, world.getGameTime());
                }
                return MathHelper.positiveModulo(TemperatureUtil.clampTemperature((int) tempItemCap.getWorldTemperatureLevel()) / TemperatureEnum.HEAT_STROKE.getUpperBound(), 1.0333333f);
            }
            catch (NullPointerException e)
            {
                return 0.5f;
            }

        }
    }
}
