package sfiomn.legendarysurvivaloverhaul.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.common.ForgeMod;
import sereneseasons.api.SSItems;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureItemCapability;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;
import sfiomn.legendarysurvivaloverhaul.util.WorldUtil;

import static sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsUtil.seasonTooltip;
import static sfiomn.legendarysurvivaloverhaul.util.WorldUtil.timeInGame;

public class RenderTooltipFrame {
    public static final ResourceLocation ICONS = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/overlay.png");

    private static final int FRAME_HEIGHT = 20;
    private static final int FRAME_WIDTH = 122;
    private static final int LEFT_SIDE_FRAME_X_OFFSET = 0;
    private static final int LEFT_SIDE_FRAME_WIDTH = 6;
    private static final int MIDDLE_SIDE_FRAME_X_OFFSET = 4;

    private static final int RIGHT_SIDE_FRAME_WIDTH = LEFT_SIDE_FRAME_WIDTH;
    private static final int MIDDLE_SIDE_FRAME_MAX_WIDTH = FRAME_WIDTH - (MIDDLE_SIDE_FRAME_X_OFFSET * 2);
    private static final int RIGHT_SIDE_FRAME_X_OFFSET = FRAME_WIDTH - RIGHT_SIDE_FRAME_WIDTH;

    private static Entity ENTITY_LOOKED_AT = null;

    public static IGuiOverlay TOOLTIP_ITEM_FRAME = (forgeGui, guiGraphics, partialTicks, width, height) -> {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            if (ENTITY_LOOKED_AT == null || player.tickCount % 4 == 0) {
                ENTITY_LOOKED_AT = WorldUtil.getEntityLookedAt(player, player.getAttributeValue(ForgeMod.ENTITY_REACH.get()));
            }
        }

        if (ENTITY_LOOKED_AT instanceof ItemFrame && !((ItemFrame) ENTITY_LOOKED_AT).getItem().isEmpty()) {
            Item itemInFrame = ((ItemFrame) ENTITY_LOOKED_AT).getItem().getItem();

            if (LegendarySurvivalOverhaul.sereneSeasonsLoaded && (itemInFrame == SSItems.CALENDAR.get() || itemInFrame == ItemRegistry.SEASONAL_CALENDAR.get())) {
                render(forgeGui, guiGraphics, width, height, seasonTooltip(ENTITY_LOOKED_AT.blockPosition(), ENTITY_LOOKED_AT.level()));
            } else if (itemInFrame == ItemRegistry.THERMOMETER.get()) {
                TemperatureItemCapability tempItemCap = CapabilityUtil.getTempItemCapability(((ItemFrame) ENTITY_LOOKED_AT).getItem());
                render(forgeGui, guiGraphics, width, height, Component.literal(tempItemCap.getWorldTemperatureLevel() + "\u00B0C"));
            } else if (itemInFrame == Items.COMPASS) {
                render(forgeGui, guiGraphics, width, height, Component.literal("XYZ: " + ENTITY_LOOKED_AT.blockPosition().getX() +
                        " / " + ENTITY_LOOKED_AT.blockPosition().getY() + " / " + ENTITY_LOOKED_AT.blockPosition().getZ()));
            } else if (itemInFrame == Items.CLOCK) {
                render(forgeGui, guiGraphics, width, height, Component.literal(timeInGame(Minecraft.getInstance())));
            }
        }
    };

    public static void render(ForgeGui forgeGui, GuiGraphics guiGraphics, int width, int height, Component text) {
        forgeGui.setupOverlayRenderState(true, false);

        Minecraft.getInstance().getProfiler().push("tooltip_frame");
        drawTooltipInFrame(guiGraphics, width, height, text);
        Minecraft.getInstance().getProfiler().pop();
    }

    public static void drawTooltipInFrame(GuiGraphics gui, int width, int height, Component text) {
        int textWidth = Minecraft.getInstance().font.width(text);
        int white = 0xFFFFFF;
        int left = width / 2 - textWidth / 2 - LEFT_SIDE_FRAME_WIDTH;
        int top = height / 2 + FRAME_HEIGHT / 2;

        int x = left;

        // Left side frame
        gui.blit(ICONS, x, top, LEFT_SIDE_FRAME_X_OFFSET, 18, LEFT_SIDE_FRAME_WIDTH, FRAME_HEIGHT);

        // Middle side frame
        int remainingTextWidth = textWidth;
        x = left + LEFT_SIDE_FRAME_WIDTH;
        while (remainingTextWidth > 0) {
            int renderedWidth = Math.min(remainingTextWidth, MIDDLE_SIDE_FRAME_MAX_WIDTH);
            gui.blit(ICONS, x, top, MIDDLE_SIDE_FRAME_X_OFFSET, 18, renderedWidth, FRAME_HEIGHT);

            remainingTextWidth -= renderedWidth;
            x += renderedWidth;
        }

        x = left + LEFT_SIDE_FRAME_WIDTH + textWidth;
        // Left side frame
        gui.blit(ICONS, x, top, RIGHT_SIDE_FRAME_X_OFFSET, 18, RIGHT_SIDE_FRAME_WIDTH, FRAME_HEIGHT);

        gui.drawCenteredString(Minecraft.getInstance().font, text, width / 2, top + FRAME_HEIGHT / 2 - 4, white);
    }
}
