package sfiomn.legendarysurvivaloverhaul.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import java.util.Random;

public class RenderThirstGui
{
	private static int updateTimer = 0;

	private static final Random rand = new Random();

	public static final ResourceLocation ICONS = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/overlay.png");

	// Row position on the overlay sheet
	private static final int THIRST_TEXTURE_POS_Y = 0;
	private static final int THIRST_TEXTURE_POS_X = 0;

	// Dimensions of the icon
	private static final int THIRST_TEXTURE_WIDTH = 9;
	private static final int THIRST_TEXTURE_HEIGHT = 9;

	// Similar to net.minecraftforge.client.ForgeIngameGui.renderFood
	public static void render(GuiGraphics gui, Player player, int width, int height)
	{
		rand.setSeed(updateTimer * 445L);

		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();

		drawHydrationBar(gui, player, width, height);

		RenderSystem.disableBlend();
	}
	
	public static void drawHydrationBar(GuiGraphics gui, Player player, int width, int height)
	{
		ThirstCapability thirstCap = CapabilityUtil.getThirstCapability(player);

		// hydration is 0 - 20
		int hydration = thirstCap.getHydrationLevel();
		float saturation = thirstCap.getSaturationLevel();

		int forgeGuiLeft = 39;
		int left = width / 2 + 91; // Same x offset as the hunger bar
		int top = height - forgeGuiLeft;
		forgeGuiLeft += 10;

		int saturationInt = (int)saturation;

		// Draw the 10 hydration / saturation bubbles
		for (int i = 0; i < 10; i++)
		{
			int halfIcon = i * 2 + 1;
			int x = left - i * 8 - 9;
			int y = top;

			// Shake based on hydration level and saturation level
			int yOffset = 0;
			if (Config.Baked.showVanillaAnimationOverlay && saturation <= 0.0f && updateTimer % (hydration * 3 + 1) == 0)
			{
				yOffset = (rand.nextInt(3) - 1);
			}

			int xTextureOffset = THIRST_TEXTURE_POS_X;
			int yTextureOffset = THIRST_TEXTURE_POS_Y;
			if (player.hasEffect(MobEffectRegistry.THIRST.get()))
			{
				xTextureOffset = THIRST_TEXTURE_WIDTH * 3;
			}
			if (player.hasEffect(MobEffectRegistry.HEAT_Thirst.get())) {
				yTextureOffset = THIRST_TEXTURE_HEIGHT;
			}

			if (halfIcon < hydration) // Full hydration icon
				gui.blit(ICONS, x, y + yOffset, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT, xTextureOffset + THIRST_TEXTURE_WIDTH, yTextureOffset, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);
			else if (halfIcon == hydration) // Half hydration icon
				gui.blit(ICONS, x, y + yOffset, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT, xTextureOffset + (THIRST_TEXTURE_WIDTH * 2), yTextureOffset, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);
			else
				gui.blit(ICONS, x, y + yOffset, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT, xTextureOffset, yTextureOffset, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);

			// Reassign texture offset for saturation
			yTextureOffset = 0;
			xTextureOffset = THIRST_TEXTURE_WIDTH * 6;
			if (player.hasEffect(MobEffectRegistry.THIRST.get()))
			{
				xTextureOffset += THIRST_TEXTURE_WIDTH * 2;
			}
			if(saturationInt > 0 && Config.Baked.thirstSaturationDisplayed)
			{
				if (halfIcon < saturationInt) { // Full saturation icon
					gui.blit(ICONS, x, y + yOffset, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT, xTextureOffset, yTextureOffset, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);
				}
				else if (halfIcon == saturationInt) { // Half saturation icon
					gui.blit(ICONS, x, y + yOffset, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT, xTextureOffset + THIRST_TEXTURE_WIDTH, yTextureOffset, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);
				}
			}
		}
	}

	public static void updateTimer()
	{
		updateTimer++;
	}
}
