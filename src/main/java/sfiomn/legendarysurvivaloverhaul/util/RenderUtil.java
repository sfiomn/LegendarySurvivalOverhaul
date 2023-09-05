package sfiomn.legendarysurvivaloverhaul.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.vector.Matrix4f;
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
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuilder();
		
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		bufferBuilder.vertex(matrix, x, y + height, z)
				.uv((texX * f), (texY + texHeight) * f1).color(255, 255, 255, 122).endVertex();
		bufferBuilder.vertex(matrix, (x + width), y + height, z)
				.uv((texX + texWidth) * f, (texY + texHeight) * f1).color(255, 255, 255, 122).endVertex();
		bufferBuilder.vertex(matrix, (x + width), y, z)
				.uv((texX + texWidth) * f,(texY * f1)).color(255, 255, 255, 122).endVertex();
		bufferBuilder.vertex(matrix, x, y, z)
				.uv((texX * f), (texY * f1)).color(255, 255, 255, 255).endVertex();
		tessellator.end();
	}

	public static void drawTexturedModelRectWithAlpha(Matrix4f matrix, float x, float y, int width, int height, int texX, int texY, int texWidth, int texHeight, float alpha)
	{

		RenderSystem.disableAlphaTest();

		RenderSystem.color4f(1.0F, 1.0F, 1.0F, alpha);

		RenderUtil.drawTexturedModelRect(matrix, x, y, width, height, texX, texY, texWidth, texHeight);
		RenderSystem.color4f(1.0F,1.0F,1.0F,1.0F);

		RenderSystem.enableAlphaTest();
	}
}
