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
import sfiomn.legendarysurvivaloverhaul.util.WorldUtil;


public class ThermometerProperty implements IItemPropertyGetter {

    private long updateTime;
    private float lastTemperature = 0.0f;

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
                long currTime = System.currentTimeMillis();
                if ((currTime - updateTime) > 500) {
                    updateTime = currTime;
                    float d = (float) WorldUtil.calculateClientWorldEntityTemperature(world, holder) / TemperatureEnum.HEAT_STROKE.getUpperBound();
                    lastTemperature = MathHelper.positiveModulo(d, 1.0333333f);
                }
                return lastTemperature;
            }
            catch (NullPointerException e)
            {
                return 0.5f;
            }

        }
    }
}
