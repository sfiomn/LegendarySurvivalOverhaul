package sfiomn.legendarysurvivaloverhaul.common.compat.sereneseasons;

import net.minecraft.block.Block;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.config.BiomeConfig;
import sereneseasons.config.FertilityConfig;
import sereneseasons.init.ModFertility;
import sereneseasons.init.ModTags;

public class SereneSeasonsUtil {
    public static StringTextComponent formatSeasonName(BlockPos blockPos, World world){
        ISeasonState season = SeasonHelper.getSeasonState(world);
        RegistryKey<Biome> biomeName = world.getBiomeName(blockPos).orElse(null);
        StringBuilder seasonText = new StringBuilder();
        if(biomeName != null && BiomeConfig.usesTropicalSeasons(biomeName)){
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
