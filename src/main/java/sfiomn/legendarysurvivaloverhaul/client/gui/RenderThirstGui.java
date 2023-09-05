package sfiomn.legendarysurvivaloverhaul.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.client.gui.ForgeIngameGui;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.EffectRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;
import sfiomn.legendarysurvivaloverhaul.util.RenderUtil;

import java.util.Random;

public class RenderThirstGui
{
	private static int updateCounter = 0;

	private static final Random rand = new Random();
	
	public static final ResourceLocation ICONS = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/overlay.png");

	// Row position on the overlay sheet
	private static final int THIRST_TEXTURE_POS_Y = 0;
	private static final int THIRST_TEXTURE_POS_X = 0;

	// Dimensions of the icon
	private static final int THIRST_TEXTURE_WIDTH = 9;
	private static final int THIRST_TEXTURE_HEIGHT = 9;

	// Similar to net.minecraftforge.client.ForgeIngameGui.renderFood
	public static void render(MatrixStack matrix, PlayerEntity player, int width, int height)
	{
		rand.setSeed(updateCounter * 445L);

		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();

		bind(ICONS);
		drawHydrationBar(matrix, player, width, height);

		RenderSystem.disableBlend();
		bind(AbstractGui.GUI_ICONS_LOCATION);
	}
	
	public static void drawHydrationBar(MatrixStack matrix, PlayerEntity player, int width, int height)
	{
		ThirstCapability thirstCap = CapabilityUtil.getThirstCapability(player);

		// hydration is 0 - 20
		int hydration = thirstCap.getHydrationLevel();
		float saturation = thirstCap.getSaturationLevel();

		Matrix4f m4f = matrix.last().pose();

		int left = width / 2 + 91; // Same x offset as the hunger bar
		int top = height - ForgeIngameGui.right_height;
		ForgeIngameGui.right_height += 10;

		int saturationInt = (int)saturation;

		// Draw the 10 hydration / saturation bubbles
		for (int i = 0; i < 10; i++)
		{

			int halfIcon = i * 2 + 1;
			int x = left - i * 8 - 9;
			int y = top;

			// Shake based on hydration level and saturation level
			int yOffset = 0;
			if (Config.Baked.showVanillaAnimationOverlay && saturation <= 0.0f && updateCounter % (hydration * 3 + 1) == 0)
			{
				yOffset = (rand.nextInt(3) - 1);
			}

			int xTextureOffset = THIRST_TEXTURE_POS_X;
			int yTextureOffset = THIRST_TEXTURE_POS_Y;
			if (player.hasEffect(EffectRegistry.THIRST.get()))
			{
				xTextureOffset = THIRST_TEXTURE_WIDTH * 3;
			}
			if (player.hasEffect(EffectRegistry.HEAT_Thirst.get())) {
				yTextureOffset = THIRST_TEXTURE_HEIGHT;
			}

			if (halfIcon < hydration) // Full hydration icon
				RenderUtil.drawTexturedModelRect(m4f, x, y + yOffset, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT, xTextureOffset + THIRST_TEXTURE_WIDTH, yTextureOffset, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);
			else if (halfIcon == hydration) // Half hydration icon
				RenderUtil.drawTexturedModelRect(m4f, x, y + yOffset, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT, xTextureOffset + (THIRST_TEXTURE_WIDTH * 2), yTextureOffset, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);
			else
				RenderUtil.drawTexturedModelRect(m4f, x, y + yOffset, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT, xTextureOffset, yTextureOffset, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);

			// Reassign texture offset for saturation
			yTextureOffset = 0;
			xTextureOffset = THIRST_TEXTURE_WIDTH * 6;
			if (player.hasEffect(EffectRegistry.THIRST.get()))
			{
				xTextureOffset += THIRST_TEXTURE_WIDTH * 2;
			}
			if(saturationInt > 0 && Config.Baked.thirstSaturationDisplayed)
			{
				if (halfIcon < saturationInt) { // Full saturation icon
					RenderUtil.drawTexturedModelRect(m4f, x, y + yOffset, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT, xTextureOffset, yTextureOffset, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);
				}
				else if (halfIcon == saturationInt) { // Half saturation icon
					RenderUtil.drawTexturedModelRect(m4f, x, y + yOffset, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT, xTextureOffset + THIRST_TEXTURE_WIDTH, yTextureOffset, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);
				}
			}
		}
	}

	public static void updateThirstGui()
	{
		updateCounter++;
	}
	
	private static void bind(ResourceLocation resource)
	{
		Minecraft.getInstance().getTextureManager().bind(resource);
	}
}
