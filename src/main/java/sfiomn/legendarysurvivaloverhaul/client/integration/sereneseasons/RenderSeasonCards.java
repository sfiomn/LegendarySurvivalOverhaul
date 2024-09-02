package sfiomn.legendarysurvivaloverhaul.client.integration.sereneseasons;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.config.ServerConfig;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.RenderUtil;

@OnlyIn(Dist.CLIENT)
public class RenderSeasonCards {
    private static final ResourceLocation SPRING_CARD = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/cards/spring.png");
    private static final ResourceLocation AUTUMN_CARD = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/cards/autumn.png");
    private static final ResourceLocation SUMMER_CARD = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/cards/summer.png");
    private static final ResourceLocation WINTER_CARD = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/cards/winter.png");
    private static final ResourceLocation DRY_CARD = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/cards/dry.png");
    private static final ResourceLocation WET_CARD = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/cards/wet.png");
    private static final int CARD_WIDTH = 128;
    private	static final int CARD_HEIGHT = 128;
    private static ResourceLocation seasonCard = null;
    private static Season lastSeason = null;
    private static SereneSeasonsUtil.TropicalSeason lastTropicalSeason = null;
    private static ResourceKey<Level> lastDimension = null;
    private static boolean isDimensionSeasonal;
    private static float fadeLevel = 0;
    private static boolean fadeIn;
    private static int delayTimer = 0;
    private static int cardTimer = 0;

    public static IGuiOverlay SEASON_CARD_GUI = (forgeGui, guiGraphics, partialTicks, width, height) -> {
        if (LegendarySurvivalOverhaul.sereneSeasonsLoaded && Config.Baked.seasonCardsEnabled &&
                seasonCard != null) {
            int x = Mth.floor(width / 2.0f - CARD_WIDTH / 2.0f);
            int y = Mth.floor(height / 4.0f - CARD_HEIGHT / 2.0f);

            forgeGui.setupOverlayRenderState(true, false);

            Minecraft.getInstance().getProfiler().push("season_card");
            RenderSystem.setShaderTexture(0, seasonCard);
            RenderUtil.drawTexturedModelRectWithAlpha(guiGraphics.pose().last().pose(), x + Config.Baked.seasonCardsOffsetX, y + Config.Baked.seasonCardsOffsetY, 128, 128, 0, 0, 256, 256, fadeLevel);
            Minecraft.getInstance().getProfiler().pop();
        }
    };

    public static void updateSeasonCardFading(Player player) {
        if (player == null || !player.isAlive())
            return;

        Level level = player.level();
        if (lastDimension == null || lastDimension != level.dimension()) {
            delayTimer = Config.Baked.seasonCardsSpawnDimensionDelayInTicks;
            isDimensionSeasonal = ServerConfig.isDimensionWhitelisted(level.dimension());
            lastDimension = level.dimension();
        }

        if (!isDimensionSeasonal) {
            if (lastSeason != null || lastTropicalSeason != null)
                reset();
            return;
        }

        if (delayTimer > 0) {
            delayTimer--;
            return;
        }

        if (seasonCard == null) {
            Season currentSeason;
            SereneSeasonsUtil.TropicalSeason currentTropicalSeason;
            SereneSeasonsUtil.SeasonType seasonType = SereneSeasonsUtil.getSeasonType(level.getBiome(player.blockPosition()));

            if (seasonType == SereneSeasonsUtil.SeasonType.NORMAL_SEASON) {
                currentSeason = SeasonHelper.getSeasonState(level).getSeason();
                if (currentSeason != lastSeason) {
                    lastSeason = currentSeason;
                    fadeIn = true;
                    cardTimer = 0;
                    if (currentSeason == Season.AUTUMN)
                        seasonCard = AUTUMN_CARD;
                    else if (currentSeason == Season.SPRING)
                        seasonCard = SPRING_CARD;
                    else if (currentSeason == Season.SUMMER)
                        seasonCard = SUMMER_CARD;
                    else if (currentSeason == Season.WINTER)
                        seasonCard = WINTER_CARD;
                }
            } else if (seasonType == SereneSeasonsUtil.SeasonType.TROPICAL_SEASON) {
                currentTropicalSeason = SereneSeasonsUtil.TropicalSeason.getTropicalSeason(SeasonHelper.getSeasonState(level).getTropicalSeason());
                if (currentTropicalSeason != lastTropicalSeason) {
                    lastTropicalSeason = currentTropicalSeason;
                    fadeIn = true;
                    cardTimer = 0;
                    if (currentTropicalSeason == SereneSeasonsUtil.TropicalSeason.DRY)
                        seasonCard = DRY_CARD;
                    else if (currentTropicalSeason == SereneSeasonsUtil.TropicalSeason.WET)
                        seasonCard = WET_CARD;
                }
            }
        }

        if (seasonCard != null) {
            float targetFadeLevel = 0.0f;
            if (fadeIn)
                targetFadeLevel = 1.0f;

            if (targetFadeLevel > fadeLevel) {
                fadeLevel = Math.min(targetFadeLevel, fadeLevel + (Math.round(1.0f / Config.Baked.seasonCardsFadeInInTicks * 100) / 100.0f));
            }
            if (targetFadeLevel < fadeLevel) {
                fadeLevel = Math.max(targetFadeLevel, fadeLevel - (Math.round(1.0f / Config.Baked.seasonCardsFadeOutInTicks * 100) / 100.0f));
            }

            if (fadeLevel == 1.0f) {
                if (cardTimer++ >= Config.Baked.seasonCardsDisplayTimeInTicks)
                    fadeIn = false;
            } else if (fadeLevel == 0.0f) {
                seasonCard = null;
            }
        }
    }

    public static void reset() {
        lastSeason = null;
        lastTropicalSeason = null;
        seasonCard = null;
        fadeLevel = 0;
    }

    public static void init() {
        lastDimension = null;
    }
}
