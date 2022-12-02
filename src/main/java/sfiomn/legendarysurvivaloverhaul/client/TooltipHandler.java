package sfiomn.legendarysurvivaloverhaul.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonArmorIdentity;
import sfiomn.legendarysurvivaloverhaul.api.item.PaddingEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.common.items.PaddingItem;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;
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

		if (!stack.isEmpty())
		{
			List<ITextComponent> tooltip = event.getToolTip();

			ITextComponent baseArmorTempTooltip = getArmorBaseTemperature(stack);

			if (baseArmorTempTooltip != null)
			{
				tooltip.add(baseArmorTempTooltip);
			}

			ITextComponent paddingArmorTempTooltip = getArmorPaddingTemperature(stack);

			if (paddingArmorTempTooltip != null)
			{
				tooltip.add(paddingArmorTempTooltip);
			}

			// Added Description for padding items.
			if (stack.getItem() instanceof PaddingItem) {
				ITextComponent addedDescPaddingItem = getAddedDescPaddingItem((PaddingItem) stack.getItem());
				if (addedDescPaddingItem != null) {
					tooltip.add(addedDescPaddingItem);
				}
			}
			
			/*
			if (insulation != 1.0f)
			{
				ITextComponent insulationTranslation = new TranslationTextComponent("legendarysurvivaloverhaul.armorTooltip.insulation");
				int insulationPercent = (int) ((insulation - 1.0f) * 100);
				TextFormatting color = insulationPercent < 0 ? TextFormatting.BLUE : TextFormatting.RED;
				
				ITextComponent text = new StringTextComponent(insulationPercent < 0 ? "+" : "-")
						.mergeStyle(color)
						.appendString(Math.abs(insulationPercent) + "% ")
						.append(insulationTranslation);
				tooltip.add(text);
			}
			*/
		}
	}

	private static ITextComponent getArmorBaseTemperature(ItemStack stack) {
		float temperature = 0.0f;
		float insulation = 1.0f;

		List<JsonArmorIdentity> identities = JsonConfig.armorTemperatures.get(stack.getItem().getRegistryName().toString());

		if (identities != null)
		{
			for (JsonArmorIdentity jai : identities)
			{
				if (jai.matches(stack))
				{
					temperature = jai.temperature;
					insulation = jai.insulation;
					break;
				}
			}
		}

		ITextComponent text;

		if (temperature > 0.0f)
			text = new TranslationTextComponent("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".armor.heating");
		else if (temperature < 0.0f)
			text = new TranslationTextComponent("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".armor.cooling");
		else
			return null;

		String tempTxt = (temperature % 1.0f == 0f ? (int) Math.abs(temperature) : Math.abs(temperature)) + " ";

		text = new StringTextComponent("+")
				.withStyle(TextFormatting.BLUE)
				.append(tempTxt)
				.append(text);
		return text;
	}

	private static ITextComponent getArmorPaddingTemperature(ItemStack stack) {
		String paddingId = TemperatureUtil.getArmorPaddingTag(stack);
		PaddingEnum padding = PaddingEnum.getFromId(paddingId);

		ITextComponent text;

		if (padding != null && padding.modifier() > 0) {
			text = new TranslationTextComponent("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".armorPadding." + paddingId);
		}
		else if (padding != null && padding.modifier() == 0)
			text = new StringTextComponent("Error");
		else {
			return null;
		}

		text = new StringTextComponent("")
				.withStyle(TextFormatting.BLUE)
				.append(text);

		return text;
	}

	private static ITextComponent getAddedDescPaddingItem (PaddingItem paddingItem) {

		PaddingEnum padding = paddingItem.padding;

		ITextComponent text;
		if (InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), KeybindingRegistry.showAddedDesc.getKey().getValue())) {
			if (padding != null && padding.modifier() > 0) {
				text = new TranslationTextComponent("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".paddingItem." + padding.type() + ".desc")
						.append(" " + padding.modifier() + "\u00B0C");
			} else if (padding != null && padding.modifier() == 0)
				text = new StringTextComponent("Error");
			else {
				return null;
			}

			text = new StringTextComponent("")
					.withStyle(TextFormatting.DARK_GRAY)
					.append(text);
		} else {
			text = new StringTextComponent(TextFormatting.GRAY + I18n.get("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".addedDesc.activate", TextFormatting.LIGHT_PURPLE, I18n.get(KeybindingRegistry.showAddedDesc.getTranslatedKeyMessage().getString()), TextFormatting.GRAY));
		}

		return text;
	}
}
