package sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.config.FertilityConfig;
import sereneseasons.init.ModFertility;
import sereneseasons.init.ModTags;

import static sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsModifier.biomeIdentities;

public class SereneSeasonsUtil {
    public static StringTextComponent formatSeasonName(BlockPos blockPos, World world){
        StringBuilder seasonText = new StringBuilder();

        ISeasonState season = SeasonHelper.getSeasonState(world);

        if (season == null) {
            return new StringTextComponent("");
        }

        Biome biome = world.getBiome(blockPos);
        ResourceLocation biomeName = biome.getRegistryName();
        float temperature = biome.getBaseTemperature();
        boolean isBiomeTropical;

        if (biomeName != null && biomeIdentities.containsKey(biomeName.toString()))
        {
            SSBiomeIdentity identity = biomeIdentities.get(biome.getRegistryName().toString());
            if (!identity.seasonEffects)
                return new StringTextComponent("");
            isBiomeTropical = identity.isTropical;
        }
        else
        {
            isBiomeTropical = temperature > 0.8f;
        }

        if(isBiomeTropical){
            for(String word : season.getTropicalSeason().toString().split("_", 0)){
                seasonText.append(word.charAt(0)).append(word.substring(1).toLowerCase()).append(" ");
            }
        }else {
            for(String word : season.getSubSeason().toString().split("_", 0)){
                seasonText.append(word.charAt(0)).append(word.substring(1).toLowerCase()).append(" ");
            }
        }
        seasonText.append("Season");
        return new StringTextComponent(seasonText.toString());
    }

    public static boolean plantCanGrow(World world, BlockPos pos, Block plant)
    {
        boolean isFertile = ModFertility.isCropFertile(plant.getRegistryName().toString(), world, pos);
        if (FertilityConfig.seasonalCrops.get() && !isFertile && !isGlassAboveBlock(world, pos))
        {
            if (FertilityConfig.outOfSeasonCropBehavior.get() == 1 || FertilityConfig.outOfSeasonCropBehavior.get() == 2)
            {
                return false;
            }
        }
        return true;
    }

    private static boolean isGlassAboveBlock(World world, BlockPos cropPos)
    {
        for (int i = 0; i < 16; i++)
        {
            if (world.getBlockState(cropPos.offset(0, i + 1, 0)).getBlock().is(ModTags.Blocks.greenhouse_glass))
            {
                return true;
            }
        }

        return false;
    }

}
