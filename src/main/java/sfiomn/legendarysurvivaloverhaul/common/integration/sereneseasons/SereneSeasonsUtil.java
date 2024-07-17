package sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.config.FertilityConfig;
import sereneseasons.config.SeasonsConfig;
import sereneseasons.init.ModFertility;
import sereneseasons.init.ModTags;
import sereneseasons.season.SeasonTime;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.config.Config;

import java.util.Objects;

import static sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsModifier.biomeIdentities;

public class SereneSeasonsUtil {
    public static StringTextComponent formatSeasonName(BlockPos blockPos, World world) {
        if (!LegendarySurvivalOverhaul.sereneSeasonsLoaded)
            return new StringTextComponent(new TranslationTextComponent("message.legendarysurvivaloverhaul.sereneseasons.no_serene_season_loaded").getString());

        ISeasonState season = SeasonHelper.getSeasonState(world);

        if (!SeasonsConfig.isDimensionWhitelisted(world.dimension()))
            return new StringTextComponent(new TranslationTextComponent("message.legendarysurvivaloverhaul.sereneseasons.no_season_dimension").getString());

        int seasonType = getSeasonType(world.getBiome(blockPos));
        int subSeasonDuration = (int) ((double) season.getSubSeasonDuration() / (double) season.getDayDuration());

        StringBuilder subSeasonName = new StringBuilder();
        TranslationTextComponent seasonTextTranslate;
        if (seasonType == 2) {
            return new StringTextComponent(new TranslationTextComponent("message.legendarysurvivaloverhaul.sereneseasons.no_season_info").getString());
        } else if (seasonType == 1 && Config.Baked.tropicalSeasonsEnabled) {
            for(String word : season.getTropicalSeason().toString().split("_", 0)) {
                subSeasonName.append(word.charAt(0)).append(word.substring(1).toLowerCase()).append(" ");
            }
            seasonTextTranslate = new TranslationTextComponent("message.legendarysurvivaloverhaul.sereneseasons.season_info",
                    subSeasonName.toString(),
                    ((season.getDay() + subSeasonDuration) % (subSeasonDuration * 2)) + 1,
                    subSeasonDuration * 2);
        } else {
            for(String word : season.getSubSeason().toString().split("_", 0)) {
                subSeasonName.append(word.charAt(0)).append(word.substring(1).toLowerCase()).append(" ");
            }
            seasonTextTranslate = new TranslationTextComponent("message.legendarysurvivaloverhaul.sereneseasons.season_info",
                    subSeasonName.toString(),
                    (season.getDay() % subSeasonDuration) + 1,
                    subSeasonDuration);
        }
        return new StringTextComponent(seasonTextTranslate.getString());
    }

    //  Season type 0 = normal, Season type 1 = tropical, Season type 2 = no season
    public static int getSeasonType(Biome biome) {
        ResourceLocation biomeName = biome.getRegistryName();
        float temperature = biome.getBaseTemperature();
        boolean isBiomeTropical;

        if (biomeName != null && biomeIdentities.containsKey(biomeName.toString()))
        {
            SSBiomeIdentity identity = biomeIdentities.get(biome.getRegistryName().toString());
            if (!identity.seasonEffects)
                return Config.Baked.defaultSeasonEnabled ? temperature > 0.8f ? 1 : 0 : 2;
            isBiomeTropical = identity.isTropical;
        }
        else
        {
            isBiomeTropical = temperature > 0.8f;
        }
        if (isBiomeTropical)
            return 1;
        return 0;
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

    public enum TropicalSeason {
        DRY(Season.TropicalSeason.EARLY_DRY, Season.TropicalSeason.MID_DRY, Season.TropicalSeason.LATE_DRY),
        WET(Season.TropicalSeason.EARLY_WET, Season.TropicalSeason.MID_WET, Season.TropicalSeason.LATE_WET);

        public final Season.TropicalSeason earlyTropicalSeason;
        public final Season.TropicalSeason midTropicalSeason;
        public final Season.TropicalSeason lateTropicalSeason;

        TropicalSeason(Season.TropicalSeason earlyTropicalSeason, Season.TropicalSeason midTropicalSeason, Season.TropicalSeason lateTropicalSeason) {
            this.earlyTropicalSeason = earlyTropicalSeason;
            this.midTropicalSeason = midTropicalSeason;
            this.lateTropicalSeason = lateTropicalSeason;
        }

        public static TropicalSeason getTropicalSeason(Season.TropicalSeason subTropicalSeason) {
            for (TropicalSeason season: values()) {
                if (subTropicalSeason == season.earlyTropicalSeason ||
                        subTropicalSeason == season.midTropicalSeason ||
                        subTropicalSeason == season.lateTropicalSeason) {
                    return season;
                }
            }
            return null;
        }
    }
}
