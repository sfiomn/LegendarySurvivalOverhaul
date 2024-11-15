package sfiomn.legendarysurvivaloverhaul.client.tooltips;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.config.json.bodydamage.JsonConsumableHeal;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonConsumableTemperature;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonConsumableThirst;
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonEffectParameter;
import sfiomn.legendarysurvivaloverhaul.api.item.CoatEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.common.integration.curios.CuriosUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;
import sfiomn.legendarysurvivaloverhaul.registry.AttributeRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.EffectRegistry;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.FORGE)
public class TooltipHandler
{
	
	@SuppressWarnings("unused")
	@SubscribeEvent
	public static void onTooltip(ItemTooltipEvent event)
	{
		ItemStack stack = event.getItemStack();

		ResourceLocation itemRegistryName = ForgeRegistries.ITEMS.getKey(stack.getItem());

		if (!stack.isEmpty() && itemRegistryName != null)
		{
			List<ITextComponent> tooltips = event.getToolTip();

			LegendarySurvivalOverhaul.LOGGER.debug("heating resistance desc id : " + AttributeRegistry.HEATING_TEMPERATURE.get().getDescriptionId());

			for (ITextComponent component: tooltips) {
				if (component instanceof TextComponent) {
					if (componentHasOneOfKeys((TextComponent) component,
							AttributeRegistry.HEATING_TEMPERATURE.get().getDescriptionId(),
							AttributeRegistry.HEAT_RESISTANCE.get().getDescriptionId()))
						((TextComponent) component).withStyle(Style.EMPTY.withColor(Color.fromRgb(16420407)));
					if (componentHasOneOfKeys((TextComponent) component,
							AttributeRegistry.COOLING_TEMPERATURE.get().getDescriptionId(),
							AttributeRegistry.COLD_RESISTANCE.get().getDescriptionId()))
						((TextComponent) component).withStyle(Style.EMPTY.withColor(Color.fromRgb(6466303)));
					if (componentHasOneOfKeys((TextComponent) component, AttributeRegistry.THERMAL_RESISTANCE.get().getDescriptionId()))
						((TextComponent) component).withStyle(Style.EMPTY.withColor(Color.fromRgb(10040319)));
				}
			}

			if (Config.Baked.temperatureEnabled) {
				if (CuriosUtil.isCuriosItem(stack))
					addCurioItemTemperatureText(itemRegistryName, tooltips);

				else if (stack.getItem() instanceof ArmorItem) {
					addCoatTemperatureText(stack, tooltips);
				}

				addFoodEffectText(stack, tooltips);
			}

			if (Config.Baked.localizedBodyDamageEnabled)
				addHealingText(stack, tooltips);

			if (Config.Baked.thirstEnabled && Config.Baked.showHydrationTooltip)
				addHydrationText(stack, tooltips);
		}
	}

	private static boolean componentHasOneOfKeys(TextComponent component, String... keys) {
		if (component instanceof TranslationTextComponent) {
			return Arrays.stream(((TranslationTextComponent) component).getArgs()).anyMatch(s -> {
				if (s instanceof TranslationTextComponent) {
					for (String key: keys) {
						if (((TranslationTextComponent) s).getKey().equals(key))
							return true;
					}
				}
				return false;
			});
		}
		return false;
	}

	@SubscribeEvent
	public static void onTooltipPostText(RenderTooltipEvent.PostText event) {
		if (event.isCanceled())
			return;

		Minecraft mc = Minecraft.getInstance();
		Screen gui = mc.screen;
		if (gui == null)
			return;

		int toolTipY = event.getY();
		int toolTipX = event.getX();
		int toolTipZ = 400; // tooltip text zLevel is 400, hardcoded in GuiUtils.

		// Find thirst font of text lines.
		HydrationTooltip hydrationTooltip = null;
		List<? extends ITextProperties> lines = event.getLines();
		for (int i = 0; i < lines.size(); ++i)
		{
			hydrationTooltip = HydrationTooltip.ThirstFont.getHydrationTooltip(lines.get(i));
			if (hydrationTooltip != null)
			{
				toolTipY += i * 10;
				break;
			}
		}

		if (hydrationTooltip == null)
			return;

		hydrationTooltip.renderTooltipIcons(event.getMatrixStack(), toolTipX, toolTipY, toolTipZ);
	}

