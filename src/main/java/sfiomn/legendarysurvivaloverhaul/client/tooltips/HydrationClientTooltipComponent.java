package sfiomn.legendarysurvivaloverhaul.client.tooltips;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public class HydrationClientTooltipComponent implements ClientTooltipComponent {
    public static final ResourceLocation ICONS = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/overlay.png");
    public static final int THIRST_TEXTURE_WIDTH = 9;
    public static final int THIRST_TEXTURE_HEIGHT = 9;
    
    public final int hydration;
    public final float saturation;
    public int hydrationIconNumber;
    public int saturationIconNumber;

    public HydrationClientTooltipComponent(int hydration, float saturation) {
        this.hydration = hydration;
        this.saturation = saturation;

        this.hydrationIconNumber = Math.min((int) Math.ceil(Math.abs(hydration) / 2f), 10);

        if (Config.Baked.thirstSaturationDisplayed) {
            this.saturationIconNumber = Math.min((int) Math.ceil(Math.abs(saturation) / 2f), 10);
        } else {
            this.saturationIconNumber = 0;
        }
    }

    @Override
    public int getHeight() {
        int height = 14;

        // Saturation bar
        // If merge thirst and saturation, left is kept from thirst alignment to align both the saturation bar and the thirst bar
        if (saturationIconNumber > 0 && Config.Baked.thirstSaturationDisplayed) {
            if (hydrationIconNumber > 0 && !Config.Baked.mergeHydrationAndSaturationTooltip)
                height += 10;
        }

        return height;
    }

    @Override
    public int getWidth(Font font) {
        return Math.max(hydrationIconNumber, saturationIconNumber) * THIRST_TEXTURE_WIDTH;
    }

    @Override
    public void renderImage(Font font, int tooltipX, int tooltipY, GuiGraphics gui) {
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getRendertypeTextShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        int leftMax = 0;
        leftMax = Math.max(tooltipX + (this.hydrationIconNumber - 1) * THIRST_TEXTURE_WIDTH,
                tooltipX + (saturationIconNumber - 1) * THIRST_TEXTURE_WIDTH);

        // Hydration bar
        int left = 0;
        if (!Config.Baked.mergeHydrationAndSaturationTooltip)
            left = tooltipX + (this.hydrationIconNumber - 1) * THIRST_TEXTURE_WIDTH;
        else
            left = leftMax;
        int top = tooltipY + 2;

        int xOffsetTexture = 0;
        if (this.hydration >= 0)
            xOffsetTexture = THIRST_TEXTURE_WIDTH;
        else
            xOffsetTexture = THIRST_TEXTURE_WIDTH + THIRST_TEXTURE_WIDTH * 3;

        // Draw the hydration bubbles
        for (int i = 0; i < this.hydrationIconNumber; i++) {
            int halfIcon = i * 2 + 1;
            int x = left - i * THIRST_TEXTURE_WIDTH;
            int y = top;
            if (halfIcon < Math.abs(this.hydration)) // Full thirst icon
                gui.blit(ICONS, x, y, xOffsetTexture, 0, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT, 256, 256);
            else if (halfIcon == Math.abs(this.hydration)) // Half thirst icon
                gui.blit(ICONS, x, y, xOffsetTexture + THIRST_TEXTURE_WIDTH, 0, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT, 256, 256);
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
        } else
            xOffsetTexture = THIRST_TEXTURE_WIDTH * 14;

        // Draw the saturation bubbles
        for (int i = 0; i < saturationIconNumber; i++) {
            int halfIcon = i * 2 + 1;
            int x = left - i * THIRST_TEXTURE_WIDTH;
            int y = top;

            if (halfIcon < (int) Math.ceil(Math.abs(saturation))) // Full saturation icon
                gui.blit(ICONS, x, y, xOffsetTexture, 0, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT, 256, 256);
            else if (halfIcon == (int) Math.ceil(Math.abs(saturation))) // Half saturation icon
                gui.blit(ICONS, x, y, xOffsetTexture + THIRST_TEXTURE_WIDTH, 0, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT, 256, 256);
        }

        RenderSystem.disableBlend();

        // reset to drawHoveringText state
        RenderSystem.disableDepthTest();
    }
}
