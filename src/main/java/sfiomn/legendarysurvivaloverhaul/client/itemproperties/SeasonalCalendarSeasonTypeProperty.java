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
                ResourceLocation biomeName = biome.getRegistryName();
                float temperature = biome.getBaseTemperature();
                boolean isTropical;

                if (biomeName != null && biomeIdentities.containsKey(biomeName.toString())) {
                    SSBiomeIdentity identity = biomeIdentities.get(biomeName.toString());
                    if (!identity.seasonEffects)
                        return 2.0f;
                    else
                        isTropical = identity.isTropical;
                } else
                    isTropical = temperature > 0.8f;
                if (Config.Baked.tropicalSeasonsEnabled && isTropical)
                    return 1.0f;
                return 0;
            }
            catch (NullPointerException e)
            {
                return 2.0f;
            }

        }
    }
}
