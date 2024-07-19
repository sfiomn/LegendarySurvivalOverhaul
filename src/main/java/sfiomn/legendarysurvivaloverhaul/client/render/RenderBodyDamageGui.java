package sfiomn.legendarysurvivaloverhaul.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.bodydamage.BodyDamageCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RenderBodyDamageGui
{
	private static BodyDamageCapability BODY_DAMAGE_CAP = null;
	private static final ResourceLocation ICONS = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/overlay.png");

	private static final int BODY_MODEL_TEXTURE_WIDTH = 16;
	private static final int BODY_MODEL_TEXTURE_HEIGHT = 32;

	private static final Map<BodyPartEnum, Integer> flashCounters = new HashMap<>();
	private static final Map<BodyPartEnum, Float> bodyPartHealth = new HashMap<>();
	
	public static IGuiOverlay BODY_DAMAGE_GUI = (forgeGui, guiGraphics, partialTicks, width, height) -> {

		if (Config.Baked.localizedBodyDamageEnabled
				&& !Minecraft.getInstance().options.hideGui
				&& forgeGui.shouldDrawSurvivalElements()) {

			Player player = forgeGui.getMinecraft().player;

			if (player != null) {
				forgeGui.setupOverlayRenderState(true, false);

				if (BODY_DAMAGE_CAP == null || player.tickCount % 20 == 0)
					BODY_DAMAGE_CAP = CapabilityUtil.getBodyDamageCapability(player);

				if (Config.Baked.alwaysShowBodyDamageIndicator || BODY_DAMAGE_CAP.isWounded()) {
					Minecraft.getInstance().getProfiler().push("body_damage_gui");
					drawBodyDamage(guiGraphics, BODY_DAMAGE_CAP, width, height);
					Minecraft.getInstance().getProfiler().pop();
				}
			}
		}
	};
	
	public static void drawBodyDamage(GuiGraphics gui, BodyDamageCapability cap, int width, int height) {
		int x = width / 2 + 92 + Config.Baked.bodyDamageIndicatorOffsetX;
		int y = height - 34 + Config.Baked.bodyDamageIndicatorOffsetY;

		for (BodyPartEnum bodyPart: BodyPartEnum.values()) {

			if (!bodyPartHealth.containsKey(bodyPart)) {
				bodyPartHealth.put(bodyPart, cap.getBodyPartDamage(bodyPart));
			} else {
				if (bodyPartHealth.get(bodyPart) != cap.getBodyPartDamage(bodyPart)) {
					flashCounters.put(bodyPart, 4);
					bodyPartHealth.put(bodyPart, cap.getBodyPartDamage(bodyPart));
				}
			}

			BodyPartIcon icon = BodyPartIcon.get(bodyPart);
			if (icon == null)
				continue;

			float healthRatio = cap.getBodyPartHealthRatio(bodyPart);
			boolean shouldFlash = flashCounters.containsKey(bodyPart);

            BodyPartCondition offset = BodyPartCondition.get(healthRatio, shouldFlash);

			gui.blit(ICONS, x + icon.x, y + icon.y,
					BODY_MODEL_TEXTURE_WIDTH * offset.iconIndexX + icon.texX,
					BODY_MODEL_TEXTURE_HEIGHT * offset.iconIndexY + icon.texY, icon.width, icon.height);
		}
	}

	public static void updateFlashingTimer() {
		Iterator<Map.Entry<BodyPartEnum, Integer>> iter = flashCounters.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<BodyPartEnum, Integer> flashBodyPart = iter.next();
			if (flashBodyPart.getValue() > 0)
				flashBodyPart.setValue(flashBodyPart.getValue() - 1);
			else
				iter.remove();
		}
	}
	
	private enum BodyPartCondition {
		HEALTHY(0, 0),
		SLIGHTLY_WOUNDED(1, 0),
		WOUNDED(2, 0),
		HEAVILY_WOUNDED(3, 0),
		DEAD(4, 0),
		HEALTHY_FLASH(0, 1),
		SLIGHTLY_WOUNDED_FLASH(1, 1),
		WOUNDED_FLASH(2, 1),
		HEAVILY_WOUNDED_FLASH(3, 1),
		DEAD_FLASH(4, 1);
		
		public final int iconIndexX;
		public final int iconIndexY;
		
		BodyPartCondition(int iconIndexX, int iconIndexY) {
			this.iconIndexX = iconIndexX;
			this.iconIndexY = iconIndexY;
		}

		public static BodyPartCondition get(float healthRatio, boolean flash) {
			if (healthRatio >= 1.0f) {
				return flash ? HEALTHY_FLASH: HEALTHY;
			} else if (healthRatio >= 0.66f) {
				return flash ? SLIGHTLY_WOUNDED_FLASH: SLIGHTLY_WOUNDED;
			} else if (healthRatio >= 0.33f) {
				return flash ? WOUNDED_FLASH: WOUNDED;
			} else if (healthRatio > 0) {
				return flash ? HEAVILY_WOUNDED_FLASH: HEAVILY_WOUNDED;
			} else {
				return flash ? DEAD_FLASH: DEAD;
			}
		}
	}

	private enum BodyPartIcon {
		HEAD(4, 0, 4, 136, 8, 8),
		RIGHT_ARM(12, 8, 12, 144, 4, 12),
		LEFT_ARM(0, 8, 0, 144, 4, 12),
		CHEST(4, 8, 4, 144, 8, 12),
		RIGHT_LEG(8, 20, 8, 156, 4, 8),
		LEFT_LEG(4, 20, 4, 156, 4, 8),
		RIGHT_FOOT(8, 28, 8, 164, 4, 4),
		LEFT_FOOT(4, 28, 4, 164, 4, 4);

		public final int x;
		public final int y;
		public final int texX;
		public final int texY;
		public final int width;
		public final int height;

		BodyPartIcon(int x, int y, int texX, int texY, int width, int height)
		{
			this.x = x;
			this.y = y;
			this.texX = texX;
			this.texY = texY;
			this.width = width;
			this.height = height;
		}

		public static BodyPartIcon get(BodyPartEnum bodyPart) {
			switch (bodyPart) {
				case HEAD:
					return HEAD;
				case RIGHT_ARM:
					return RIGHT_ARM;
				case LEFT_ARM:
					return LEFT_ARM;
				case CHEST:
					return CHEST;
				case RIGHT_LEG:
					return RIGHT_LEG;
				case RIGHT_FOOT:
					return RIGHT_FOOT;
				case LEFT_LEG:
					return LEFT_LEG;
				case LEFT_FOOT:
					return LEFT_FOOT;
				default:
					return null;
			}
		}
	}
}
