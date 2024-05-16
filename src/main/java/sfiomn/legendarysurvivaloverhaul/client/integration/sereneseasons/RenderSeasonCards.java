package sfiomn.legendarysurvivaloverhaul.client.integration.sereneseasons;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.util.RenderUtil;

@OnlyIn(Dist.CLIENT)
public class RenderSeasonCards {
    private static final ResourceLocation SPRING_CARD = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/cards/spring.png");
    private static final ResourceLocation AUTUMN_CARD = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/cards/autumn.png");
    private static final ResourceLocation SUMMER_CARD = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/cards/summer.png");
    private static final ResourceLocation WINTER_CARD = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/cards/winter.png");
    private static final int CARD_TEXT_WIDTH = 256;
    private	static final int CARD_TEXT_HEIGHT = 256;
    private static final int CARD_WIDTH = 128;
    private	static final int CARD_HEIGHT = 128;
    private static ResourceLocation seasonCard = null;
    private static Season lastSeason = null;
    private static float fadeLevel = 0;
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
            RenderUtil.drawTexturedModelRectWithAlpha(m4f, x, y, CARD_WIDTH, CARD_HEIGHT, 0, 0, CARD_TEXT_WIDTH, CARD_TEXT_HEIGHT, fadeLevel);
        }
    }

    public static void updateSeasonCardFading(PlayerEntity player) {
        if (player != null && player.isAlive()) {

            Season currentSeason = SeasonHelper.getSeasonState(player.level).getSeason();

            float targetFadeLevel;
            if (lastSeason == null || currentSeason != lastSeason)
                targetFadeLevel = 1.0f;
            else
                targetFadeLevel = 0;

            if (targetFadeLevel > fadeLevel) {
                fadeLevel = Math.min(targetFadeLevel, fadeLevel + 0.05f);
            }
            if (targetFadeLevel < fadeLevel) {
                fadeLevel = Math.max(targetFadeLevel, fadeLevel - 0.05f);
            }

            if (fadeLevel > 0 && fadeLevel < 1.0f) {
                if (currentSeason == Season.AUTUMN)
                    seasonCard = AUTUMN_CARD;
                else if (currentSeason == Season.SPRING)
                    seasonCard = SPRING_CARD;
                else if (currentSeason == Season.SUMMER)
                    seasonCard = SUMMER_CARD;
                else if (currentSeason == Season.WINTER)
                    seasonCard = WINTER_CARD;
            } else if (fadeLevel == 1.0f) {
                if (cardTimer++ >= 80) {
                    cardTimer = 0;
                    lastSeason = currentSeason;
                }
            } else {
                seasonCard = null;
            }
        }
    }

    private static void bind(ResourceLocation resource)
    {
        Minecraft.getInstance().getTextureManager().bind(resource);
    }
}