	private static void addCurioItemTemperatureText(ResourceLocation itemRegistryName, List<ITextComponent> tooltip) {
		float temperature = 0.0f;

		JsonTemperatureResistance jsonTemperatureResistance = JsonConfig.itemTemperatures.get(itemRegistryName.toString());

		if (jsonTemperatureResistance != null)
		{
			temperature = jsonTemperatureResistance.temperature;
		}

		ITextComponent text;
		Color formattingColor;

		if (temperature > 0.0f) {
			text = new TranslationTextComponent("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".armor.heating");
			formattingColor = Color.fromRgb(16420407);
		} else if (temperature < 0.0f) {
			text = new TranslationTextComponent("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".armor.cooling");
			formattingColor = Color.fromRgb(6466303);
		} else
			return;

		String tempTxt = (temperature % 1.0f == 0f ? (int) Math.abs(temperature) : Math.abs(temperature)) + " ";

		text = new StringTextComponent("+")
				.withStyle(Style.EMPTY.withColor(formattingColor))
				.append(tempTxt)
				.append(text);

		tooltip.add(text);
	}

	private static void addCoatTemperatureText(ItemStack stack, List<ITextComponent> tooltip) {
		String coatId = TemperatureUtil.getArmorCoatTag(stack);
		CoatEnum coat = CoatEnum.getFromId(coatId);

		ITextComponent text;

		if (coat != null && coat.modifier() > 0) {
			text = new TranslationTextComponent("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".armor_coat." + coatId);
		}
		else if (coat != null && coat.modifier() == 0)
			text = new StringTextComponent("Error");
		else {
			return;
		}

		text = new StringTextComponent("")
				.withStyle(TextFormatting.BLUE)
				.append(text);

		tooltip.add(text);
	}

	private static void addFoodEffectText(ItemStack stack, List<ITextComponent> tooltip) {
		ResourceLocation itemRegistryName = stack.getItem().getRegistryName();
		assert itemRegistryName != null;
		List<JsonConsumableTemperature> jcts = JsonConfig.consumableTemperature.get(itemRegistryName.toString());

		if (jcts != null) {
			for (JsonConsumableTemperature jct: jcts) {
				EffectInstance effectInstance = new EffectInstance(jct.getEffect(), jct.duration, Math.abs(jct.temperatureLevel));
				IFormattableTextComponent iformattabletextcomponent = new TranslationTextComponent(effectInstance.getDescriptionId());

				if (Math.abs(jct.temperatureLevel) > 1) {
					iformattabletextcomponent = new TranslationTextComponent("potion.withAmplifier", iformattabletextcomponent, new TranslationTextComponent("potion.potency." + (Math.abs(jct.temperatureLevel) - 1)));
				}

				if (jct.duration > 20) {
					iformattabletextcomponent = new TranslationTextComponent("potion.withDuration", iformattabletextcomponent, EffectUtils.formatDuration(effectInstance, 1.0f));
				}

				if (jct.getEffect() == EffectRegistry.COLD_FOOD.get() || jct.getEffect() == EffectRegistry.COLD_DRINK.get())
					tooltip.add(iformattabletextcomponent.withStyle(Style.EMPTY.withColor(Color.fromRgb(6466303))));

				if (jct.getEffect() == EffectRegistry.HOT_FOOD.get() || jct.getEffect() == EffectRegistry.HOT_DRINk.get())
					tooltip.add(iformattabletextcomponent.withStyle(Style.EMPTY.withColor(Color.fromRgb(16420407))));

			}
		}
	}

	private static void addHealingText(ItemStack stack, List<ITextComponent> tooltips) {

		ResourceLocation itemRegistryName = ForgeRegistries.ITEMS.getKey(stack.getItem());
		assert itemRegistryName != null;
		JsonConsumableHeal jsonConsumableHeal = JsonConfig.consumableHeal.get(itemRegistryName.toString());

		if (jsonConsumableHeal != null) {
			if (jsonConsumableHeal.healingCharges > 0) {
				tooltips.add(new TranslationTextComponent("tooltip.legendarysurvivaloverhaul.body_heal_item.body_part", jsonConsumableHeal.healingCharges));
			} else if (jsonConsumableHeal.healingCharges == 0) {
				tooltips.add(new TranslationTextComponent("tooltip.legendarysurvivaloverhaul.body_heal_item.whole_body"));
			}
			tooltips.add(new TranslationTextComponent("tooltip.legendarysurvivaloverhaul.body_heal_item.healing_value", jsonConsumableHeal.healingValue, MathUtil.round(jsonConsumableHeal.healingTime / 20.0f, 1)));
		}
	}

	private static void addHydrationText(ItemStack stack, List<ITextComponent> tooltip) {
		ResourceLocation itemRegistryName = stack.getItem().getRegistryName();
		assert itemRegistryName != null;
		JsonConsumableThirst jsonConsumableThirst = ThirstUtil.getThirstJsonConfig(itemRegistryName, stack);

		HydrationTooltip hydrationTooltip = null;
		List<IFormattableTextComponent> hydrationEffectComponents = new ArrayList<>();

		if (jsonConsumableThirst != null) {
			hydrationTooltip = new HydrationTooltip(jsonConsumableThirst.hydration, jsonConsumableThirst.saturation);
			for (JsonEffectParameter effect: jsonConsumableThirst.effects) {
				if (effect.chance > 0 && effect.duration > 0 && !effect.name.isEmpty()) {
					hydrationEffectComponents.add(getHydrationEffectTooltip(effect.chance, effect.name, effect.amplifier, effect.duration));
				}
			}
		}

		if (hydrationTooltip == null) {
			return;
		}

		Style thirstStyle = Style.EMPTY.withFont(new HydrationTooltip.ThirstFont(hydrationTooltip));
		StringTextComponent placeholder = new StringTextComponent(hydrationTooltip.getPlaceholderTooltip());
		if (hydrationTooltip.hydrationIconNumber > 0)
			tooltip.add(placeholder.setStyle(thirstStyle));
		if ((hydrationTooltip.saturationIconNumber > 0 && !Config.Baked.mergeHydrationAndSaturationTooltip) ||
				(hydrationTooltip.hydrationIconNumber <= 0 && hydrationTooltip.saturationIconNumber > 0) )
			tooltip.add(placeholder.setStyle(thirstStyle));

		for (IFormattableTextComponent hydrationEffectComponent: hydrationEffectComponents) {
			if (hydrationEffectComponent != null)
				tooltip.add(hydrationEffectComponent);
		}
	}

	private static IFormattableTextComponent getHydrationEffectTooltip(double effectChance, String effectName, int amplifier, int duration) {
		Effect effect = null;
		if (effectName != null && !effectName.isEmpty() && effectChance > 0)
			effect = ForgeRegistries.POTIONS.getValue(new ResourceLocation(effectName));

		if (effect == null)
			return null;

		EffectInstance effectInstance = new EffectInstance(effect, duration, amplifier, false, true);
		IFormattableTextComponent iformattabletextcomponent = new TranslationTextComponent(effectInstance.getDescriptionId());

		if (effectInstance.getAmplifier() > 1) {
			iformattabletextcomponent = new TranslationTextComponent("potion.withAmplifier", iformattabletextcomponent, new TranslationTextComponent("potion.potency." + effectInstance.getAmplifier()));
		}

		if (effectInstance.getDuration() > 20) {
			iformattabletextcomponent = new TranslationTextComponent("potion.withDuration", iformattabletextcomponent, EffectUtils.formatDuration(effectInstance, 1.0f));
		}

		if (effectChance < 1)
			iformattabletextcomponent = new TranslationTextComponent("tooltip.legendarysurvivaloverhaul.potion_with_effectChance", (int) (effectChance*100), iformattabletextcomponent);


		return iformattabletextcomponent.withStyle(Style.EMPTY.withColor(TextFormatting.BLUE));
	}
}
