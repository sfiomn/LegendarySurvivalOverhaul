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
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.season.SeasonTime;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureItemCapability;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;


public class SeasonalCalendarTimeProperty implements IItemPropertyGetter {

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
            return 0;
        }
        else
        {
            try
            {
                double d0;

                int seasonCycleTicks = SeasonHelper.getSeasonState(world).getSeasonCycleTicks();
                d0 = (double)((float)seasonCycleTicks / (float) SeasonTime.ZERO.getCycleDuration());

                return MathHelper.positiveModulo((float)d0, 1.0F);
            }
            catch (NullPointerException e)
            {
                return 0;
            }

        }
    }
}
