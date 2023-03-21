package sfiomn.legendarysurvivaloverhaul.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.StringTextComponent;
import sfiomn.legendarysurvivaloverhaul.util.RenderUtil;

public class TooltipFrame {
    private final Minecraft mc;
    private final Entity entityLookedAt;
    private final MatrixStack matrixStack;
    private StringTextComponent textToShow;

    public TooltipFrame(Minecraft mc, MatrixStack matrixStack, Entity entityLookedAt) {
        this.mc = mc;
        this.entityLookedAt = entityLookedAt;
        this.matrixStack = matrixStack;
    }

    public boolean hasSameEntity(Entity entityIn) {
        return this.entityLookedAt.equals(entityIn);
    }

    public boolean hasText() {
        return this.textToShow!=null && this.textToShow.getText().length() > 0;
    }

    public void setText(StringTextComponent text) {
        this.textToShow = text;
    }

    public void drawFrame() {
        RenderUtil.drawFrame(this.mc, this.matrixStack, this.textToShow);
    }
}
