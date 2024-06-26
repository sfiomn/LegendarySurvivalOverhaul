package sfiomn.legendarysurvivaloverhaul.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

import java.awt.*;

public class RenderFrame {
    public static final ResourceLocation ICONS = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/overlay.png");

    private static final int FRAME_HEIGHT = 20;
    private static final int FRAME_WIDTH = 122;
    private static final int LEFT_SIDE_FRAME_X_OFFSET = 0;
    private static final int LEFT_SIDE_FRAME_WIDTH = 6;
    private static final int MIDDLE_SIDE_FRAME_X_OFFSET = 4;

    private static final int RIGHT_SIDE_FRAME_WIDTH = LEFT_SIDE_FRAME_WIDTH;
    private static final int MIDDLE_SIDE_FRAME_MAX_WIDTH = FRAME_WIDTH - (MIDDLE_SIDE_FRAME_X_OFFSET * 2);
    private static final int RIGHT_SIDE_FRAME_X_OFFSET = FRAME_WIDTH - RIGHT_SIDE_FRAME_WIDTH;
    private static final int FRAME_TEXTURE_SIZE = 256;

    public static void render(Minecraft mc, GuiGraphics gui, Component text) {
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();

        int textWidth = mc.font.width(text);
        int white = 0xFFFFFF;
        int left = width / 2 - textWidth / 2 - LEFT_SIDE_FRAME_WIDTH;
        int top = height / 2 + FRAME_HEIGHT / 2;

        RenderSystem.enableBlend();

        int y = top;
        int x = left;

        // Left side frame
        gui.blit(ICONS, x, y, LEFT_SIDE_FRAME_X_OFFSET, 18, LEFT_SIDE_FRAME_WIDTH, FRAME_HEIGHT, FRAME_TEXTURE_SIZE, FRAME_TEXTURE_SIZE);

        // Middle side frame
        int remainingTextWidth = textWidth;
        x = left + LEFT_SIDE_FRAME_WIDTH;
        while (remainingTextWidth > 0) {
            int renderedWidth = Math.min(remainingTextWidth, MIDDLE_SIDE_FRAME_MAX_WIDTH);
            gui.blit(ICONS, x, y, MIDDLE_SIDE_FRAME_X_OFFSET, 18, renderedWidth, FRAME_HEIGHT, FRAME_TEXTURE_SIZE, FRAME_TEXTURE_SIZE);

            remainingTextWidth -= renderedWidth;
            x += renderedWidth;
        }

        x = left + LEFT_SIDE_FRAME_WIDTH + textWidth;
        // Left side frame
        gui.blit(ICONS, x, y, RIGHT_SIDE_FRAME_X_OFFSET, 18, RIGHT_SIDE_FRAME_WIDTH, FRAME_HEIGHT, FRAME_TEXTURE_SIZE, FRAME_TEXTURE_SIZE);

        gui.drawCenteredString(mc.font, text, width / 2, FRAME_HEIGHT / 2, white);
        //mc.font.draw(matrix, text, (float) width / 2 - (float) textWidth / 2, y + (float) FRAME_HEIGHT / 2 - 4, white);
        RenderSystem.disableBlend();
    }
}
