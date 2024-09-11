package sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons;

import net.minecraft.core.Holder;
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

import static sereneseasons.init.ModTags.Biomes.BLACKLISTED_BIOMES;
import static sereneseasons.init.ModTags.Biomes.TROPICAL_BIOMES;

public class SereneSeasonsUtil {
    public static double averageSeasonTemperature;
    public static double averageTropicalSeasonTemperature;

    public static Component seasonTooltip(BlockPos blockPos, Level level) {
        if (!LegendarySurvivalOverhaul.sereneSeasonsLoaded)
            return Component.translatable("message.legendarysurvivaloverhaul.sereneseasons.no_serene_season_loaded");


        if (!ServerConfig.isDimensionWhitelisted(level.dimension()))
            return Component.translatable("message.legendarysurvivaloverhaul.sereneseasons.no_season_dimension");

        SeasonType seasonType = getSeasonType(level.getBiome(blockPos));
        ISeasonState season = SeasonHelper.getSeasonState(level);
        int subSeasonDuration = (int) ((double) season.getSubSeasonDuration() / (double) season.getDayDuration());

        StringBuilder subSeasonName = new StringBuilder();
        if (seasonType == SeasonType.NO_SEASON) {
            return Component.translatable("message.legendarysurvivaloverhaul.sereneseasons.no_season_info");

        } else if (seasonType == SeasonType.TROPICAL_SEASON) {
            for(String word : season.getTropicalSeason().toString().split("_", 0)) {
                subSeasonName.append(word.charAt(0)).append(word.substring(1).toLowerCase()).append(" ");
            }
            return Component.translatable("message.legendarysurvivaloverhaul.sereneseasons.season_info",
                    subSeasonName.toString(),
                    ((season.getDay() + subSeasonDuration) % (subSeasonDuration * 2)) + 1,
                    subSeasonDuration * 2);

        } else {
            for(String word : season.getSubSeason().toString().split("_", 0)) {
                subSeasonName.append(word.charAt(0)).append(word.substring(1).toLowerCase()).append(" ");
            }
            return Component.translatable("message.legendarysurvivaloverhaul.sereneseasons.season_info",
                    subSeasonName.toString(),
                    (season.getDay() % subSeasonDuration) + 1,
                    subSeasonDuration);
        }
    }

    public static SeasonType getSeasonType(Holder<Biome> biome) {
        if (Config.Baked.tropicalSeasonsEnabled && biome.is(TROPICAL_BIOMES))
            return SeasonType.TROPICAL_SEASON;
        else if (!Config.Baked.defaultSeasonEnabled && biome.is(BLACKLISTED_BIOMES))
            return SeasonType.NO_SEASON;
        return SeasonType.NORMAL_SEASON;
    }

    public static boolean isGlassAboveBlock(Level world, BlockPos cropPos) {
        for(int i = 0; i < 16; ++i) {
            if (world.getBlockState(cropPos.offset(0, i + 1, 0)).is(ModTags.Blocks.GREENHOUSE_GLASS)) {
                return true;
            }
        }

        return false;
    }

    public static boolean plantCanGrow(Level world, BlockPos pos, Block plant) {
        ResourceLocation resourceLocation = ForgeRegistries.BLOCKS.getKey(plant);
        if (resourceLocation != null) {
            boolean isFertile = ModFertility.isCropFertile(resourceLocation.getPath(), world, pos);
            if (FertilityConfig.seasonalCrops.get() && !isFertile && !isGlassAboveBlock(world, pos)) {
                return FertilityConfig.outOfSeasonCropBehavior.get() != 1 && FertilityConfig.outOfSeasonCropBehavior.get() != 2;
            }
        }
        return true;
    }

    public static void initAverageTemperatures() {
        averageSeasonTemperature += Config.Baked.earlyAutumnModifier;
        averageSeasonTemperature += Config.Baked.earlySpringModifier;
        averageSeasonTemperature += Config.Baked.earlySummerModifier;
        averageSeasonTemperature += Config.Baked.earlyWinterModifier;
        averageSeasonTemperature += Config.Baked.midAutumnModifier;
        averageSeasonTemperature += Config.Baked.midSpringModifier;
        averageSeasonTemperature += Config.Baked.midSummerModifier;
        averageSeasonTemperature += Config.Baked.midWinterModifier;
        averageSeasonTemperature += Config.Baked.lateAutumnModifier;
        averageSeasonTemperature += Config.Baked.lateSpringModifier;
        averageSeasonTemperature += Config.Baked.lateSummerModifier;
        averageSeasonTemperature += Config.Baked.lateWinterModifier;
        averageSeasonTemperature /= 12;

        averageTropicalSeasonTemperature += Config.Baked.earlyWetSeasonModifier;
        averageTropicalSeasonTemperature += Config.Baked.earlyDrySeasonModifier;
        averageTropicalSeasonTemperature += Config.Baked.midWetSeasonModifier;
        averageTropicalSeasonTemperature += Config.Baked.midDrySeasonModifier;
        averageTropicalSeasonTemperature += Config.Baked.lateWetSeasonModifier;
        averageTropicalSeasonTemperature += Config.Baked.lateDrySeasonModifier;
        averageTropicalSeasonTemperature /= 6;
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

    public enum SeasonType {
        NO_SEASON(0.2f),
        TROPICAL_SEASON(0.1f),
        NORMAL_SEASON(0.0f);

        public final float propertyValue;
        SeasonType(float propertyValue) {
            this.propertyValue = propertyValue;
        }
    }
}
