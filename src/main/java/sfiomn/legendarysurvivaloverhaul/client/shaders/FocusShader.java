package sfiomn.legendarysurvivaloverhaul.client.shaders;

import com.google.gson.JsonSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;

public class FocusShader {
    public static final ResourceLocation BLUR_SHADER = new ResourceLocation("shaders/post/blobs2.json");
    public float intensity;

    public static ModShaderGroup shaderGroup;
    public FocusShader() {
        this.intensity = 0;
    }

    public static void loadResources() {
        Minecraft mc = Minecraft.getInstance();
        if (shaderGroup != null)
            shaderGroup.close();

        try {
            if (mc.isSameThread()) {
                shaderGroup = new ModShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getMainRenderTarget(), BLUR_SHADER);
                shaderGroup.resize(mc.getWindow().getWidth(), mc.getWindow().getHeight());
            }
        } catch (JsonSyntaxException | IOException e) {}
    }

    public void render(float partialTicks) {
        if (shaderGroup == null)
            loadResources();
        if (shaderGroup != null) {
            updateIntensity(this.intensity);
            shaderGroup.process(partialTicks);
        }
    }

    @OnlyIn(value = Dist.CLIENT)
    public void updateIntensity(float intensity) {
        for (Shader mcShader : shaderGroup.getShaders()) {
            ShaderUniform shaderuniform = mcShader.getEffect().getUniform("Radius");

            if (shaderuniform != null) {
                shaderuniform.set(intensity);
                this.intensity = intensity;
            }
        }
    }

    @OnlyIn(value = Dist.CLIENT)
    public void resize() {
        if (shaderGroup != null)
            shaderGroup.resize(Minecraft.getInstance().getWindow().getWidth(), Minecraft.getInstance().getWindow().getHeight());
    }
}
