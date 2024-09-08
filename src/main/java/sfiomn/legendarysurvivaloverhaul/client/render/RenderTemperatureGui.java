package sfiomn.legendarysurvivaloverhaul.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureDisplayEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessMode;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;

import java.util.Objects;
import java.util.Random;

public class RenderTemperatureGui
{
	private static TemperatureCapability TEMPERATURE_CAP = null;
	private static WetnessCapability WETNESS_CAP = null;
	private static final Random rand = new Random();

	private static final ResourceLocation ICONS = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/overlay.png");

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

	private static boolean shakeSide = false;
	
	public static IGuiOverlay TEMPERATURE_GUI = (forgeGui, guiGraphics, partialTicks, width, height) -> {
		if (Config.Baked.temperatureEnabled
				&& !Minecraft.getInstance().options.hideGui
				&& forgeGui.shouldDrawSurvivalElements()) {
			Player player = forgeGui.getMinecraft().player;

			if (player != null) {
				rand.setSeed(player.tickCount * 445L);

				forgeGui.setupOverlayRenderState(true, false);

                if (Objects.requireNonNull(Config.Baked.temperatureDisplayMode) == TemperatureDisplayEnum.SYMBOL) {
                    drawTemperatureAsSymbol(guiGraphics, player, width, height);
                }

				if (Config.Baked.wetnessMode == WetnessMode.DYNAMIC)
					drawWetness(guiGraphics, player, width, height);
			}
		}
	};

	public static IGuiOverlay FOOD_BAR_COLD_EFFECT_GUI = (forgeGui, guiGraphics, partialTicks, width, height) -> {
		if (!Minecraft.getInstance().options.hideGui
				&& forgeGui.shouldDrawSurvivalElements()) {
			Player player = forgeGui.getMinecraft().player;

			if (player != null && player.hasEffect(MobEffectRegistry.COLD_HUNGER.get())) {
				forgeGui.setupOverlayRenderState(true, false);

				Minecraft.getInstance().getProfiler().push("temperature_gui");
				drawFoodBarColdEffect(guiGraphics, player, width, height);
				Minecraft.getInstance().getProfiler().pop();

				forgeGui.rightHeight += 10;
			}
		}
	};
	
