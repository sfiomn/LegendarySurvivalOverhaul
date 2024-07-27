package sfiomn.legendarysurvivaloverhaul.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sfiomn.legendarysurvivaloverhaul.client.shaders.FocusShader;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class RenderThirstOverlay {

    private static FocusShader focusShader;
    private static final float DEFAULT_SHADER_INTENSITY = 0;
    private static final float MAX_SHADER_INTENSITY = 4;
    private static final float SHADER_INTENSITY_STEP = 0.05f;
    private static final int HYDRATION_LEVEL_MIN_EFFECT = 6;
    private static final int HYDRATION_LEVEL_MAX_EFFECT = 2;
    private static float shaderIntensity = 0;
    private static int updateTimer = 0;

    public static void render()
    {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player != null) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            drawThirstEffect(player);

            Minecraft.getInstance().getMainRenderTarget().bindWrite(true);
            RenderSystem.disableBlend();
        }
    }

    public static void drawThirstEffect(PlayerEntity player) {
        if (focusShader == null)
            return;

        if ((player.isSpectator() || player.isCreative())) {
            focusShader.stopRender();
        } else if (!(Minecraft.getInstance().screen instanceof DeathScreen)) {
            focusShader.render();
        }
    }

    public static void updateThirstEffect(@Nullable PlayerEntity player) {
        if (focusShader == null) {
            focusShader = new FocusShader();
        }

        float targetShaderIntensity = DEFAULT_SHADER_INTENSITY;
        if (player != null && player.isAlive()) {

            ThirstCapability thirstCap = CapabilityUtil.getThirstCapability(player);
            // hydration is 0 - 20
            int hydration = thirstCap.getHydrationLevel();

            if (hydration <= HYDRATION_LEVEL_MIN_EFFECT) {
                targetShaderIntensity = (1 - ((float) (hydration - HYDRATION_LEVEL_MAX_EFFECT) / (float) (HYDRATION_LEVEL_MIN_EFFECT - HYDRATION_LEVEL_MAX_EFFECT))) * MAX_SHADER_INTENSITY;
            }

            if (updateTimer++ % 2 == 0) {
                if (targetShaderIntensity > shaderIntensity) {
                    shaderIntensity = Math.min(shaderIntensity + SHADER_INTENSITY_STEP, targetShaderIntensity);
                } else if (targetShaderIntensity < shaderIntensity) {
                    shaderIntensity = Math.max(shaderIntensity - SHADER_INTENSITY_STEP, targetShaderIntensity);
                }
                if (focusShader.intensity != shaderIntensity)
                    focusShader.updateIntensity(shaderIntensity);
            }
        }
    }
}
