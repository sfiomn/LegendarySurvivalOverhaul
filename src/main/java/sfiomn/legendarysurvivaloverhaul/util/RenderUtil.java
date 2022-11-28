package sfiomn.legendarysurvivaloverhaul.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.opengl.GL11;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

public final class RenderUtil
{
	public static final ResourceLocation legendaryFrame = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/legendary_frame.png");

	private static final int legendarySmallFrameOffsetIndex = 0;
	private static final int legendarySmallFrameWidth = 38;
	private static final int legendaryMediumFrameOffsetIndex = 1;
	private static final int legendaryMediumFrameWidth = 92;
	private static final int legendaryLargeFrameOffsetIndex = 2;
	private static final int legendaryLargeFrameWidth = 122;
	private static final int legendaryFrameHeight = 30;
	private static final int legendaryFrameTextureSize = 128;


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

	public static void drawLegendaryFrame(Minecraft mc, MatrixStack matrix, StringTextComponent text) {
		int width = mc.getWindow().getGuiScaledWidth();
		int height = mc.getWindow().getGuiScaledHeight();

		int textWidth = mc.font.width(text);
		int white = 0xFFFFFF;
		int y = height / 2 + legendaryFrameHeight / 2;

		mc.getTextureManager().bind(legendaryFrame);
		RenderSystem.enableBlend();

		if (textWidth < 27) {
			int x = width / 2 - legendarySmallFrameWidth / 2;
			AbstractGui.blit(matrix, x, y, 0, legendarySmallFrameOffsetIndex * legendaryFrameHeight, legendaryLargeFrameWidth, legendaryFrameHeight, legendaryFrameTextureSize, legendaryFrameTextureSize);
		} else if (textWidth < 81) {
			int x = width / 2 - legendaryMediumFrameWidth / 2;
			AbstractGui.blit(matrix, x, y, 0, legendaryMediumFrameOffsetIndex * legendaryFrameHeight, legendaryMediumFrameWidth, legendaryFrameHeight, legendaryFrameTextureSize, legendaryFrameTextureSize);
		} else {
			int x = width / 2 - legendaryLargeFrameWidth / 2;
			AbstractGui.blit(matrix, x, y, 0, legendaryLargeFrameOffsetIndex * legendaryFrameHeight, legendaryLargeFrameWidth, legendaryFrameHeight, legendaryFrameTextureSize, legendaryFrameTextureSize);
		}

		mc.font.draw(matrix, text, (float) width / 2 - (float) textWidth / 2, y + (float) legendaryFrameHeight / 2 - 4, white);
		RenderSystem.disableBlend();
	}
}
