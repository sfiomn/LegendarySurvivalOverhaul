package sfiomn.legendarysurvivaloverhaul.client.shaders;

import com.google.gson.JsonSyntaxException;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ModShaderGroup extends ShaderGroup {
    public ModShaderGroup(TextureManager p_i1050_1_, IResourceManager p_i1050_2_, Framebuffer p_i1050_3_, ResourceLocation p_i1050_4_) throws IOException, JsonSyntaxException {
        super(p_i1050_1_, p_i1050_2_, p_i1050_3_, p_i1050_4_);
    }

    private static Field shaders = ObfuscationReflectionHelper.findField(ShaderGroup.class, "field_148031_d");

    public List<Shader> getShaders() {
        try {
            return (List<Shader>) shaders.get(this);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            return new ArrayList<>();
        }
    }
}
