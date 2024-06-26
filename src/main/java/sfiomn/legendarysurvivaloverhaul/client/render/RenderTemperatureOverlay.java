package sfiomn.legendarysurvivaloverhaul.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.common.effects.FrostbiteEffect;
import sfiomn.legendarysurvivaloverhaul.common.effects.HeatStrokeEffect;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;

@OnlyIn(Dist.CLIENT)
public class RenderTemperatureOverlay {
    private static final ResourceLocation FROSTBITE_EFFECT = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/freeze_effect.png");
    private static final ResourceLocation HEAT_STROKE_EFFECT = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/heat_effect.png");
    private static final int TEMPERATURE_EFFECT_WIDTH = 256;
    private	static final int TEMPERATURE_EFFECT_HEIGHT = 256;
    private static ResourceLocation temperatureEffect = null;
    private static float fadeLevel = 0;
    private static boolean triggerTemperatureSoundEffect;

    public static void render(GuiGraphics gui, int width, int height)
    {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.clearColor(1.0F, 1.0F, 1.0F, fadeLevel);

        drawTemperatureEffect(gui, width, height);
        RenderSystem.clearColor(1.0F,1.0F,1.0F,1.0F);

        RenderSystem.disableBlend();
    }

    public static void drawTemperatureEffect(GuiGraphics gui, int width, int height) {
        if (temperatureEffect != null) {
            gui.blit(temperatureEffect, 0, 0, width, height, 0, 0, TEMPERATURE_EFFECT_WIDTH, TEMPERATURE_EFFECT_HEIGHT);
        }
    }

    public static void updateTemperatureEffect(Player player) {
        if (player != null && player.isAlive()) {

            float temperature = CapabilityUtil.getTempCapability(player).getTemperatureLevel();
            TemperatureEnum tempEnum = CapabilityUtil.getTempCapability(player).getTemperatureEnum();

            boolean frostbiteLimit = temperature <= TemperatureEnum.FROSTBITE.getMiddle() + 1;
            boolean heatstrokeLimit = temperature >= TemperatureEnum.HEAT_STROKE.getMiddle() - 1;

            float targetFadeLevel;
            if (tempEnum == TemperatureEnum.FROSTBITE && !FrostbiteEffect.playerIsImmuneToFrost(player)) {
                temperatureEffect = FROSTBITE_EFFECT;
                if (frostbiteLimit) {
                    targetFadeLevel = 0.65f;
                    if (triggerTemperatureSoundEffect) {
                        triggerTemperatureSoundEffect = false;
                        player.playSound(SoundRegistry.FROSTBITE.get(), 1.0f, 1.0f);
                    }
                } else {
                    targetFadeLevel = 0.35f;
                    triggerTemperatureSoundEffect = true;
                    if (fadeLevel == 0) {
                        player.playSound(SoundRegistry.FROSTBITE_EARLY.get(), 1.0f, 1.0f);
                    }
                }
            } else if (tempEnum == TemperatureEnum.HEAT_STROKE && !HeatStrokeEffect.playerIsImmuneToHeat(player)) {
                temperatureEffect = HEAT_STROKE_EFFECT;

                if (heatstrokeLimit) {
                    targetFadeLevel = 0.5f;
                    if (triggerTemperatureSoundEffect) {
                        triggerTemperatureSoundEffect = false;
                        player.playSound(SoundRegistry.HEAT_STROKE.get(), 1.0f, 1.0f);
                    }
                } else {
                    targetFadeLevel = 0.25f;
                    triggerTemperatureSoundEffect = true;
                    if (fadeLevel == 0) {
                        player.playSound(SoundRegistry.HEAT_STROKE_EARLY.get(), 1.0f, 1.0f);
                    }
                }
            } else {
                triggerTemperatureSoundEffect = true;
                targetFadeLevel = 0;
            }

            if (targetFadeLevel > fadeLevel) {
                fadeLevel = Math.min(targetFadeLevel, fadeLevel + MathUtil.round(1.0f / 20.0f, 2));
            }
            if (targetFadeLevel < fadeLevel) {
                fadeLevel = Math.max(targetFadeLevel, fadeLevel - MathUtil.round(1.0f / 20.0f, 2));
            }

            if (fadeLevel == 0) {
                temperatureEffect = null;
            }
        }
    }
}
