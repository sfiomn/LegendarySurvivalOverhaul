package sfiomn.legendarysurvivaloverhaul.client.tooltips;

import com.mojang.datafixers.util.Either;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonTemperature;
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonConsumableThirst;
import sfiomn.legendarysurvivaloverhaul.api.item.CoatEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.common.items.CoatItem;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;
import sfiomn.legendarysurvivaloverhaul.registry.EffectRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.KeybindingRegistry;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;

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

			if (stack.getItem() instanceof ArmorItem && Config.Baked.temperatureEnabled) {
				addArmorBaseTemperatureText(itemRegistryName, tooltip);

				addArmorCoatTemperatureText(stack, tooltip);
			}

			// Added Description for coat items.
			if (stack.getItem() instanceof CoatItem && Config.Baked.temperatureEnabled)
				addCoatItemDescText((CoatItem) stack.getItem(), tooltip);

			if (Config.Baked.temperatureEnabled)
				addFoodEffectText(stack, tooltip);

			if (Config.Baked.localizedBodyDamageEnabled)
				addHealingText(stack, tooltip);

			if (Config.Baked.thirstEnabled && Config.Baked.showHydrationTooltip)
				addHydrationText(stack, tooltip);
		}
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
		int toolTipZ = 400; // tooltip text zLevel is 400, hardcode in GuiUtils.

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

	private static void addArmorBaseTemperatureText(ResourceLocation itemRegistryName, List<ITextComponent> tooltip) {
		float temperature = 0.0f;

		JsonTemperature jsonTemperature = JsonConfig.itemTemperatures.get(itemRegistryName.toString());

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
		JsonConsumableThirst jsonConsumableThirst = ThirstUtil.getThirstConfig(itemRegistryName, stack);

		HydrationTooltip hydrationTooltip = null;
		Effect hydrationEffect = null;

		if (jsonConsumableThirst != null) {
			hydrationTooltip = new HydrationTooltip(jsonConsumableThirst.hydration, jsonConsumableThirst.saturation, jsonConsumableThirst.effectChance, jsonConsumableThirst.effect);
			if (jsonConsumableThirst.effectChance > 0 && !jsonConsumableThirst.effect.isEmpty()) {
				hydrationEffect = ForgeRegistries.POTIONS.getValue(new ResourceLocation(jsonConsumableThirst.effect));
			}
		} else if (stack.getItem() == Items.POTION) {
			Potion potion = PotionUtils.getPotion(stack);
			if(potion == Potions.WATER || potion == Potions.AWKWARD || potion == Potions.MUNDANE || potion == Potions.THICK)
			{
				hydrationTooltip = new HydrationTooltip(HydrationEnum.NORMAL);
				hydrationEffect = HydrationEnum.NORMAL.getEffectIfApplicable();
			}
			else if (potion != Potions.EMPTY)
			{
				hydrationTooltip = new HydrationTooltip(HydrationEnum.POTION);
				hydrationEffect = HydrationEnum.POTION.getEffectIfApplicable();
			}
		} else {
			HydrationEnum hydrationEnum = ThirstUtil.getHydrationEnumTag(stack);
			if (hydrationEnum != null) {
				hydrationTooltip = new HydrationTooltip(hydrationEnum);
				hydrationEffect = hydrationEnum.getEffectIfApplicable();
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
		if (hydrationTooltip.chanceIconNumber > 0)
			tooltip.add(placeholder.setStyle(thirstStyle));

		if (hydrationEffect != null)
			tooltip.add(getHydrationEffectTooltip(hydrationEffect));
	}

	private static IFormattableTextComponent getHydrationEffectTooltip(Effect effect) {
		EffectInstance effectInstance = new EffectInstance(effect, 600, 0, false, true);
		IFormattableTextComponent iformattabletextcomponent = new TranslationTextComponent(effectInstance.getDescriptionId());

		if (effectInstance.getAmplifier() > 1) {
			iformattabletextcomponent = new TranslationTextComponent("potion.withAmplifier", iformattabletextcomponent, new TranslationTextComponent("potion.potency." + effectInstance.getAmplifier()));
		}

		if (effectInstance.getDuration() > 20) {
			iformattabletextcomponent = new TranslationTextComponent("potion.withDuration", iformattabletextcomponent, EffectUtils.formatDuration(effectInstance, 1.0f));
		}

		return iformattabletextcomponent.withStyle(Style.EMPTY.withColor(TextFormatting.BLUE));
	}
}
