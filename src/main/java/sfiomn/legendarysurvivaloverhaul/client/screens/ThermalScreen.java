package sfiomn.legendarysurvivaloverhaul.client.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.common.containers.AbstractThermalContainer;

@OnlyIn(Dist.CLIENT)
public class ThermalScreen extends ContainerScreen<AbstractThermalContainer> {
    public static final ResourceLocation HEATER_SCREEN = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/heater_screen.png");
    public static final ResourceLocation COOLER_SCREEN = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/cooler_screen.png");
    public static final int FUEL_LEVEL_HEIGHT = 27;
    public static final int FUEL_LEVEL_WIDTH = 29;

    public ThermalScreen(AbstractThermalContainer screenContainer, PlayerInventory playerInventory, ITextComponent titleIn) {
        super(screenContainer, playerInventory, titleIn);
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
        if (menu.getThermalType() == ThermalTypeEnum.HEATING) {
            minecraft.getTextureManager().bind(HEATER_SCREEN);
        } else {
            minecraft.getTextureManager().bind(COOLER_SCREEN);
        }

        blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        // Fuel level
        int fuelTimeHeight = (int) (menu.getFuelTimeScale() * FUEL_LEVEL_HEIGHT);
        blit(matrixStack, this.leftPos + 73, this.topPos + 35 + (FUEL_LEVEL_HEIGHT - fuelTimeHeight), 175, 12 + (FUEL_LEVEL_HEIGHT - fuelTimeHeight), FUEL_LEVEL_WIDTH, fuelTimeHeight);

        // Powered status
        if (menu.isPowered())
            blit(matrixStack, this.leftPos + 136, this.topPos + 45, 175, 0, 12, 12);
    }
}
