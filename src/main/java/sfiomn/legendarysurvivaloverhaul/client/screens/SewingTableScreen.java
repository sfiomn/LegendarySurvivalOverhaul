package sfiomn.legendarysurvivaloverhaul.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.containers.SewingTableContainer;

@OnlyIn(Dist.CLIENT)
public class SewingTableScreen extends AbstractContainerScreen<SewingTableContainer> {
    public static final ResourceLocation SEWING_TABLE_SCREEN = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/sewing_table_screen.png");
    Rect2i craftDisabledArea;

    public SewingTableScreen(SewingTableContainer screenContainer, Inventory playerInventory, Component titleIn) {
        super(screenContainer, playerInventory, titleIn);
    }

    @Override
    protected void init() {
        super.init();
        craftDisabledArea = new Rect2i(this.leftPos + 93, this.topPos + 41, 13, 13);
    }

    @Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(gui);
        super.render(gui, mouseX, mouseY, partialTicks);
        this.renderTooltip(gui, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics gui, float partialTicks, int x, int y) {
        if (minecraft == null) {
            return;
        }

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        gui.blit(SEWING_TABLE_SCREEN, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        if (this.menu.getSlot(0).getItem().getItem() instanceof ArmorItem &&
                this.menu.getSlot(1).getItem().getItem() instanceof Item &&
                !this.menu.getSlot(2).hasItem()) {
            gui.blit(SEWING_TABLE_SCREEN, this.leftPos + 93, this.topPos + 41, 176, 0, 13, 13);
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics gui, int mouseX, int mouseY) {
        super.renderTooltip(gui, mouseX, mouseY);

        if (Minecraft.getInstance().player == null) return;
        if (Minecraft.getInstance().screen == null) return;

        if (craftDisabledArea.contains(mouseX, mouseY)) {
            if (this.menu.getSlot(0).getItem().getItem() instanceof ArmorItem &&
                    this.menu.getSlot(1).getItem().getItem() instanceof Item &&
                    !this.menu.getSlot(2).hasItem()) {

                Component tooltipText = Component.translatable("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".sewing_table.disabled");
                gui.renderTooltip(Minecraft.getInstance().font, tooltipText, mouseX, mouseY);
            }
        }
    }
}
