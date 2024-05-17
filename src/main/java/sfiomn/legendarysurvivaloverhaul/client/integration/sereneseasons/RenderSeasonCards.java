package sfiomn.legendarysurvivaloverhaul.client.integration.sereneseasons;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.world.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.config.SeasonsConfig;
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
    private static final int CARD_TEXT_WIDTH = 256;
    private	static final int CARD_TEXT_HEIGHT = 256;
    private static final int CARD_WIDTH = 128;
    private	static final int CARD_HEIGHT = 128;
    private static ResourceLocation seasonCard = null;
    private static Season lastSeason = null;
    private static SereneSeasonsUtil.TropicalSeason lastTropicalSeason = null;
    private static DimensionType lastDimension = null;
    private static boolean isDimensionSeasonal;
    private static float fadeLevel = 0;
    private static int delayTimer = 40;
    private static int cardTimer = 0;

    public static void render(MatrixStack matrix, int width, int height)
    {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        drawSeasonCard(matrix, width, height);

        RenderSystem.disableBlend();
        bind(AbstractGui.GUI_ICONS_LOCATION);
    }

    public static void drawSeasonCard(MatrixStack matrix, int width, int height) {
        if (seasonCard != null) {
            Matrix4f m4f = matrix.last().pose();
            bind(seasonCard);
            float x = width / 2.0f - CARD_WIDTH / 2.0f;
            float y = height / 4.0f - CARD_HEIGHT / 2.0f;
            RenderUtil.drawTexturedModelRectWithAlpha(m4f, x + Config.Baked.seasonCardsOffsetX, y + Config.Baked.seasonCardsOffsetY, CARD_WIDTH, CARD_HEIGHT, 0, 0, CARD_TEXT_WIDTH, CARD_TEXT_HEIGHT, fadeLevel);
        }
    }

    public static void updateSeasonCardFading(PlayerEntity player) {
        if (player != null && player.isAlive()) {
            if (delayTimer > 0) {
                delayTimer--;
                return;
            }

            if (lastDimension == null || lastDimension != player.level.dimensionType()) {
                isDimensionSeasonal = SeasonsConfig.isDimensionWhitelisted(player.level.dimension());
                lastDimension = player.level.dimensionType();
            }

            Season currentSeason = null;
            SereneSeasonsUtil.TropicalSeason currentTropicalSeason = null;
            int seasonType;
            if (isDimensionSeasonal) {
                seasonType = SereneSeasonsUtil.getSeasonType(player.level.getBiome(player.blockPosition()));
                if (seasonType == 0 || (seasonType == 1 && !Config.Baked.tropicalSeasonsEnabled))
                    currentSeason = SeasonHelper.getSeasonState(player.level).getSeason();
                else if (seasonType == 1)
                    currentTropicalSeason = SereneSeasonsUtil.TropicalSeason.getTropicalSeason(SeasonHelper.getSeasonState(player.level).getTropicalSeason());
            } else if (lastSeason != null)
                reset();

            float targetFadeLevel = 0;
            if (((currentSeason != lastSeason && currentSeason != null) || (currentTropicalSeason != lastTropicalSeason && currentTropicalSeason != null)) && isDimensionSeasonal)
                targetFadeLevel = 1.0f;

            if (targetFadeLevel > fadeLevel) {
                fadeLevel = Math.min(targetFadeLevel, fadeLevel + 0.05f);
            }
            if (targetFadeLevel < fadeLevel) {
                fadeLevel = Math.max(targetFadeLevel, fadeLevel - 0.05f);
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
                if (cardTimer++ >= 60) {
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
        delayTimer = 40;
        fadeLevel = 0;
    }

    private static void bind(ResourceLocation resource)
    {
        Minecraft.getInstance().getTextureManager().bind(resource);
    }
}
