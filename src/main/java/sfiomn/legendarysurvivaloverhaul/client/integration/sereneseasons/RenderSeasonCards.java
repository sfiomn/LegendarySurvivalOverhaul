package sfiomn.legendarysurvivaloverhaul.client.integration.sereneseasons;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.config.ServerConfig;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;

@OnlyIn(Dist.CLIENT)
public class RenderSeasonCards {
    private static final ResourceLocation SPRING_CARD = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/cards/spring.png");
    private static final ResourceLocation AUTUMN_CARD = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/cards/autumn.png");
    private static final ResourceLocation SUMMER_CARD = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/cards/summer.png");
    private static final ResourceLocation WINTER_CARD = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/cards/winter.png");
    private static final ResourceLocation DRY_CARD = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/cards/dry.png");
    private static final ResourceLocation WET_CARD = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/cards/wet.png");
    private static final int CARD_TEXT_WIDTH = 256;
    private	static final int CARD_TEXT_HEIGHT = 256;
    private static final int CARD_WIDTH = 128;
    private	static final int CARD_HEIGHT = 128;
    private static ResourceLocation seasonCard = null;
    private static Season lastSeason = null;
    private static SereneSeasonsUtil.TropicalSeason lastTropicalSeason = null;
    private static ResourceKey<Level> lastDimension = null;
    private static boolean isDimensionSeasonal;
    private static float fadeLevel = 0;
    private static final int delayTimeTicks = Config.Baked.seasonCardsSpawnDimensionDelayInTicks;
    private static int delayTimer = delayTimeTicks;
    private static int cardTimer = 0;

    public static void render(GuiGraphics gui, int width, int height)
    {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableColorLogicOp();
        RenderSystem.clearColor(1.0F, 1.0F, 1.0F, fadeLevel);

        drawSeasonCard(gui, width, height);

        RenderSystem.clearColor(1.0F,1.0F,1.0F,1.0F);
        RenderSystem.enableColorLogicOp();
        RenderSystem.disableBlend();
    }

    public static void drawSeasonCard(GuiGraphics gui, int width, int height) {
        if (seasonCard != null) {
            int x = Mth.floor(width / 2.0f - CARD_WIDTH / 2.0f);
            int y = Mth.floor(height / 4.0f - CARD_HEIGHT / 2.0f);
            gui.blit(seasonCard, x + Config.Baked.seasonCardsOffsetX, y + Config.Baked.seasonCardsOffsetY, CARD_WIDTH, CARD_HEIGHT, 0, 0, CARD_TEXT_WIDTH, CARD_TEXT_HEIGHT);
        }
    }

    public static void updateSeasonCardFading(Player player) {
        if (player != null && player.isAlive()) {
            Level level = player.level();
            if (lastDimension == null || lastDimension != level.dimension()) {
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

            Season currentSeason = null;
            SereneSeasonsUtil.TropicalSeason currentTropicalSeason = null;
            int seasonType = SereneSeasonsUtil.getSeasonType(level, level.getBiome(player.blockPosition()).get());

            if (seasonType == 0 || (seasonType == 1 && !Config.Baked.tropicalSeasonsEnabled))
                currentSeason = SeasonHelper.getSeasonState(level).getSeason();
            else if (seasonType == 1)
                currentTropicalSeason = SereneSeasonsUtil.TropicalSeason.getTropicalSeason(SeasonHelper.getSeasonState(level).getTropicalSeason());

            float targetFadeLevel = 0;
            if (((currentSeason != lastSeason && currentSeason != null) || (currentTropicalSeason != lastTropicalSeason && currentTropicalSeason != null)) && isDimensionSeasonal)
                targetFadeLevel = 1.0f;

            if (targetFadeLevel > fadeLevel) {
                fadeLevel = Math.min(targetFadeLevel, fadeLevel + (Math.round(1.0f / Config.Baked.seasonCardsFadeInInTicks * 100) / 100.0f));
            }
            if (targetFadeLevel < fadeLevel) {
                fadeLevel = Math.max(targetFadeLevel, fadeLevel - (Math.round(1.0f / Config.Baked.seasonCardsFadeOutInTicks * 100) / 100.0f));
            }

            if (fadeLevel > 0 && fadeLevel < 1.0f) {
                cardTimer = 0;
                if (currentSeason != null) {
                    if (currentSeason == Season.AUTUMN)
                        seasonCard = AUTUMN_CARD;
                    else if (currentSeason == Season.SPRING)
                        seasonCard = SPRING_CARD;
                    else if (currentSeason == Season.SUMMER)
                        seasonCard = SUMMER_CARD;
                    else if (currentSeason == Season.WINTER)
                        seasonCard = WINTER_CARD;
                } else if (currentTropicalSeason != null) {
                    if (currentTropicalSeason == SereneSeasonsUtil.TropicalSeason.DRY)
                        seasonCard = DRY_CARD;
                    else if (currentTropicalSeason == SereneSeasonsUtil.TropicalSeason.WET)
                        seasonCard = WET_CARD;
                }
            } else if (fadeLevel == 1.0f) {
                if (cardTimer++ >= Config.Baked.seasonCardsDisplayTimeInTicks) {
                    if (currentSeason != null)
                        lastSeason = currentSeason;
                    else if (currentTropicalSeason != null) {
                        lastTropicalSeason = currentTropicalSeason;
                    }
                }
            } else {
                seasonCard = null;
            }
        }
    }

    public static void reset() {
        lastSeason = null;
        lastTropicalSeason = null;
        delayTimer = delayTimeTicks;
        fadeLevel = 0;
    }
}
