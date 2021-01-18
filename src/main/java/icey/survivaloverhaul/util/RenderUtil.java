package icey.survivaloverhaul.util;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.vector.Matrix4f;

public class RenderUtil
{
	// Basically a more sensibly-named version of Minecraft's included blit function
	
	public static void drawTexturedModelRect(Matrix4f matrix, float x, float y, int texX, int texY, int width, int height)
	{
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
