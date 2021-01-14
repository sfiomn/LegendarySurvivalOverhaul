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
	
	private static final int texturepos_X = 0;
	private static final int texturepos_Y = 48;
	
	private static final int textureWidth = 16;
	private static final int textureHeight = 16;

	private static int oldTemperature = -1;
	private static int frameCounter = -1;
	private static boolean risingTemperature = false;
	private static boolean startAnimation = false;
	private static boolean shakeSide = false;
	
	@SubscribeEvent
	public static void renderHud(RenderGameOverlayEvent.Post event)
	{
		if (event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE 
				&& Config.BakedConfigValues.temperatureEnabled)
		{
			
		}
	}
	
	// @SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event)
	{
		
	}
	
	private void bind(ResourceLocation resource)
	{
		mc.getTextureManager().bindTexture(resource);
	}
}
