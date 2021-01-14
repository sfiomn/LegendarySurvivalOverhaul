package icey.survivaloverhaul.util;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class RenderUtil
{
	public static void drawTexturedModelRect(Tessellator tessellator, BufferBuilder bufferBuilder, float x, float y, int texX, int texY, int width, int height)
	{
		float f = 0.00390625f;
		float f1 = 0.00390625f;
		double z = 0.0d;
		
		bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferBuilder.pos(x, y + height, z)
				.tex((texX * f), (texY + height) * f1).endVertex();
		bufferBuilder.pos((x + width), y + height, z)
				.tex((texX + width) * f, (texY + height) * f1).endVertex();
		bufferBuilder.pos((x + width), y, z)
				.tex((texX + width) * f,(texY * f1)).endVertex();
		bufferBuilder.pos(x, y, z)
				.tex((texX * f), (texY * f1)).endVertex();
		tessellator.draw();
	}
}
