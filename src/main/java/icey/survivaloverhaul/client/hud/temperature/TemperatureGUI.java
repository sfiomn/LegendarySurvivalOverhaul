package icey.survivaloverhaul.client.hud.temperature;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.mojang.blaze3d.platform.GlStateManager;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.api.temperature.TemperatureEnum;
import icey.survivaloverhaul.api.temperature.TemperatureUtil;
import icey.survivaloverhaul.config.Config;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
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
	private final Minecraft mc = Minecraft.getInstance();
	private final MainWindow window = mc.getMainWindow();
	
	private int updateCounter = 0;
	
	private static TemperatureGUI instance;
	
	private final Random rand = new Random();
	
	public static final ResourceLocation ICONS = new ResourceLocation(Main.MOD_ID, "textures/gui/overlay.png");
	
	private static final int texturepos_X = 0;
	private static final int texturepos_Y = 48;
	
	private static final int textureWidth = 16;
	private static final int textureHeight = 16;
	
	public TemperatureGUI()
	{
		instance = this;
	}
	
	@SubscribeEvent
	public static void renderHud(RenderGameOverlayEvent event)
	{
		if (event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE && Config.BakedConfigValues.temperatureEnabled)
		{
			
		}
	}
	
	private void renderTemperatureIcon(int width, int height, int temperature)
	{
		GlStateManager.enableBlend();
		
		TemperatureEnum tempEnum = TemperatureUtil.getTemperatureEnum(temperature);
		int bgXOffset = textureWidth * tempEnum.ordinal();

		GlStateManager.disableBlend();
	}
	
	// @SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event)
	{
		if(event.phase == TickEvent.Phase.END)
		{
			if(!instance.mc.isGamePaused())
			{
				instance.updateCounter++;
			}
		}
	}
	
	private void bind(ResourceLocation resource)
	{
		mc.getTextureManager().bindTexture(resource);
	}
}
