package sfiomn.legendarysurvivaloverhaul.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public final class RenderUtil
{
	private RenderUtil() {}
	
	/**
	 * Basically a more sensibly-named version of Minecraft's included blit function
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
		// These are some weirdly specific constants but I'm too scared to change them
		
		float f = 0.00390625f;
		float f1 = 0.00390625f;
		float z = 0.0f;

		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder bufferBuilder = tesselator.getBuilder();
		
		bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
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

	public static void drawTexturedModelRectWithAlpha(Matrix4f matrix, float x, float y, int width, int height, int texX, int texY, int texWidth, int texHeight, float alpha)
	{

		RenderSystem.disableColorLogicOp();

		RenderSystem.clearColor(1.0F, 1.0F, 1.0F, alpha);

		RenderUtil.drawTexturedModelRect(matrix, x, y, width, height, texX, texY, texWidth, texHeight);
		RenderSystem.clearColor(1.0F,1.0F,1.0F,1.0F);

		RenderSystem.enableColorLogicOp();
	}
}
