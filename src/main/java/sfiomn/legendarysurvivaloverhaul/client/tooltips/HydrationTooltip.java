package sfiomn.legendarysurvivaloverhaul.client.tooltips;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.Style;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.RenderUtil;

import java.util.Optional;

public class HydrationTooltip {

    public static final ResourceLocation ICONS = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/overlay.png");
    public static final int THIRST_TEXTURE_WIDTH = 9;
    public static final int THIRST_TEXTURE_HEIGHT = 9;

    public int hydration;
    public float saturation;
    public float dirty;
    public int hydrationIconNumber;
    private String hydrationBarText;
    public int saturationIconNumber;
    private String saturationBarText;
    public int dirtyIconNumber;
    private String placeholderTooltip;

    public HydrationTooltip(int hydration, float saturation, float dirty) {
        this.hydration = hydration;
        this.saturation = saturation;
        this.dirty = dirty;

        this.hydrationIconNumber = (int) Math.ceil(Math.abs(hydration) / 2f);
        if (hydrationIconNumber > 10)
        {
            hydrationBarText = "x" + ((hydration < 0 ? -1 : 1) * hydrationIconNumber);
            hydrationIconNumber = 1;
        }

        if (Config.Baked.thirstSaturationDisplayed) {
            this.saturationIconNumber = (int) Math.ceil(Math.abs(saturation) / 2f);
        } else {
            this.saturationIconNumber = 0;
        }
        if (saturationIconNumber > 10)
        {
            saturationBarText = "x" + ((saturation < 0 ? -1 : 1) * saturationIconNumber);
            saturationIconNumber = 1;
        }

        this.dirtyIconNumber = 0;
        if (dirty > 0.0f && dirty < 1.0f && (hydrationIconNumber > 0 || saturationIconNumber > 0)) {
            dirtyIconNumber = 5;
        }
    }

    public HydrationTooltip(HydrationEnum hydrationEnum) {
        this(hydrationEnum.getHydration(), hydrationEnum.getSaturation(), hydrationEnum.getDirtiness());
    }

    public String getPlaceholderTooltip() {
        if (placeholderTooltip != null)
        {
            return placeholderTooltip;
        }
        // Scale blank string to match 9x9 icon
        float scale = 2.2f;

        float thirstBarLength = hydrationIconNumber * scale;
        if (hydrationBarText != null)
            thirstBarLength += hydrationBarText.length();

        float saturationBarLength = 0;
        if (Config.Baked.thirstSaturationDisplayed) {
            saturationBarLength = saturationIconNumber * scale;
            if (saturationBarText != null)
                saturationBarLength += saturationBarText.length();
        }
        float dirtyBarLength = dirtyIconNumber * scale;

        int length = (int) Math.ceil(Math.max(thirstBarLength, Math.max(saturationBarLength, dirtyBarLength)));
        StringBuilder placeholder = new StringBuilder(" ");
        for (int i=0; i< length; i++) {
            placeholder.append(" ");
        }
        placeholderTooltip = placeholder.toString();
        return placeholderTooltip;
    }

