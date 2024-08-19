package sfiomn.legendarysurvivaloverhaul.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.common.integration.vampirism.VampirismUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import java.util.Random;

public class RenderThirstGui
{
	private static ThirstCapability THIRST_CAP = null;
	private static final Random rand = new Random();

	public static final ResourceLocation ICONS = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/overlay.png");

	// Row position on the overlay sheet
	private static final int THIRST_TEXTURE_POS_Y = 0;
	private static final int THIRST_TEXTURE_POS_X = 0;

	// Dimensions of the icon
	private static final int THIRST_TEXTURE_WIDTH = 9;
	private static final int THIRST_TEXTURE_HEIGHT = 9;

	public static final IGuiOverlay THIRST_GUI = (forgeGui, guiGraphics, partialTicks, width, height) -> {
		if (Config.Baked.thirstEnabled
				&& !Minecraft.getInstance().options.hideGui
				&& forgeGui.shouldDrawSurvivalElements()) {
			Player player = forgeGui.getMinecraft().player;

			if (player != null) {
				if (!ThirstUtil.isThirstActive(player))
					return;

				rand.setSeed(player.tickCount * 445L);
				forgeGui.setupOverlayRenderState(true, false);

				Minecraft.getInstance().getProfiler().push("thirst_gui");
				drawHydrationBar(forgeGui, guiGraphics, player, width, height);
				Minecraft.getInstance().getProfiler().pop();

				forgeGui.rightHeight += 10;
			}
		}
	};
	
	public static void drawHydrationBar(ForgeGui forgeGui, GuiGraphics gui, Player player, int width, int height) {
		if (THIRST_CAP == null || player.tickCount % 20 == 0)
			THIRST_CAP = CapabilityUtil.getThirstCapability(player);

		// hydration is 0 - 20
		int hydration = THIRST_CAP.getHydrationLevel();
		float saturation = THIRST_CAP.getSaturationLevel();

		int left = width / 2 + 91; // Same x offset as the hunger bar
		int top = height - forgeGui.rightHeight;

		int saturationInt = (int)saturation;

		// Draw the 10 hydration / saturation bubbles
		for (int i = 0; i < 10; i++)
		{
			int halfIcon = i * 2 + 1;
			int x = left - i * 8 - 9;
			int y = top;

			// Shake based on hydration level and saturation level
			int yOffset = 0;
			if (Config.Baked.showVanillaAnimationOverlay && saturation <= 0.0f && player.tickCount % (hydration * 3 + 1) == 0)
			{
				yOffset = (rand.nextInt(3) - 1);
			}

			int xTextureOffset = THIRST_TEXTURE_POS_X;
			int yTextureOffset = THIRST_TEXTURE_POS_Y;
			if (player.hasEffect(MobEffectRegistry.THIRST.get()))
			{
				xTextureOffset = THIRST_TEXTURE_WIDTH * 3;
			}
			if (player.hasEffect(MobEffectRegistry.HEAT_THIRST.get())) {
				yTextureOffset = THIRST_TEXTURE_HEIGHT;
			}

			if (halfIcon < hydration) // Full hydration icon
				gui.blit(ICONS, x, y + yOffset, xTextureOffset + THIRST_TEXTURE_WIDTH, yTextureOffset, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);
			else if (halfIcon == hydration) // Half hydration icon
				gui.blit(ICONS, x, y + yOffset, xTextureOffset + (THIRST_TEXTURE_WIDTH * 2), yTextureOffset, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);
			else
				gui.blit(ICONS, x, y + yOffset, xTextureOffset, yTextureOffset, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);

			// Reassign texture offset for saturation
			yTextureOffset = 0;
			xTextureOffset = THIRST_TEXTURE_WIDTH * 6;
			if (player.hasEffect(MobEffectRegistry.THIRST.get())) {
				xTextureOffset += THIRST_TEXTURE_WIDTH * 2;
			} else if (player.hasEffect(MobEffectRegistry.HEAT_THIRST.get())) {
				xTextureOffset += THIRST_TEXTURE_WIDTH * 8;
			}

			if(saturationInt > 0 && Config.Baked.thirstSaturationDisplayed)
			{
				if (halfIcon < saturationInt) { // Full saturation icon
					gui.blit(ICONS, x, y + yOffset, xTextureOffset, yTextureOffset, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);
				}
				else if (halfIcon == saturationInt) { // Half saturation icon
					gui.blit(ICONS, x, y + yOffset, xTextureOffset + THIRST_TEXTURE_WIDTH, yTextureOffset, THIRST_TEXTURE_WIDTH, THIRST_TEXTURE_HEIGHT);
				}
			}
		}
	}
}
