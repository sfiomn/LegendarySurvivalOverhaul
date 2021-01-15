package icey.survivaloverhaul.client.hud.temperature;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.api.temperature.TemperatureEnum;
import icey.survivaloverhaul.api.temperature.TemperatureUtil;
import icey.survivaloverhaul.common.capability.temperature.TemperatureCapability;
import icey.survivaloverhaul.config.Config;
import icey.survivaloverhaul.util.RenderUtil;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = Main.MOD_ID, bus = EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class TemperatureGUI
{
	private static final Minecraft mc = Minecraft.getInstance();
	
	private static int updateCounter = 0;
	
	private static final Random rand = new Random();
	
	public static final ResourceLocation ICONS = new ResourceLocation(Main.MOD_ID, "textures/gui/overlay.png");
	
	private static final int texturePosX = 0;
	private static final int texturePosY = 48;
	
	private static final int textureWidth = 16;
	private static final int textureHeight = 16;

	private static int oldTemperature = -1;
	private static int frameCounter = -1;
	private static boolean risingTemperature = false;
	private static boolean startAnimation = false;
	private static boolean shakeSide = false;
	
	@SubscribeEvent
	public static void renderHud(RenderGameOverlayEvent event)
	{
		if (event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE
				&& Config.BakedConfigValues.temperatureEnabled 
				&& mc.playerController.gameIsSurvivalOrAdventure())
		{
			int scaledWidth = mc.getMainWindow().getScaledWidth();
			int scaledHeight = mc.getMainWindow().getScaledHeight();
			
			bind(ICONS);
			TemperatureCapability cap = TemperatureCapability.getTempCapability(mc.player);
			
			renderTemperatureGui(cap, scaledWidth, scaledHeight);
		}
	}
	
	// @SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event)
	{
		
	}
	
	public static void renderTemperatureGui(TemperatureCapability cap, int width, int height)
	{
		RenderSystem.enableBlend();
		
		switch (Config.BakedConfigValues.temperatureDisplayMode)
		{
		case SYMBOL:
			int x = width / 2 - 8;
			int y = height - 54;
			
			IconPair icon = IconPair.NORMAL;
			
			RenderUtil.drawTexturedModelRect(x, y, textureWidth * icon.getIconIndex(), texturePosY, textureWidth, textureHeight);
			RenderUtil.drawTexturedModelRect(x, y, textureWidth * icon.getIconHolder(), texturePosY, textureWidth, textureHeight);
			
			break;
		default:
			break;
		}
		
		RenderSystem.disableBlend();
	}
	
	private static void bind(ResourceLocation resource)
	{
		mc.getTextureManager().bindTexture(resource);
	}
	
	private enum IconPair
	{
		NORMAL(0, 3),
		FIRE(1, 4),
		SNOWFLAKE(2, 5),
		NORMAL_FLASH(0, 6),
		FIRE_FLASH(1, 7),
		SNOWFLAKE_FLASH(2, 8),
		ABOVE_NORMAL(0, 11),
		BELOW_NORMAL(0, 12),
		ABOVE_NORMAL_FLASH(0, 9),
		BELOW_NORMAL_FLASH(0, 10);
		
		private int iconIndex;
		private int iconHolder;
		
		private IconPair(int iconIndex, int iconHolder)
		{
			this.iconIndex = iconIndex;
			this.iconHolder = iconHolder;
		}
		
		public int getIconIndex()
		{
			return this.iconIndex;
		}
		
		public int getIconHolder()
		{
			return this.iconHolder;
		}
	}
}
