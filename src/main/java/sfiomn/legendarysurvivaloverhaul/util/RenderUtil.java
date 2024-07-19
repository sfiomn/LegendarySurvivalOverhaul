package sfiomn.legendarysurvivaloverhaul.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public final class RenderUtil
{
	private RenderUtil() {}
	
	/**
	 * Basically a more sensibly-named version of Minecraft's included blit function
	 * Useful to specify different texWidth and width as the corresponded blit function is protected
	 * @param matrix The matrix this should be drawn from
	 * @param x Horizontal position on the screen where this texture should be drawn 
	 * @param y Vertical position on the screen where this texture should be drawn
	 * @param texX Horizontal position of the texture on the currently bound texture sheet.
	 * @param texY Vertical position of the texture on the currently bound texture sheet.
	 * @param texWidth Width of the given texture, in pixels
	 * @param texHeight Height of the given texture, in pixels
	 */
	public static void drawTexturedModelRect(Matrix4f matrix, float x, float y, int width, int height, int texX, int texY, int texWidth, int texHeight)
	{
		// f & f1 means 1/256 as we assume the image size to be 256x256
		float f = 0.00390625f;
		float f1 = 0.00390625f;
		float z = 0.0f;

		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder bufferBuilder = tesselator.getBuilder();
		
		bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
		bufferBuilder.vertex(matrix, x, y + height, z)
				.uv((texX * f), (texY + texHeight) * f1).color(255, 255, 255, 122).endVertex();
		bufferBuilder.vertex(matrix, (x + width), y + height, z)
				.uv((texX + texWidth) * f, (texY + texHeight) * f1).color(255, 255, 255, 122).endVertex();
		bufferBuilder.vertex(matrix, (x + width), y, z)
				.uv((texX + texWidth) * f,(texY * f1)).color(255, 255, 255, 122).endVertex();
		bufferBuilder.vertex(matrix, x, y, z)
				.uv((texX * f), (texY * f1)).color(255, 255, 255, 255).endVertex();
		tesselator.end();
	}

	public static void drawTexturedModelRectWithAlpha(Matrix4f matrix, float x, float y, int width, int height, int texX, int texY, int texWidth, int texHeight, float alpha) {
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);

		RenderUtil.drawTexturedModelRect(matrix, x, y, width, height, texX, texY, texWidth, texHeight);

		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		RenderSystem.setShaderColor(1.0F,1.0F,1.0F,1.0F);
	}

	public static void renderTextureOverlay(GuiGraphics gui, ResourceLocation resourceLocation, int screenWidth, int screenHeight, float alpha) {
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		gui.setColor(1.0F, 1.0F, 1.0F, alpha);

		gui.blit(resourceLocation, 0, 0, -90, 0.0F, 0.0F, screenWidth, screenHeight, screenWidth, screenHeight);

		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		gui.setColor(1.0F, 1.0F, 1.0F, 1.0F);
	}
}
