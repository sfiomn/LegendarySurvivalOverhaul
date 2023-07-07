package sfiomn.legendarysurvivaloverhaul.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonConsumableTemperature;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonTemperature;
import sfiomn.legendarysurvivaloverhaul.api.item.CoatEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.common.items.CoatItem;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;
import sfiomn.legendarysurvivaloverhaul.registry.EffectRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.KeybindingRegistry;

import java.util.List;

@Mod.EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.FORGE)
public class TooltipHandler
{
	
	@SuppressWarnings("unused")
	@SubscribeEvent
	public static void onTooltip(ItemTooltipEvent event)
	{
		ItemStack stack = event.getItemStack();

		if (!stack.isEmpty() && stack.getItem().getRegistryName() != null)
		{
			List<ITextComponent> tooltip = event.getToolTip();

			ResourceLocation itemRegistryName = stack.getItem().getRegistryName();

			if (stack.getItem() instanceof ArmorItem) {
				addArmorBaseTemperatureText(itemRegistryName, tooltip);

				addArmorCoatTemperatureText(stack, tooltip);
			}

			// Added Description for coat items.
			if (stack.getItem() instanceof CoatItem) {
				addCoatItemDescText((CoatItem) stack.getItem(), tooltip);
			}

			if (stack.isEdible()) {
				addFoodEffectText(stack, tooltip);
			}
		}
	}

	private static void addArmorBaseTemperatureText(ResourceLocation itemRegistryName, List<ITextComponent> tooltip) {
		float temperature = 0.0f;

		JsonTemperature jsonTemperature = JsonConfig.armorTemperatures.get(itemRegistryName.toString());

		if (jsonTemperature != null)
		{
			temperature = jsonTemperature.temperature;
		}

		ITextComponent text;

		if (temperature > 0.0f)
			text = new TranslationTextComponent("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".armor.heating");
		else if (temperature < 0.0f)
			text = new TranslationTextComponent("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".armor.cooling");
		else
			return;

		String tempTxt = (temperature % 1.0f == 0f ? (int) Math.abs(temperature) : Math.abs(temperature)) + " ";

		text = new StringTextComponent("+")
				.withStyle(TextFormatting.BLUE)
				.append(tempTxt)
				.append(text);

		tooltip.add(text);
	}

	private static void addArmorCoatTemperatureText(ItemStack stack, List<ITextComponent> tooltip) {
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

	private static void addCoatItemDescText(CoatItem coatItem, List<ITextComponent> tooltip) {

		CoatEnum coat = coatItem.coat;

		ITextComponent text;
		if (InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), KeybindingRegistry.showAddedDesc.getKey().getValue())) {
			if (coat != null && coat.modifier() > 0) {
				text = new TranslationTextComponent("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".coat_item." + coat.type() + ".desc")
						.append(" " + coat.modifier() + "\u00B0C");
			} else if (coat != null && coat.modifier() == 0)
				text = new StringTextComponent("Error");
			else {
				return;
			}

			text = new StringTextComponent("")
					.withStyle(TextFormatting.DARK_GRAY)
					.append(text);
		} else {
			text = new StringTextComponent(TextFormatting.GRAY + I18n.get("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".added_desc.activate", TextFormatting.LIGHT_PURPLE, I18n.get(KeybindingRegistry.showAddedDesc.getTranslatedKeyMessage().getString()), TextFormatting.GRAY));
		}

		tooltip.add(text);
	}


	private static void addFoodEffectText(ItemStack stack, List<ITextComponent> tooltip) {
		ResourceLocation itemRegistryName = stack.getItem().getRegistryName();
		assert itemRegistryName != null;
		List<JsonConsumableTemperature> jcts = JsonConfig.consumableTemperature.get(itemRegistryName.toString());

		if (jcts != null) {
			for (JsonConsumableTemperature jct: jcts) {
				EffectInstance effectInstance = new EffectInstance(jct.getEffect(), jct.duration, jct.temperatureLevel);
				IFormattableTextComponent iformattabletextcomponent = new TranslationTextComponent(effectInstance.getDescriptionId());

				if (jct.temperatureLevel > 1) {
					iformattabletextcomponent = new TranslationTextComponent("potion.withAmplifier", iformattabletextcomponent, new TranslationTextComponent("potion.potency." + (jct.temperatureLevel - 1)));
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
}