	public static void drawTemperatureAsSymbol(GuiGraphics gui, Player player, int width, int height) {

		if (TEMPERATURE_CAP == null || player.tickCount % 20 == 0)
			TEMPERATURE_CAP = CapabilityUtil.getTempCapability(player);

		int x = width / 2 - (TEMPERATURE_TEXTURE_WIDTH / 2);
		int y = height - 52;
		
		int xOffset = 0;
		int yOffset = 0;

		float temperature = TEMPERATURE_CAP.getTemperatureLevel();
		float targetTemperature = TEMPERATURE_CAP.getTargetTemperatureLevel();
		TemperatureEnum tempEnum = TEMPERATURE_CAP.getTemperatureEnum();
		
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
		
		boolean isDying = (TemperatureEnum.FROSTBITE.getMiddle() >= TEMPERATURE_CAP.getTemperatureLevel()  || TemperatureEnum.HEAT_STROKE.getMiddle() < TEMPERATURE_CAP.getTemperatureLevel());
		
		if (shakeFrequency > 0)
		{
			if (player.tickCount % shakeFrequency == 0 && !isDying)
			{
				int shakeCheck = player.tickCount / shakeFrequency;

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
		
		gui.blit(ICONS, x + xOffset, y + yOffset, TEMPERATURE_TEXTURE_WIDTH * icon.getIconIndex(), TEMPERATURE_TEXTURE_POS_Y, TEMPERATURE_TEXTURE_WIDTH, TEMPERATURE_TEXTURE_HEIGHT);
		gui.blit(ICONS, x + xOffset, y + yOffset, TEMPERATURE_TEXTURE_WIDTH * icon.getIconHolder(), TEMPERATURE_TEXTURE_POS_Y, TEMPERATURE_TEXTURE_WIDTH, TEMPERATURE_TEXTURE_HEIGHT);

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

		gui.blit(ICONS, x + xOffset, y + yOffset, ovrXOffset, ovrYOffset, TEMPERATURE_TEXTURE_WIDTH, TEMPERATURE_TEXTURE_HEIGHT);
	}
	
	public static void drawWetness(GuiGraphics gui, Player player, int width, int height)
	{
		if (WETNESS_CAP == null || player.tickCount % 20 == 0)
			WETNESS_CAP = CapabilityUtil.getWetnessCapability(player);

		int wetness = WETNESS_CAP.getWetness();
		byte wetnessSymbol;
		
		int x = width / 2 - (WETNESS_TEXTURE_WIDTH / 2);
		int y = height - 61;

		
		int xOffset = Config.Baked.wetnessIndicatorOffsetX;
		int yOffset = Config.Baked.wetnessIndicatorOffsetY;
		
		if (wetness == 0)
			return;
		else
			wetnessSymbol = (byte) (Mth.clamp(MathUtil.invLerp(0, WetnessCapability.WETNESS_LIMIT, wetness) * 4, 0, 3));
		
		if (lastWetnessSymbol != wetnessSymbol)
		{
			flashCounter = 3;
			lastWetnessSymbol = wetnessSymbol;
		}
		
		int texPosX = wetnessSymbol * WETNESS_TEXTURE_WIDTH;
		int texPosY = WETNESS_TEXTURE_POS_Y + (flashCounter >= 0 ? WETNESS_TEXTURE_HEIGHT : 0);
		
		gui.blit(ICONS, x + xOffset, y + yOffset, texPosX, texPosY, WETNESS_TEXTURE_WIDTH, WETNESS_TEXTURE_HEIGHT);
	}

	public static void drawFoodBarColdEffect(GuiGraphics gui, Player player, int width, int height) {

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
			if (Config.Baked.showVanillaAnimationOverlay && player.getFoodData().getSaturationLevel() <= 0.0f && player.tickCount % (player.getFoodData().getFoodLevel() * 3 + 1) == 0)
			{
				yOffset = (rand.nextInt(3) - 1);
			}

			int xTextureOffset = HUNGER_TEXTURE_WIDTH * 6;
			int yTextureOffset = HUNGER_TEXTURE_HEIGHT;

			if (player.hasEffect(MobEffects.HUNGER)) {
				xTextureOffset += HUNGER_TEXTURE_WIDTH * 3;
			}

			if (halfIcon < foodLevel) // Full hunger icon
				gui.blit(ICONS, x, y + yOffset, xTextureOffset + HUNGER_TEXTURE_WIDTH, yTextureOffset, HUNGER_TEXTURE_WIDTH, HUNGER_TEXTURE_HEIGHT);
			else if (halfIcon == foodLevel) // Half hunger icon
				gui.blit(ICONS, x, y + yOffset, xTextureOffset + (HUNGER_TEXTURE_WIDTH * 2), yTextureOffset, HUNGER_TEXTURE_WIDTH, HUNGER_TEXTURE_HEIGHT);
			else
				gui.blit(ICONS, x, y + yOffset, xTextureOffset, yTextureOffset, HUNGER_TEXTURE_WIDTH, HUNGER_TEXTURE_HEIGHT);


			// Reassign texture offset for saturation
			xTextureOffset = HUNGER_TEXTURE_WIDTH * 12;
			if(saturationLevelInt > 0 && Config.Baked.thirstSaturationDisplayed)
			{
				if (halfIcon < saturationLevelInt) { // Full saturation icon
					gui.blit(ICONS, x, y + yOffset, xTextureOffset, yTextureOffset, HUNGER_TEXTURE_WIDTH, HUNGER_TEXTURE_HEIGHT);
				}
				else if (halfIcon == saturationLevelInt) { // Half saturation icon
					gui.blit(ICONS, x, y + yOffset, xTextureOffset + HUNGER_TEXTURE_WIDTH, yTextureOffset, HUNGER_TEXTURE_WIDTH, HUNGER_TEXTURE_HEIGHT);
				}
			}
		}
	}

	public static void updateTimer()
	{
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
