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
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.season.SeasonTime;


public class SeasonalCalendarTimeProperty implements ClampedItemPropertyFunction {

    @OnlyIn(Dist.CLIENT)
    @Override
    public float unclampedCall(@NotNull ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity entity, int i)
    {
        Level level = clientLevel;
        Entity holder = (entity != null ? entity : itemStack.getFrame());

        if (level == null && holder != null)
        {
            level = holder.level();
        }

        if (level == null)
        {
            return 0;
        }
        else
        {
            try
            {
                double d0;

                int seasonCycleTicks = SeasonHelper.getSeasonState(level).getSeasonCycleTicks();
                d0 = (double)((float)seasonCycleTicks / (float) SeasonTime.ZERO.getCycleDuration());

                return Mth.positiveModulo((float)d0, 1.0F);
            }
            catch (NullPointerException e)
            {
                return 0;
            }

        }
    }
}