    public void renderTooltipIcons(MatrixStack matrixStack, int tooltipX, int tooltipY, int tooltipZ) {
        RenderSystem.disableLighting();
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        matrixStack.pushPose();
        matrixStack.translate(0.0D, 0.0D, tooltipZ);

        Minecraft.getInstance().getTextureManager().bind(ICONS);

        int leftMax = 0;
        leftMax = Math.max(tooltipX + (this.hydrationIconNumber - 1) * THIRST_TEXTURE_WIDTH,
                tooltipX + (saturationIconNumber - 1) * THIRST_TEXTURE_WIDTH);

        // Thirst bar
        int left = 0;
        if (!Config.Baked.mergeHydrationAndSaturationTooltip)
            left = tooltipX + (this.hydrationIconNumber - 1) * THIRST_TEXTURE_WIDTH;
        else
            left = leftMax;
        int top = tooltipY + 2;

        int xOffsetTexture = 0;
        if (this.hydration >= 0) {
            xOffsetTexture = THIRST_TEXTURE_WIDTH;
            // Show the thirst bar dirty if dirty chance 100%
            if (dirty >= 1.0f) {
                xOffsetTexture += THIRST_TEXTURE_WIDTH * 3;
            }
        } else {
            xOffsetTexture = THIRST_TEXTURE_WIDTH * 10;
            // Show the thirst bar dirty if dirty chance 100%
            if (dirty >= 1.0f) {
                xOffsetTexture += THIRST_TEXTURE_WIDTH * 2;
            }
        }

        // Draw the hydration bubbles
        for (int i = 0; i < this.hydrationIconNumber; i++)
        {
            int halfIcon = i * 2 + 1;
            int x = left - i * THIRST_TEXTURE_WIDTH;
            int y = top;

            Matrix4f m4f = matrixStack.last().pose();
            if (halfIcon < Math.abs(this.hydration)) // Full thirst icon
                RenderUtil.drawTexturedModelRect(m4f, x, y, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT, xOffsetTexture, 0, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);
            else if (halfIcon == Math.abs(this.hydration)) // Half thirst icon
                RenderUtil.drawTexturedModelRect(m4f, x, y, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT, xOffsetTexture + THIRST_TEXTURE_WIDTH, 0, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);
        }
        if (hydrationBarText != null) {
            int x = left + 18;
            int y = top;
            matrixStack.pushPose();
            matrixStack.translate(x, y, 0);
            matrixStack.scale(0.75f, 0.75f, 0.75f);
            Minecraft.getInstance().font.draw(matrixStack, hydrationBarText, 2, 2, 0xFFAAAAAA);
            matrixStack.popPose();
        }

        // Saturation bar
        // If merge thirst and saturation, left is kept from thirst alignment to align both the saturation bar and the thirst bar
        if (saturationIconNumber > 0 && Config.Baked.thirstSaturationDisplayed) {
            if (hydrationIconNumber > 0 && !Config.Baked.mergeHydrationAndSaturationTooltip)
                top += 10;
            if (!Config.Baked.mergeHydrationAndSaturationTooltip)
                left = tooltipX + (saturationIconNumber - 1) * THIRST_TEXTURE_WIDTH;
            else
                left = leftMax;
        }

        if (this.saturation >= 0) {
            xOffsetTexture = THIRST_TEXTURE_WIDTH * 6;
            // Show the thirst bar dirty if dirty chance 100%
            if (dirty >= 1.0f) {
                xOffsetTexture += THIRST_TEXTURE_WIDTH * 2;
            }
        } else {
            xOffsetTexture = THIRST_TEXTURE_WIDTH * 14;
        }

        // Draw the saturation bubbles
        for (int i = 0; i < saturationIconNumber; i++) {
            int halfIcon = i * 2 + 1;
            int x = left - i * THIRST_TEXTURE_WIDTH;
            int y = top;

            Matrix4f m4f = matrixStack.last().pose();
            if (halfIcon < (int) Math.ceil(Math.abs(saturation))) { // Full saturation icon
                RenderUtil.drawTexturedModelRect(m4f, x, y, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT, xOffsetTexture, 0, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);
            } else if (halfIcon == (int) Math.ceil(Math.abs(saturation))) { // Half saturation icon
                RenderUtil.drawTexturedModelRect(m4f, x, y, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT, xOffsetTexture + THIRST_TEXTURE_WIDTH, 0, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);
            }
        }

        if (saturationBarText != null)
        {
            int x = left + 18;
            int y = top;
            matrixStack.pushPose();
            matrixStack.translate(x, y, 0);
            matrixStack.scale(0.75f, 0.75f, 0.75f);
            Minecraft.getInstance().font.draw(matrixStack, saturationBarText, 2, 1, 0xFFAAAAAA);
            matrixStack.popPose();
        }

        // Dirty bar
        if (dirtyIconNumber > 0) {
            top += 10;
        }
        left = tooltipX + (dirtyIconNumber - 1) * THIRST_TEXTURE_WIDTH;

        xOffsetTexture = THIRST_TEXTURE_WIDTH * 3;
        // Draw the dirty bubbles
        for (int i = 0; i < dirtyIconNumber; i++) {
            int halfIcon = i * 2 + 1;
            int x = left - i * THIRST_TEXTURE_WIDTH;
            int y = top;

            Matrix4f m4f = matrixStack.last().pose();
            if (halfIcon < (int) (dirty * 10)) { // Full dirty icon
                RenderUtil.drawTexturedModelRect(m4f, x, y, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT, xOffsetTexture + THIRST_TEXTURE_WIDTH, 0, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);
            } else if (halfIcon == (int) (dirty * 10)) { // Half dirty icon
                RenderUtil.drawTexturedModelRect(m4f, x, y, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT, xOffsetTexture + (THIRST_TEXTURE_WIDTH * 2), 0, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);
            } else {
                RenderUtil.drawTexturedModelRect(m4f, x, y, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT, xOffsetTexture, 0, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);
            }
        }

        matrixStack.popPose();

        RenderSystem.disableBlend();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        Minecraft.getInstance().getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);

        // reset to drawHoveringText state
        RenderSystem.disableRescaleNormal();
        RenderHelper.turnOff();
        RenderSystem.disableLighting();
        RenderSystem.disableDepthTest();
    }

    static class ThirstFont extends ResourceLocation
    {
        HydrationTooltip hydrationTooltip;
        ThirstFont(HydrationTooltip hydrationTooltip)
        {
            super(Style.DEFAULT_FONT.getNamespace(), Style.DEFAULT_FONT.getPath());
            this.hydrationTooltip = hydrationTooltip;
        }

        static Object getFontId(ITextProperties line)
        {
            if (line instanceof ITextComponent)
                return ((ITextComponent)line).getStyle().getFont();

            final Object[] fontId = { Style.DEFAULT_FONT };
            line.visit(new ITextProperties.IStyledTextAcceptor<ITextProperties>() {
                public Optional<ITextProperties> accept(Style n, String s) {
                    fontId[0] = n.getFont();
                    return Optional.empty();
                }
            }, Style.EMPTY);
            return fontId[0];
        }

        static HydrationTooltip getHydrationTooltip(ITextProperties line)
        {
            Object fontId = getFontId(line);
            if (fontId instanceof ThirstFont) {
                return ((ThirstFont) fontId).hydrationTooltip;
            }
            return null;
        }
    }
}
