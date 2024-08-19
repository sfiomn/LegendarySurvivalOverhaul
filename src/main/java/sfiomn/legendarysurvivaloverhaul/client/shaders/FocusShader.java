package sfiomn.legendarysurvivaloverhaul.client.shaders;

import com.mojang.blaze3d.shaders.Uniform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

import java.lang.reflect.Field;
import java.util.List;

public class FocusShader {
    public static final ResourceLocation BLUR_SHADER = new ResourceLocation("shaders/post/blobs2.json");
    private static final Field shaders = ObfuscationReflectionHelper.findField(PostChain.class, "f_110009_");

    public FocusShader() {}

    public void render(float intensity) {
        PostChain currentEffect = Minecraft.getInstance().gameRenderer.currentEffect();
        if (intensity > 0) {
            if (currentEffect == null ||
                    !currentEffect.getName().equals("minecraft:shaders/post/blobs2.json")) {
                try {
                    Minecraft.getInstance().gameRenderer.loadEffect(BLUR_SHADER);
                } catch (NullPointerException e) {
                    return;
                }
            }
            updateIntensity(intensity);
        } else if (intensity == 0) {
            stopRender();
        }
    }

    public void stopRender() {
        PostChain currentEffect = Minecraft.getInstance().gameRenderer.currentEffect();
        if (currentEffect != null &&
                currentEffect.getName().equals("minecraft:shaders/post/blobs2.json")) {
            Minecraft.getInstance().gameRenderer.shutdownEffect();
        }
    }

    @OnlyIn(value = Dist.CLIENT)
    public void updateIntensity(float intensity) {

        Uniform shaderRadius;
        try {
            shaderRadius = ((List<PostPass>) shaders.get(Minecraft.getInstance().gameRenderer.currentEffect())).get(0).getEffect().getUniform("Radius");
        } catch (IllegalArgumentException | IllegalAccessException | NullPointerException e) {
            shaderRadius = null;
        }

        if (shaderRadius != null) {
            shaderRadius.set(intensity);
        }
    }
}
