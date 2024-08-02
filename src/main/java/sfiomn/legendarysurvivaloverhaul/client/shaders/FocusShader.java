package sfiomn.legendarysurvivaloverhaul.client.shaders;

import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.List;

public class FocusShader {
    public static final ResourceLocation BLUR_SHADER = new ResourceLocation("shaders/post/blobs2.json");
    private static final Field shaders = ObfuscationReflectionHelper.findField(ShaderGroup.class, "field_148031_d");

    public FocusShader() {}

    public void render(float intensity) {
        ShaderGroup currentEffect = Minecraft.getInstance().gameRenderer.currentEffect();
        if (intensity > 0) {
            if (currentEffect == null ||
                    !currentEffect.getName().equals("minecraft:shaders/post/blobs2.json")) {
                Minecraft.getInstance().gameRenderer.loadEffect(BLUR_SHADER);
            }
            updateIntensity(intensity);
        } else if (intensity == 0) {
            stopRender();
        }

    }

    public void stopRender() {
        ShaderGroup currentEffect = Minecraft.getInstance().gameRenderer.currentEffect();
        if (currentEffect != null &&
                currentEffect.getName().equals("minecraft:shaders/post/blobs2.json")) {
            Minecraft.getInstance().gameRenderer.shutdownEffect();
        }
    }

    @OnlyIn(value = Dist.CLIENT)
    public void updateIntensity(float intensity) {
        ShaderUniform shaderRadius;
        try {
            shaderRadius = ((List<Shader>) shaders.get(Minecraft.getInstance().gameRenderer.currentEffect())).get(0).getEffect().getUniform("Radius");
        } catch (IllegalArgumentException | IllegalAccessException e) {
            shaderRadius = null;
        }

        if (shaderRadius != null) {
            shaderRadius.set(intensity);
        }
    }
}
