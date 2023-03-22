package sfiomn.legendarysurvivaloverhaul.client.screens;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.GuiUtils;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.containers.SewingTableContainer;
import sfiomn.legendarysurvivaloverhaul.common.items.CoatItem;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class SewingTableScreen extends ContainerScreen<SewingTableContainer> {
    public static final ResourceLocation SEWING_TABLE_SCREEN = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/sewing_table_screen.png");
    Rectangle2d craftEnabledAre;

    public SewingTableScreen(SewingTableContainer screenContainer, PlayerInventory playerInventory, ITextComponent titleIn) {
        super(screenContainer, playerInventory, titleIn);
    }

    @Override
    protected void init() {
        super.init();
        craftEnabledAre = new Rectangle2d(this.leftPos + 93, this.topPos + 41, 13, 13);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        if (minecraft == null) {
            return;
        }

        RenderSystem.color4f(1, 1, 1, 1);
        minecraft.getTextureManager().bind(SEWING_TABLE_SCREEN);

        blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        if (this.menu.getSlot(0).getItem().getItem() instanceof ArmorItem &&
                this.menu.getSlot(1).getItem().getItem() instanceof CoatItem &&
                !this.menu.getSlot(2).hasItem()) {
            blit(matrixStack, this.leftPos + 93, this.topPos + 41, 176, 0, 13, 13);
        }
    }

    @Override
    protected void renderTooltip(MatrixStack matrixStack, int mouseX, int mouseY) {
        super.renderTooltip(matrixStack, mouseX, mouseY);

        if (Minecraft.getInstance().player == null) return;
        if (Minecraft.getInstance().screen == null) return;

        if (Minecraft.getInstance().player.inventory.getCarried().isEmpty() && craftEnabledAre.contains(mouseX, mouseY)) {
            if (this.menu.getSlot(0).getItem().getItem() instanceof ArmorItem &&
                    this.menu.getSlot(1).getItem().getItem() instanceof CoatItem &&
                    !this.menu.getSlot(2).hasItem()) {

                List<ITextComponent> list = Lists.newArrayList();
                TranslationTextComponent tooltipText = new TranslationTextComponent("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".sewing_table_disabled");
                IFormattableTextComponent iformattabletextcomponent = (new StringTextComponent("")).append(tooltipText);
                list.add(iformattabletextcomponent);

                GuiUtils.drawHoveringText(matrixStack, list, mouseX, mouseY, Minecraft.getInstance().screen.width, Minecraft.getInstance().screen.height, Minecraft.getInstance().font.width(tooltipText), Minecraft.getInstance().font);
            }
        }
    }
}
