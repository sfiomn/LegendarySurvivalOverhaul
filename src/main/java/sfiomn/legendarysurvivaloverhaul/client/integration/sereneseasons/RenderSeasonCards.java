package sfiomn.legendarysurvivaloverhaul.client.integration.sereneseasons;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
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
    private static RegistryKey<World> lastDimension = null;
    private static boolean isDimensionSeasonal;
    private static float fadeLevel = 0;
    private static final int delayTimeTicks = Config.Baked.seasonCardsSpawnDimensionDelayInTicks;
    private static int delayTimer = delayTimeTicks;
    private static boolean fadeIn;
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
        if (player == null || !player.isAlive())
            return;
        if (lastDimension == null || lastDimension != player.level.dimension()) {
            delayTimer = delayTimeTicks;
            lastDimension = player.level.dimension();
            isDimensionSeasonal = SeasonsConfig.isDimensionWhitelisted(lastDimension);
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
            SereneSeasonsUtil.SeasonType seasonType = SereneSeasonsUtil.getSeasonType(player.level.getBiome(player.blockPosition()));

            if (seasonType == SereneSeasonsUtil.SeasonType.NORMAL_SEASON || (seasonType == SereneSeasonsUtil.SeasonType.TROPICAL_SEASON && !Config.Baked.tropicalSeasonsEnabled)) {
                currentSeason = SeasonHelper.getSeasonState(player.level).getSeason();
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
                currentTropicalSeason = SereneSeasonsUtil.TropicalSeason.getTropicalSeason(SeasonHelper.getSeasonState(player.level).getTropicalSeason());
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
        seasonCard = null;
        lastSeason = null;
        lastTropicalSeason = null;
        fadeLevel = 0;
    }

    public static void init() {
        seasonCard = null;
        lastSeason = null;
        lastTropicalSeason = null;
        lastDimension = null;
    }

    private static void bind(ResourceLocation resource)
    {
        Minecraft.getInstance().getTextureManager().bind(resource);
    }
}
