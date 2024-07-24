package sfiomn.legendarysurvivaloverhaul.client.tooltips;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.config.Config;

import java.util.Objects;

public class HydrationClientTooltipComponent implements ClientTooltipComponent {
    public static final ResourceLocation ICONS = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/overlay.png");
    public static final int THIRST_TEXTURE_WIDTH = 9;
    public static final int THIRST_TEXTURE_HEIGHT = 9;
    
    public final int hydration;
    public final float saturation;
    public final float effectChance;
    public int hydrationIconNumber;
    public int saturationIconNumber;
    public int chanceIconNumber;

    public HydrationClientTooltipComponent(int hydration, float saturation, float effectChance, String effectName) {
        this.hydration = hydration;
        this.saturation = saturation;
        this.effectChance = effectChance;
        MobEffect effect;

        this.hydrationIconNumber = Math.min((int) Math.ceil(Math.abs(hydration) / 2f), 10);

        if (Config.Baked.thirstSaturationDisplayed) {
            this.saturationIconNumber = Math.min((int) Math.ceil(Math.abs(saturation) / 2f), 10);
        } else {
            this.saturationIconNumber = 0;
        }

        this.chanceIconNumber = 0;
        //  show chance Effect bar only if chance is > 0 + effect not null
        //  If chance = 100%, instead change hydration color bar (if hydration > 0)
        if (effectChance > 0.0f && !Objects.equals(effectName, "")) {
            effect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(effectName));
            if ((hydrationIconNumber == 0 || effectChance < 1.0f) && effect != null)
                chanceIconNumber = 5;
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
        // Effect bar
        if (chanceIconNumber > 0)
            height += 10;

        return height;
    }

    @Override
    public int getWidth(Font font) {
        return Math.max(hydrationIconNumber, Math.max(saturationIconNumber, chanceIconNumber)) * THIRST_TEXTURE_WIDTH;
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
        if (this.hydration >= 0) {
            xOffsetTexture = THIRST_TEXTURE_WIDTH;
            // Show the thirst bar dirty if dirty chance 100%
            if (effectChance >= 1.0f) {
                xOffsetTexture += THIRST_TEXTURE_WIDTH * 3;
            }
        } else {
            xOffsetTexture = THIRST_TEXTURE_WIDTH * 10;
            // Show the thirst bar dirty if dirty chance 100%
            if (effectChance >= 1.0f) {
                xOffsetTexture += THIRST_TEXTURE_WIDTH * 2;
            }
        }

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
            // Show the thirst bar dirty if dirty chance 100%
            if (effectChance >= 1.0f)
                xOffsetTexture += THIRST_TEXTURE_WIDTH * 2;
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

        // Chance bar
        if (chanceIconNumber > 0)
            top += 10;

        left = tooltipX + (chanceIconNumber - 1) * THIRST_TEXTURE_WIDTH;

        xOffsetTexture = THIRST_TEXTURE_WIDTH * 3;
        // Draw the dirty bubbles
        for (int i = 0; i < chanceIconNumber; i++) {
            int halfIcon = i * 2 + 1;
            int x = left - i * THIRST_TEXTURE_WIDTH;
            int y = top;

            if (halfIcon < (int) (effectChance * 10)) { // Full dirty icon
                gui.blit(ICONS, x, y, xOffsetTexture + THIRST_TEXTURE_WIDTH, 0, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT, 256, 256);
            } else if (halfIcon == (int) (effectChance * 10)) { // Half dirty icon
                gui.blit(ICONS, x, y, xOffsetTexture + (THIRST_TEXTURE_WIDTH * 2), 0, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT, 256, 256);
            } else {
                gui.blit(ICONS, x, y, xOffsetTexture, 0, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT, 256, 256);
            }
        }

        RenderSystem.disableBlend();

        // reset to drawHoveringText state
        RenderSystem.disableDepthTest();
    }
}
