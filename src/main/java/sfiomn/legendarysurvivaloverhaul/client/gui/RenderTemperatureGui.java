package sfiomn.legendarysurvivaloverhaul.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessMode;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.EffectRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;
import sfiomn.legendarysurvivaloverhaul.util.RenderUtil;

import java.util.Random;

public class RenderTemperatureGui
{
	private static int updateCounter = 0;

	private static final Random rand = new Random();
	
	public static final ResourceLocation ICONS = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/overlay.png");

	private static final int TEMPERATURE_TEXTURE_POS_Y = 48;
	private static final int TEMPERATURE_TEXTURE_WIDTH = 16;
	private static final int TEMPERATURE_TEXTURE_HEIGHT = 16;
	
	private static final int WETNESS_TEXTURE_POS_Y = 96;
	
	private static final int WETNESS_TEXTURE_WIDTH = 10;
	private static final int WETNESS_TEXTURE_HEIGHT = 10;

	private static final int HUNGER_TEXTURE_WIDTH = 9;
	private static final int HUNGER_TEXTURE_HEIGHT = 9;
	private static int frameCounter = -1;
	private static int delay = 0;
	private static boolean risingTemperature = false;
	private static boolean startAnimation = false;
	
	private static int lastWetnessSymbol = 0;
	private static int flashCounter = -1;
	
	public static boolean shakeSide = false;
	
	public static void render(MatrixStack matrix, PlayerEntity player, int width, int height)
	{
		rand.setSeed(updateCounter * 445L);

		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		
		TemperatureCapability tempCap = CapabilityUtil.getTempCapability(player);
		WetnessCapability wetCap = CapabilityUtil.getWetnessCapability(player);
		
		switch (Config.Baked.temperatureDisplayMode)
		{
		case SYMBOL:
			drawTemperatureAsSymbol(matrix, tempCap, width, height);
			break;
		default:
			break;
		}
		
		if (Config.Baked.wetnessMode == WetnessMode.DYNAMIC)
			drawWetness(matrix, wetCap, width, height);

		RenderSystem.disableBlend();
		bind(AbstractGui.GUI_ICONS_LOCATION);
	}

	public static void renderFoodBarEffect(MatrixStack matrix, PlayerEntity player, int width, int height) {
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();

		if (Config.Baked.temperatureSecondaryEffects)
			drawHungerSecondaryEffect(matrix, player, width, height);

		RenderSystem.disableBlend();
		bind(AbstractGui.GUI_ICONS_LOCATION);
	}
	
