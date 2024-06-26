package sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.config.FertilityConfig;
import sereneseasons.config.ServerConfig;
import sereneseasons.init.ModFertility;
import sereneseasons.init.ModTags;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.config.Config;

import static sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsModifier.biomeIdentities;

public class SereneSeasonsUtil {
    public static Component formatSeasonName(BlockPos blockPos, Level level) {
        if (!LegendarySurvivalOverhaul.sereneSeasonsLoaded)
            return Component.translatable("message.legendarysurvivaloverhaul.sereneseasons.no_serene_season_loaded");

        ISeasonState season = SeasonHelper.getSeasonState(level);

        if (!ServerConfig.isDimensionWhitelisted(level.dimension()))
            return Component.translatable("message.legendarysurvivaloverhaul.sereneseasons.no_season_dimension");

        int seasonType = getSeasonType(level, level.getBiome(blockPos).get());
        int subSeasonDuration = (int) ((double) season.getSubSeasonDuration() / (double) season.getDayDuration());

        String subSeasonName = "";
        if (seasonType == 2) {
            return Component.translatable("message.legendarysurvivaloverhaul.sereneseasons.no_season_info");

        } else if (seasonType == 1 && Config.Baked.tropicalSeasonsEnabled) {
            for(String word : season.getTropicalSeason().toString().split("_", 0)) {
                subSeasonName = word.charAt(0) + word.substring(1).toLowerCase();
            }
            return Component.translatable("message.legendarysurvivaloverhaul.sereneseasons.season_info",
                    subSeasonName,
                    (season.getDay() + subSeasonDuration) % (subSeasonDuration * 2),
                    subSeasonDuration * 2);

        } else {
            for(String word : season.getSubSeason().toString().split("_", 0)) {
                subSeasonName = word.charAt(0) + word.substring(1).toLowerCase();
            }
            return Component.translatable("message.legendarysurvivaloverhaul.sereneseasons.season_info",
                    subSeasonName,
                    season.getDay() % subSeasonDuration,
                    subSeasonDuration);
        }
    }

    //  Season type 0 = normal, Season type 1 = tropical, Season type 2 = no season
    public static int getSeasonType(Level level, Biome biome) {
        ResourceLocation biomeName = level.registryAccess().registryOrThrow(Registries.BIOME).getKey(biome);
        float temperature = biome.getBaseTemperature();
        boolean isBiomeTropical;

        if (biomeName != null && biomeIdentities.containsKey(biomeName.toString()))
        {
            SSBiomeIdentity identity = biomeIdentities.get(biomeName.toString());
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

    public static boolean plantCanGrow(Level world, BlockPos pos, Block plant) {
        ResourceLocation resourceLocation = ForgeRegistries.BLOCKS.getKey(plant);
        if (resourceLocation != null) {
            boolean isFertile = ModFertility.isCropFertile(resourceLocation.getPath(), world, pos);
            if (FertilityConfig.seasonalCrops.get() && !isFertile && !isGlassAboveBlock(world, pos)) {
                if (FertilityConfig.outOfSeasonCropBehavior.get() == 1 || FertilityConfig.outOfSeasonCropBehavior.get() == 2) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isGlassAboveBlock(Level world, BlockPos cropPos)
    {
        for(int i = 0; i < 16; ++i) {
            if (world.getBlockState(cropPos.offset(0, i + 1, 0)).is(ModTags.Blocks.GREENHOUSE_GLASS)) {
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
