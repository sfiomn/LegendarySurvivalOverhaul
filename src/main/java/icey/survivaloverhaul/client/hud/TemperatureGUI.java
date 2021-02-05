package icey.survivaloverhaul.client.hud;

import java.util.Random;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.api.temperature.TemperatureEnum;
import icey.survivaloverhaul.common.capability.temperature.TemperatureCapability;
import icey.survivaloverhaul.config.Config;
import icey.survivaloverhaul.util.CapabilityUtil;
import icey.survivaloverhaul.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Main.MOD_ID, bus = EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class TemperatureGUI
{
	private static final Minecraft mc = Minecraft.getInstance();
	
	private static int updateCounter = 0;
	
	private static final Random rand = new Random();
	
	public static final ResourceLocation ICONS = new ResourceLocation(Main.MOD_ID, "textures/gui/overlay.png");
	
	private static final int temperatureTexturePosY = 48;
	
	private static final int temperatureTextureWidth = 16;
	private static final int temperatureTextureHeight = 16;

	private static int oldTemperature = -1;
	private static int frameCounter = -1;
	private static boolean risingTemperature = false;
	private static boolean startAnimation = false;
	
	public static boolean shakeSide = false;
	
	
	@SubscribeEvent
	public static void renderHud(RenderGameOverlayEvent event)
	{
		if (event.getType() == RenderGameOverlayEvent.ElementType.ALL
				&& Config.Baked.temperatureEnabled 
				&& mc.playerController.gameIsSurvivalOrAdventure())
		{
			int scaledWidth = mc.getMainWindow().getScaledWidth();
			int scaledHeight = mc.getMainWindow().getScaledHeight();
			
			rand.setSeed((long) (updateCounter * 445));
			
			bind(ICONS);
			TemperatureCapability cap = CapabilityUtil.getTempCapability(mc.player);
			
			renderGUI(event.getMatrixStack(), cap, scaledWidth, scaledHeight);
			
			bind(AbstractGui.GUI_ICONS_LOCATION);
		}
	}
	
	public static void renderGUI(MatrixStack matrix, TemperatureCapability cap, int width, int height)
	{
		RenderSystem.enableBlend();
		
		switch (Config.Baked.temperatureDisplayMode)
		{
		case SYMBOL:
			drawTemperatureAsSymbol(matrix, cap, width, height);
			break;
		default:
			break;
		}
		
		RenderSystem.disableBlend();
	}
	
	public static void drawTemperatureAsSymbol(MatrixStack matrix, TemperatureCapability cap, int width, int height)
	{
		int x = width / 2 - 8;
		int y = height - 54;
		
		int xOffset = 0;
		int yOffset = 0;
		
		int temperature = cap.getTemperatureLevel();
		
		TemperatureEnum tempEnum = cap.getTemperatureEnum(); 
		
		IconPair icon;
		
		byte shakeFrequency = 0;
		
		switch (tempEnum)
		{
		case HEAT_STROKE:
			icon = IconPair.FIRE;
			
			if (temperature == TemperatureEnum.HEAT_STROKE.getLowerBound())
			{
				shakeFrequency = 0;
			}
			else if (temperature >= TemperatureEnum.HEAT_STROKE.getMiddle())
			{
				shakeFrequency = 1;
			}
			else
			{
				shakeFrequency = 2;
			}
			
			break;
		case HOT:
			icon = IconPair.ABOVE_NORMAL;
			break;
		case NORMAL:
			icon = IconPair.NORMAL;
			break;
		case COLD:
			icon = IconPair.BELOW_NORMAL;
			break;
		case FROSTBITE:
			icon = IconPair.SNOWFLAKE;
			
			if (temperature == TemperatureEnum.FROSTBITE.getUpperBound())
			{
				shakeFrequency = 0;
			}
			else if (temperature > TemperatureEnum.FROSTBITE.getMiddle() + 1)
			{
				shakeFrequency = 2;
			}
			else
			{
				shakeFrequency = 1;
			}
			
			break;
		default:
			icon = IconPair.UNKNOWN;
			break;
		}
		
		boolean isDying = (TemperatureEnum.FROSTBITE.getMiddle() >= cap.getTemperatureLevel()  || TemperatureEnum.HEAT_STROKE.getMiddle() < cap.getTemperatureLevel());
		
		if (shakeFrequency > 0)
		{
			if (updateCounter % shakeFrequency == 0 && !isDying)
			{
				int shakeCheck = updateCounter / shakeFrequency;
				
				if (shakeCheck % 2 == 0)
						shakeSide = true;
				else
						shakeSide = false;
				
				xOffset = shakeSide ? 1 : -1;
			}
			else if (isDying)
			{
				xOffset = rand.nextFloat() > 0.5f ? 1 : -1;
				yOffset = rand.nextFloat() > 0.5f ? 1 : -1;
			}
		}
		
		xOffset += Config.Baked.temperatureDisplayOffsetX;
		yOffset += Config.Baked.temperatureDisplayOffsetY;
		
		if (frameCounter >= 22)
		{
			icon = icon.getFlashVariant();
		}
		
		Matrix4f m4f = matrix.getLast().getMatrix();
		
		RenderUtil.drawTexturedModelRect(m4f, x + xOffset, y + yOffset, temperatureTextureWidth * icon.getIconIndex(), temperatureTexturePosY, temperatureTextureWidth, temperatureTextureHeight);
		RenderUtil.drawTexturedModelRect(m4f, x + xOffset, y + yOffset, temperatureTextureWidth * icon.getIconHolder(), temperatureTexturePosY, temperatureTextureWidth, temperatureTextureHeight);
		
		if (oldTemperature == -1)
		{
			oldTemperature = temperature;
		}
		
		if (oldTemperature != temperature)
		{
			risingTemperature = oldTemperature < temperature;
			oldTemperature = temperature;
			startAnimation = true;
		}
		
		int ovrXOffset = temperatureTextureWidth * ((frameCounter / 2) - 1);
		int ovrYOffset = temperatureTexturePosY + (temperatureTextureHeight * (risingTemperature ? 1 : 2));

		RenderUtil.drawTexturedModelRect(m4f, x + xOffset, y + yOffset, ovrXOffset, ovrYOffset, temperatureTextureWidth, temperatureTextureHeight);
	}
	
	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event)
	{
		if (event.phase == TickEvent.Phase.END)
		{
			if (!mc.isGamePaused())
			{
				updateCounter++;
				
				if (frameCounter >= 0)
						frameCounter--;
				
				if (startAnimation)
				{
					frameCounter = 24;
					startAnimation = false;
				}
			}
		}
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
		ABOVE_NORMAL(0, 11),
		BELOW_NORMAL(0, 12),
		NORMAL_FLASH(0, 6),
		FIRE_FLASH(1, 7),
		SNOWFLAKE_FLASH(2, 8),
		ABOVE_NORMAL_FLASH(0, 9),
		BELOW_NORMAL_FLASH(0, 10),
		UNKNOWN(0, 13);
		
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
		
		public IconPair getFlashVariant()
		{
			switch(this)
			{
			case NORMAL:
				return NORMAL_FLASH;
			case FIRE:
				return FIRE_FLASH;
			case SNOWFLAKE:
				return SNOWFLAKE_FLASH;
			case ABOVE_NORMAL:
				return ABOVE_NORMAL_FLASH;
			case BELOW_NORMAL:
				return BELOW_NORMAL_FLASH;
			default:
				return this;
			}
		}
	}
}
