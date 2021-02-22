package icey.survivaloverhaul.util;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.vector.Matrix4f;

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
	 * @param width Width of the given texture, in pixels
	 * @param height Height of the given texture, in pixels
	 */
	public static void drawTexturedModelRect(Matrix4f matrix, float x, float y, int texX, int texY, int width, int height)
	{
		// These are some weirdly specific constants but I'm too scared to change them
		
		float f = 0.00390625f;
		float f1 = 0.00390625f;
		float z = 0.0f;
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		bufferBuilder.pos(matrix, x, y + height, z)
				.tex((texX * f), (texY + height) * f1).endVertex();
		bufferBuilder.pos(matrix, (x + width), y + height, z)
				.tex((texX + width) * f, (texY + height) * f1).endVertex();
		bufferBuilder.pos(matrix, (x + width), y, z)
				.tex((texX + width) * f,(texY * f1)).endVertex();
		bufferBuilder.pos(matrix, x, y, z)
				.tex((texX * f), (texY * f1)).endVertex();
		tessellator.draw();
	}
}
