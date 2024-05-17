package sfiomn.legendarysurvivaloverhaul.client.itemproperties;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SSBiomeIdentity;
import sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;

import static sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsModifier.biomeIdentities;


public class SeasonalCalendarSeasonTypeProperty implements IItemPropertyGetter {

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

        if (world == null || holder == null)
        {
            return 2.0f;
        }
        else
        {
            try
            {
                Biome biome = world.getBiome(new BlockPos(holder.position()));
                int seasonType = SereneSeasonsUtil.getSeasonType(biome);

                if (seasonType == 2)
                    return 2.0f;
                else if (seasonType == 1 && Config.Baked.tropicalSeasonsEnabled)
                    return 1.0f;
                else
                    return 0;
            }
            catch (NullPointerException e)
            {
                return 2.0f;
            }

        }
    }
}
