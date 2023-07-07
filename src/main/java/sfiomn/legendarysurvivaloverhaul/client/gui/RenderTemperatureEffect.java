package sfiomn.legendarysurvivaloverhaul.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.client.sounds.FrostbiteSound;
import sfiomn.legendarysurvivaloverhaul.client.sounds.HeatStrokeSound;
import sfiomn.legendarysurvivaloverhaul.common.effects.FrostbiteEffect;
import sfiomn.legendarysurvivaloverhaul.common.effects.HeatStrokeEffect;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;
import sfiomn.legendarysurvivaloverhaul.util.RenderUtil;

@OnlyIn(Dist.CLIENT)
public class RenderTemperatureEffect {
    public static final ResourceLocation FROSTBITE_EFFECT = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/freeze_effect.png");
    public static final ResourceLocation HEAT_STROKE_EFFECT = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/heat_effect.png");

    private static final int TEMPERATURE_EFFECT_WIDTH = 256;
    private	static final int TEMPERATURE_EFFECT_HEIGHT = 256;
    public static float fadeLevel = 0;
    public static float targetFadeLevel = 0;
    private static int updateTimer = 0;
    private static int lastTemperatureEffect = 0;

    public static void render(MatrixStack matrix, PlayerEntity player, int width, int height)
    {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        drawTemperatureEffect(matrix, player, width, height);

        RenderSystem.disableBlend();
        bind(AbstractGui.GUI_ICONS_LOCATION);
    }

    public static void drawTemperatureEffect(MatrixStack matrix, PlayerEntity player, int width, int height) {

        float temperature = CapabilityUtil.getTempCapability(player).getTemperatureLevel();

        Matrix4f m4f = matrix.last().pose();

        boolean frostbiteLimit = temperature <= TemperatureEnum.FROSTBITE.getMiddle() + 1 && !FrostbiteEffect.playerIsImmuneToFrost(player);
        boolean heatstrokeLimit = temperature >= TemperatureEnum.HEAT_STROKE.getMiddle() && !HeatStrokeEffect.playerIsImmuneToHeat(player);

        if (frostbiteLimit) {
            targetFadeLevel = 0.5f;
            if (fadeLevel == 0) {
                Minecraft.getInstance().getSoundManager().play(new FrostbiteSound(player));
            }
        } else if (heatstrokeLimit) {
            targetFadeLevel = 0.5f;
            if (fadeLevel == 0) {
                Minecraft.getInstance().getSoundManager().play(new HeatStrokeSound(player));
            }
        }else {
            targetFadeLevel = 0;
        }

        if (updateTimer % 2 == 0) {
            if (Math.abs(targetFadeLevel - fadeLevel) < 0.01f) {
                fadeLevel = targetFadeLevel;
            }
            if (targetFadeLevel != fadeLevel) {
                fadeLevel = (targetFadeLevel + fadeLevel) / 2;
            }
        }

        if (fadeLevel > 0) {
            if (frostbiteLimit || lastTemperatureEffect == 1) {
                lastTemperatureEffect = 1;
                bind(FROSTBITE_EFFECT);
            } else if (heatstrokeLimit || lastTemperatureEffect == 2) {
                lastTemperatureEffect = 2;
                bind(HEAT_STROKE_EFFECT);
            }
            RenderUtil.drawTexturedModelRectWithAlpha(m4f, 0, 0, width, height, 0, 0, TEMPERATURE_EFFECT_WIDTH, TEMPERATURE_EFFECT_HEIGHT, fadeLevel);
        } else {
            lastTemperatureEffect = 0;
        }
    }

    public static void updateTemperatureEffect() {
        updateTimer++;
    }

    private static void bind(ResourceLocation resource)
    {
        Minecraft.getInstance().getTextureManager().bind(resource);
    }
}
