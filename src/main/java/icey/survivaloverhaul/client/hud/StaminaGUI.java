package icey.survivaloverhaul.client.hud;

import java.util.Random;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.common.capability.stamina.StaminaCapability;
import icey.survivaloverhaul.config.Config;
import icey.survivaloverhaul.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Main.MOD_ID, bus = EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class StaminaGUI
{
	private static final Minecraft mc = Minecraft.getInstance();
	
	private static int updateCounter = 0;
	
	private static final Random rand = new Random();
	
	public static final ResourceLocation ICONS = new ResourceLocation(Main.MOD_ID, "textures/gui/overlay.png");

	private static final int texturePosX = 0;
	private static final int texturePosY = 9;
	
	private static final int textureWidth = 9;
	private static final int textureHeight = 9;
	
	public static int frameCounter = -1;
	
	// @SubscribeEvent
	public static void renderHud(RenderGameOverlayEvent event)
	{
		if (event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE
				&& Config.Baked.staminaEnabled 
				&& mc.playerController.gameIsSurvivalOrAdventure())
		{
			int scaledWidth = mc.getMainWindow().getScaledWidth();
			int scaledHeight = mc.getMainWindow().getScaledHeight();
			
			rand.setSeed((long) (updateCounter * 445));
			
			bind(ICONS);
			StaminaCapability cap = StaminaCapability.getStaminaCapability(mc.player);
			
			renderStaminaGui(event.getMatrixStack(), cap, scaledWidth, scaledHeight);
			
			bind(AbstractGui.GUI_ICONS_LOCATION);
		}
	}
	
	public static void renderStaminaGui(MatrixStack stack, StaminaCapability cap, int width, int height)
	{
		RenderSystem.enableBlend();
		
		int x = width / 2;
		int y = height / 2;
		
		int stamina = cap.getStamina();
		int maxStamina = cap.getMaxStamina();
		
		Matrix4f m4f = stack.getLast().getMatrix();
		
		for (int i = 0; i < maxStamina / StaminaCapability.STAMINA_INCREMENT; i++)
		{
			int j = stamina / StaminaCapability.STAMINA_INCREMENT;
			
			int staminaQuarter = stamina % StaminaCapability.STAMINA_INCREMENT;
			
			RenderUtil.drawTexturedModelRect(m4f, x + (textureWidth * i), y, texturePosX, texturePosY, textureWidth, textureHeight);
			
			RenderUtil.drawTexturedModelRect(m4f, x + (textureWidth * i), y, texturePosX + (8 * textureWidth) + (staminaQuarter * textureWidth), texturePosY, textureWidth, textureHeight);
		}

		RenderSystem.disableBlend();
	}
	
	private static void bind(ResourceLocation resource)
	{
		mc.getTextureManager().bindTexture(resource);
	}
}
