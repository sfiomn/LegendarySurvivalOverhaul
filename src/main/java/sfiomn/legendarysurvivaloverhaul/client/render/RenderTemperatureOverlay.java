package sfiomn.legendarysurvivaloverhaul.client.render;

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
public class RenderTemperatureOverlay {
    private static final ResourceLocation FROSTBITE_EFFECT = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/freeze_effect.png");
    private static final ResourceLocation HEAT_STROKE_EFFECT = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/heat_effect.png");
    private static final int TEMPERATURE_EFFECT_WIDTH = 256;
    private	static final int TEMPERATURE_EFFECT_HEIGHT = 256;
    private static ResourceLocation temperatureEffect = null;
    private static float fadeLevel = 0;
    private static int updateTimer = 0;
    private static int lastTemperatureEffect = 0;

    public static void render(MatrixStack matrix, int width, int height)
    {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        drawTemperatureEffect(matrix, width, height);

        RenderSystem.disableBlend();
        bind(AbstractGui.GUI_ICONS_LOCATION);
    }

    public static void drawTemperatureEffect(MatrixStack matrix, int width, int height) {

        Matrix4f m4f = matrix.last().pose();

        if (temperatureEffect != null) {
            bind(temperatureEffect);
            RenderUtil.drawTexturedModelRectWithAlpha(m4f, 0, 0, width, height, 0, 0, TEMPERATURE_EFFECT_WIDTH, TEMPERATURE_EFFECT_HEIGHT, fadeLevel);
        }
    }

    public static void updateTemperatureEffect(PlayerEntity player) {
        if (player != null && player.isAlive()) {

            float temperature = CapabilityUtil.getTempCapability(player).getTemperatureLevel();

            boolean frostbiteLimit = temperature <= TemperatureEnum.FROSTBITE.getMiddle() + 1 && !FrostbiteEffect.playerIsImmuneToFrost(player);
            boolean heatstrokeLimit = temperature >= TemperatureEnum.HEAT_STROKE.getMiddle() && !HeatStrokeEffect.playerIsImmuneToHeat(player);

            float targetFadeLevel = 0;
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

            if (updateTimer++ % 2 == 0) {
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
                    temperatureEffect = FROSTBITE_EFFECT;
                } else if (heatstrokeLimit || lastTemperatureEffect == 2) {
                    lastTemperatureEffect = 2;
                    temperatureEffect = HEAT_STROKE_EFFECT;
                }
            } else {
                temperatureEffect = null;
                lastTemperatureEffect = 0;
            }
        }
        updateTimer++;
    }

    private static void bind(ResourceLocation resource)
    {
        Minecraft.getInstance().getTextureManager().bind(resource);
    }
}
