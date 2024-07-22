package sfiomn.legendarysurvivaloverhaul.client.itemproperties;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sereneseasons.config.ServerConfig;
import sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;


public class SeasonalCalendarSeasonTypeProperty implements ClampedItemPropertyFunction {

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

        if (level == null || holder == null)
        {
            return 0.2f;
        }
        else
        {
            try
            {
                if (!ServerConfig.isDimensionWhitelisted(level.dimension()))
                    return 0.2f;

                SereneSeasonsUtil.SeasonType seasonType = SereneSeasonsUtil.getSeasonType(level.getBiome(holder.blockPosition()));

                if (seasonType == SereneSeasonsUtil.SeasonType.NO_SEASON)
                    return 0.2f;
                else if (seasonType == SereneSeasonsUtil.SeasonType.TROPICAL_SEASON)
                    return 0.1f;
                else
                    return 0;
            }
            catch (NullPointerException e)
            {
                return 0.2f;
            }

        }
    }
}
