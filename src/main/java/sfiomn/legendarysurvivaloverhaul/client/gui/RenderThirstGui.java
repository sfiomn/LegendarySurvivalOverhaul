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
		drawThirstBar(matrix, player, width, height);

		RenderSystem.disableBlend();
		bind(AbstractGui.GUI_ICONS_LOCATION);
	}
	
	public static void drawThirstBar(MatrixStack matrix, PlayerEntity player, int width, int height)
	{
		ThirstCapability thirstCap = CapabilityUtil.getThirstCapability(player);

		// thirst is 0 - 20
		int thirst = thirstCap.getThirstLevel();
		float thirstSaturation = thirstCap.getThirstSaturation();

		Matrix4f m4f = matrix.last().pose();

		int left = width / 2 + 91; // Same x offset as the hunger bar
		int top = height - ForgeIngameGui.right_height;
		ForgeIngameGui.right_height += 10;

		int xTextureOffset = THIRST_TEXTURE_POS_X;
		int yTextureOffset = THIRST_TEXTURE_POS_Y;
		if (player.hasEffect(EffectRegistry.THIRSTY.get()))
		{
			xTextureOffset = THIRST_TEXTURE_WIDTH * 3;
		}
		if (player.hasEffect(EffectRegistry.HEAT_SECONDARY_EFFECT.get())) {
			yTextureOffset = THIRST_TEXTURE_HEIGHT;
		}

		// Draw the 10 thirst bubbles
		for (int i = 0; i < 10; i++)
		{
			int halfIcon = i * 2 + 1;
			int x = left - i * 8 - 9;
			int y = top;
			int yOffset = 0;

			// Shake based on thirst level and saturation level
			if (Config.Baked.showVanillaAnimationOverlay && thirstSaturation <= 0.0f && updateCounter % (thirst * 3 + 1) == 0)
			{
				yOffset = (rand.nextInt(3) - 1);
			}

			if (halfIcon < thirst) // Full thirst icon
				RenderUtil.drawTexturedModelRect(m4f, x, y + yOffset, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT, xTextureOffset + THIRST_TEXTURE_WIDTH, yTextureOffset, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);
			else if (halfIcon == thirst) // Half thirst icon
				RenderUtil.drawTexturedModelRect(m4f, x, y + yOffset, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT, xTextureOffset + (THIRST_TEXTURE_WIDTH * 2), yTextureOffset, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);
			else
				RenderUtil.drawTexturedModelRect(m4f, x, y + yOffset, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT, xTextureOffset, yTextureOffset, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);
		}

		// Draw the 10 saturation bubbles
		// Because AppleSkin is awesome and everybody knows it
		int thirstSaturationInt = (int)thirstSaturation;
		if(thirstSaturationInt > 0)
		{
			if(Config.Baked.thirstSaturationDisplayed)
			{
				for(int i = 0; i < 10; i++)
				{
					int halfIcon = i * 2 + 1;
					int x = left - i * 8 - 9;
					int y = top;

					if (halfIcon < thirstSaturationInt) { // Full saturation icon
						xTextureOffset = THIRST_TEXTURE_WIDTH * 6;
						RenderUtil.drawTexturedModelRect(m4f, x, y, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT, xTextureOffset, yTextureOffset, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);
					}
					else if (halfIcon == thirstSaturationInt) { // Half saturation icon
						xTextureOffset = THIRST_TEXTURE_WIDTH * 7;
						RenderUtil.drawTexturedModelRect(m4f, x, y, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT, xTextureOffset, yTextureOffset, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);
					}
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
