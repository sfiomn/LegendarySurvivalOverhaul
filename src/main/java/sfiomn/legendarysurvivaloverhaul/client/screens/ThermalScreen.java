package sfiomn.legendarysurvivaloverhaul.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.common.containers.AbstractThermalContainer;

@OnlyIn(Dist.CLIENT)
public class ThermalScreen extends AbstractContainerScreen<AbstractThermalContainer> {
    public static final ResourceLocation HEATER_SCREEN = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/heater_screen.png");
    public static final ResourceLocation COOLER_SCREEN = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/cooler_screen.png");
    public static final int FUEL_LEVEL_WIDTH = 29;
    public static final int FUEL_LEVEL_HEIGHT = 27;

    public ThermalScreen(AbstractThermalContainer screenContainer, Inventory playerInventory, Component titleIn) {
        super(screenContainer, playerInventory, titleIn);
    }

    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;
    }

    @Override
    public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
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

        ResourceLocation TEXTURE;
        if (menu.getThermalType() == ThermalTypeEnum.HEATING) {
            TEXTURE = HEATER_SCREEN;
        } else {
            TEXTURE = COOLER_SCREEN;
        }

        RenderSystem.setShaderTexture(0, TEXTURE);
        gui.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        // Fuel level
        int fuelTimeHeight = (int) (menu.getFuelTimeScale() * FUEL_LEVEL_HEIGHT);
        gui.blit(TEXTURE, this.leftPos + 73, this.topPos + 35 + (FUEL_LEVEL_HEIGHT - fuelTimeHeight), 176, 24 + (FUEL_LEVEL_HEIGHT - fuelTimeHeight), FUEL_LEVEL_WIDTH, fuelTimeHeight);

        // Powered status
        if (menu.isPowered())
            gui.blit(TEXTURE, this.leftPos + 137, this.topPos + 37, 176, 0, 12, 24);
    }
}