	public static void drawTemperatureAsSymbol(MatrixStack matrix, TemperatureCapability cap, int width, int height)
	{
		int x = width / 2 - (TEMPERATURE_TEXTURE_WIDTH / 2);
		int y = height - 52;
		
		int xOffset = 0;
		int yOffset = 0;

		float temperature = cap.getTemperatureLevel();
		float targetTemperature = cap.getTargetTemperatureLevel();
		TemperatureEnum tempEnum = cap.getTemperatureEnum();
		
		IconPair icon;
		
		byte shakeFrequency = 0;
		
		switch (tempEnum)
		{
		case HEAT_STROKE:
			icon = IconPair.FIRE;
			
			if ((int) temperature == TemperatureEnum.HEAT_STROKE.getLowerBound())
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
			
			if ((int) temperature == TemperatureEnum.FROSTBITE.getUpperBound())
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

				shakeSide = shakeCheck % 2 == 0;
				
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
		
		Matrix4f m4f = matrix.last().pose();

		bind(ICONS);
		
		RenderUtil.drawTexturedModelRect(m4f, x + xOffset, y + yOffset, TEMPERATURE_TEXTURE_WIDTH, TEMPERATURE_TEXTURE_HEIGHT, TEMPERATURE_TEXTURE_WIDTH * icon.getIconIndex(), TEMPERATURE_TEXTURE_POS_Y, TEMPERATURE_TEXTURE_WIDTH, TEMPERATURE_TEXTURE_HEIGHT);
		RenderUtil.drawTexturedModelRect(m4f, x + xOffset, y + yOffset, TEMPERATURE_TEXTURE_WIDTH, TEMPERATURE_TEXTURE_HEIGHT, TEMPERATURE_TEXTURE_WIDTH * icon.getIconHolder(), TEMPERATURE_TEXTURE_POS_Y, TEMPERATURE_TEXTURE_WIDTH, TEMPERATURE_TEXTURE_HEIGHT);

		if (delay == 0) {
			if ((int) targetTemperature != (int) temperature) {
				risingTemperature = targetTemperature > temperature;
				startAnimation = true;
				delay = 80;
			}
		} else if (frameCounter == -1){
			delay --;
		}
		
		int ovrXOffset = TEMPERATURE_TEXTURE_WIDTH * ((frameCounter / 2) - 1);
		int ovrYOffset = TEMPERATURE_TEXTURE_POS_Y + (TEMPERATURE_TEXTURE_HEIGHT * (risingTemperature ? 1 : 2));

		bind(ICONS);

		RenderUtil.drawTexturedModelRect(m4f, x + xOffset, y + yOffset, TEMPERATURE_TEXTURE_WIDTH, TEMPERATURE_TEXTURE_HEIGHT, ovrXOffset, ovrYOffset, TEMPERATURE_TEXTURE_WIDTH, TEMPERATURE_TEXTURE_HEIGHT);
	}
	
	public static void drawWetness(MatrixStack matrix, WetnessCapability cap, int width, int height)
	{
		int wetness = cap.getWetness();
		byte wetnessSymbol;
		
		int x = width / 2 - (WETNESS_TEXTURE_WIDTH / 2);
		int y = height - 61;

		
		int xOffset = Config.Baked.wetnessIndicatorOffsetX;
		int yOffset = Config.Baked.wetnessIndicatorOffsetY;
		
		if (wetness == 0)
			return;
		else
			wetnessSymbol = (byte) (MathHelper.clamp(MathUtil.invLerp(0, WetnessCapability.WETNESS_LIMIT, wetness) * 4, 0, 3));
		
		if (lastWetnessSymbol != wetnessSymbol)
		{
			flashCounter = 3;
			lastWetnessSymbol = wetnessSymbol;
		}
		
		int texPosX = wetnessSymbol * WETNESS_TEXTURE_WIDTH;
		int texPosY = WETNESS_TEXTURE_POS_Y + (flashCounter >= 0 ? WETNESS_TEXTURE_HEIGHT : 0);
		
		Matrix4f m4f = matrix.last().pose();

		bind(ICONS);
		
		RenderUtil.drawTexturedModelRect(m4f, x + xOffset, y + yOffset, WETNESS_TEXTURE_WIDTH, WETNESS_TEXTURE_HEIGHT, texPosX, texPosY, WETNESS_TEXTURE_WIDTH, WETNESS_TEXTURE_HEIGHT);
	}

	public static void drawHungerSecondaryEffect(MatrixStack matrix, PlayerEntity player, int width, int height) {
		if (player.hasEffect(EffectRegistry.COLD_HUNGER.get())) {

			Matrix4f m4f = matrix.last().pose();

			bind(ICONS);

			int foodLevel = player.getFoodData().getFoodLevel();
			float saturationLevelInt = (int) player.getFoodData().getSaturationLevel();

			int left = width / 2 + 91; // Same x offset as the hunger bar
			int top = height - 39;

			// Draw the 10 hunger meats
			for (int i = 0; i < 10; i++)
			{

				int halfIcon = i * 2 + 1;
				int x = left - i * 8 - 9;
				int y = top;

				// Shake based on thirst level and saturation level
				int yOffset = 0;
				if (Config.Baked.showVanillaAnimationOverlay && player.getFoodData().getSaturationLevel() <= 0.0f && updateCounter % (player.getFoodData().getFoodLevel() * 3 + 1) == 0)
				{
					yOffset = (rand.nextInt(3) - 1);
				}

				int xTextureOffset = HUNGER_TEXTURE_WIDTH * 6;
				int yTextureOffset = HUNGER_TEXTURE_HEIGHT;

				if (player.hasEffect(Effects.HUNGER)) {
					xTextureOffset += HUNGER_TEXTURE_WIDTH * 3;
				}

				if (halfIcon < foodLevel) // Full hunger icon
					RenderUtil.drawTexturedModelRect(m4f, x, y + yOffset, HUNGER_TEXTURE_WIDTH, HUNGER_TEXTURE_HEIGHT, xTextureOffset + HUNGER_TEXTURE_WIDTH, yTextureOffset, HUNGER_TEXTURE_WIDTH, HUNGER_TEXTURE_HEIGHT);
				else if (halfIcon == foodLevel) // Half hunger icon
					RenderUtil.drawTexturedModelRect(m4f, x, y + yOffset, HUNGER_TEXTURE_WIDTH, HUNGER_TEXTURE_HEIGHT, xTextureOffset + (HUNGER_TEXTURE_WIDTH * 2), yTextureOffset, HUNGER_TEXTURE_WIDTH, HUNGER_TEXTURE_HEIGHT);
				else
					RenderUtil.drawTexturedModelRect(m4f, x, y + yOffset, HUNGER_TEXTURE_WIDTH, HUNGER_TEXTURE_HEIGHT, xTextureOffset, yTextureOffset, HUNGER_TEXTURE_WIDTH, HUNGER_TEXTURE_HEIGHT);


				// Reassign texture offset for saturation
				xTextureOffset = HUNGER_TEXTURE_WIDTH * 12;
				if(saturationLevelInt > 0 && Config.Baked.thirstSaturationDisplayed)
				{
					if (halfIcon < saturationLevelInt) { // Full saturation icon
						RenderUtil.drawTexturedModelRect(m4f, x, y + yOffset, HUNGER_TEXTURE_WIDTH, HUNGER_TEXTURE_HEIGHT, xTextureOffset, yTextureOffset, HUNGER_TEXTURE_WIDTH, HUNGER_TEXTURE_HEIGHT);
					}
					else if (halfIcon == saturationLevelInt) { // Half saturation icon
						RenderUtil.drawTexturedModelRect(m4f, x, y + yOffset, HUNGER_TEXTURE_WIDTH, HUNGER_TEXTURE_HEIGHT, xTextureOffset + HUNGER_TEXTURE_WIDTH, yTextureOffset, HUNGER_TEXTURE_WIDTH, HUNGER_TEXTURE_HEIGHT);
					}
				}
			}
		}
	}

	public static void updateTemperatureGui()
	{
		updateCounter++;

		if (frameCounter >= 0)
			frameCounter--;
		if (flashCounter >= 0)
			flashCounter--;

		if (startAnimation)
		{
			frameCounter = 24;
			startAnimation = false;
		}
	}
	
	private static void bind(ResourceLocation resource)
	{
		Minecraft.getInstance().getTextureManager().bind(resource);
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
		
		private final int iconIndex;
		private final int iconHolder;
		
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
