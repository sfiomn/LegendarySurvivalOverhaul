package sfiomn.legendarysurvivaloverhaul.client.tooltips;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.Style;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstEnum;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.RenderUtil;

import java.util.Optional;

public class ThirstTooltip {

    public static final ResourceLocation ICONS = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/overlay.png");
    public static final int THIRST_TEXTURE_WIDTH = 9;
    public static final int THIRST_TEXTURE_HEIGHT = 9;

    public int thirst;
    public float saturation;
    public float dirty;
    public int thirstIconNumber;
    private String thirstBarText;
    public int saturationIconNumber;
    private String saturationBarText;
    public int dirtyIconNumber;
    private String placeholderTooltip;

    public ThirstTooltip(int thirst, float saturation, float dirty) {
        this.thirst = thirst;
        this.saturation = saturation;
        this.dirty = dirty;

        this.thirstIconNumber = (int) Math.ceil(Math.abs(thirst) / 2f);
        if (thirstIconNumber > 10)
        {
            thirstBarText = "x" + ((thirst < 0 ? -1 : 1) * thirstIconNumber);
            thirstIconNumber = 1;
        }

        if (Config.Baked.thirstSaturationDisplayed) {
            this.saturationIconNumber = (int) Math.ceil(Math.abs(saturation) / 2f);
        } else {
            this.saturationIconNumber = 0;
        }
        if (saturationIconNumber > 10 || saturation == 0)
        {
            saturationBarText = "x" + ((saturation < 0 ? -1 : 1) * saturationIconNumber);
            saturationIconNumber = 1;
        }

        this.dirtyIconNumber = 0;
        if (dirty > 0.0f && dirty < 1.0f) {
            dirtyIconNumber = 5;
        }
    }

    public ThirstTooltip(ThirstEnum thirstEnum) {
        this(thirstEnum.getThirst(), thirstEnum.getSaturation(), thirstEnum.getDirtiness());
    }

    public String getPlaceholderTooltip() {
        if (placeholderTooltip != null)
        {
            return placeholderTooltip;
        }
        // Scale blank string to match 9x9 icon
        float scale = 2.2f;

        float thirstBarLength = thirstIconNumber * scale;
        if (thirstBarText != null)
            thirstBarLength += thirstBarText.length();

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

        // Thirst bar
        int left = tooltipX + (this.thirstIconNumber - 1) * THIRST_TEXTURE_WIDTH;
        int top = tooltipY + 2;

        int xOffsetTexture = 0;
        if (this.thirst >= 0) {
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

        // Draw the thirst bubbles
        for (int i = 0; i < this.thirstIconNumber; i++)
        {
            int halfIcon = i * 2 + 1;
            int x = left - i * THIRST_TEXTURE_WIDTH;
            int y = top;

            Matrix4f m4f = matrixStack.last().pose();
            if (halfIcon < Math.abs(this.thirst)) // Full thirst icon
                RenderUtil.drawTexturedModelRect(m4f, x, y, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT, xOffsetTexture, 0, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);
            else if (halfIcon == Math.abs(this.thirst)) // Half thirst icon
                RenderUtil.drawTexturedModelRect(m4f, x, y, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT, xOffsetTexture + THIRST_TEXTURE_WIDTH, 0, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);
        }
        if (thirstBarText != null) {
            int x = left + 18;
            int y = top;
            matrixStack.pushPose();
            matrixStack.translate(x, y, 0);
            matrixStack.scale(0.75f, 0.75f, 0.75f);
            Minecraft.getInstance().font.draw(matrixStack, thirstBarText, 2, 2, 0xFFAAAAAA);
            matrixStack.popPose();
        }

        // Saturation bar
        // If merge thirst and saturation, left is kept from thirst alignment to align both the saturation bar and the thirst bar
        if (saturationIconNumber > 0 && Config.Baked.thirstSaturationDisplayed && !Config.Baked.mergeThirstAndSaturationTooltip) {
            top += 10;
            left = tooltipX + (saturationIconNumber - 1) * THIRST_TEXTURE_WIDTH;
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
        RenderSystem.enableLighting();
        RenderSystem.enableAlphaTest();
    }

    static class ThirstFont extends ResourceLocation
    {
        ThirstTooltip thirstTooltip;
        ThirstFont(ThirstTooltip thirstTooltip)
        {
            super(Style.DEFAULT_FONT.getNamespace(), Style.DEFAULT_FONT.getPath());
            this.thirstTooltip = thirstTooltip;
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

        static ThirstTooltip getThirstTooltip(ITextProperties line)
        {
            Object fontId = getFontId(line);
            if (fontId instanceof ThirstFont) {
                return ((ThirstFont) fontId).thirstTooltip;
            }
            return null;
        }
    }
}
