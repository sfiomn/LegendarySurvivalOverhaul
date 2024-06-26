package sfiomn.legendarysurvivaloverhaul.client.tooltips;

import com.mojang.datafixers.util.Either;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonConsumableTemperature;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonTemperature;
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonThirst;
import sfiomn.legendarysurvivaloverhaul.api.item.CoatEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.common.items.CoatItem;
import sfiomn.legendarysurvivaloverhaul.common.items.drink.DrinkItem;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.KeyMappingRegistry;

import java.util.List;

@Mod.EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.FORGE)
public class TooltipHandler
{
	
	@SuppressWarnings("unused")
	@SubscribeEvent
	public static void onTooltip(ItemTooltipEvent event)
	{
		ItemStack stack = event.getItemStack();

		if (!stack.isEmpty() && ForgeRegistries.ITEMS.getKey(stack.getItem()) != null)
		{
			List<Component> tooltip = event.getToolTip();

			ResourceLocation itemRegistryName = ForgeRegistries.ITEMS.getKey(stack.getItem());

			if (stack.getItem() instanceof ArmorItem && Config.Baked.temperatureEnabled) {
				addArmorBaseTemperatureText(itemRegistryName, tooltip);

				addArmorCoatTemperatureText(stack, tooltip);
			}

			// Added Description for coat items.
			if (stack.getItem() instanceof CoatItem && Config.Baked.temperatureEnabled)
				addCoatItemDescText((CoatItem) stack.getItem(), tooltip);

			if (Config.Baked.temperatureEnabled)
				addFoodEffectText(stack, tooltip);
		}
	}


	@SubscribeEvent
	public static void onRenderTooltip(RenderTooltipEvent.GatherComponents event) {
		if (event.isCanceled())
			return;

		Minecraft mc = Minecraft.getInstance();
		Screen gui = mc.screen;
		if (gui == null)
			return;

		if (Config.Baked.thirstEnabled && Config.Baked.showHydrationTooltip)
			addHydrationTooltip(event.getItemStack(), event.getTooltipElements());

		ItemStack stack = event.getItemStack();
		ResourceLocation itemRegistryName = ForgeRegistries.ITEMS.getKey(stack.getItem());
		assert itemRegistryName != null;
		JsonThirst jsonThirst = JsonConfig.consumableThirst.get(itemRegistryName.toString());

		HydrationTooltip hydrationTooltip = null;
		if (jsonThirst != null) {
			hydrationTooltip = new HydrationTooltip(jsonThirst.hydration, jsonThirst.saturation, jsonThirst.dirty);
		} else if (stack.getItem() == Items.POTION) {
			Potion potion = PotionUtils.getPotion(stack);
			if(potion == Potions.WATER || potion == Potions.AWKWARD || potion == Potions.MUNDANE || potion == Potions.THICK)
			{
				hydrationTooltip = new HydrationTooltip(HydrationEnum.NORMAL);
			}
			else if (potion != Potions.EMPTY)
			{
				hydrationTooltip = new HydrationTooltip(HydrationEnum.POTION);
			}
		} else if (stack.getItem() instanceof DrinkItem) {
			HydrationEnum hydrationEnum = ThirstUtil.getHydrationEnumTag(stack);
			if (hydrationEnum != null)
				hydrationTooltip = new HydrationTooltip(hydrationEnum);
		}

		if (hydrationTooltip == null) {
			return;
		}


	}

	private static void addArmorBaseTemperatureText(ResourceLocation itemRegistryName, List<Component> tooltip) {
		float temperature = 0.0f;

		JsonTemperature jsonTemperature = JsonConfig.itemTemperatures.get(itemRegistryName.toString());

		if (jsonTemperature != null)
		{
			temperature = jsonTemperature.temperature;
		}

		Component text;

		if (temperature > 0.0f)
			text = Component.translatable("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".armor.heating");
		else if (temperature < 0.0f)
			text = Component.translatable("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".armor.cooling");
		else
			return;

		String tempTxt = (temperature % 1.0f == 0f ? (int) Math.abs(temperature) : Math.abs(temperature)) + " ";

		text = Component.literal("+")
				.withStyle(ChatFormatting.BLUE)
				.append(tempTxt)
				.append(text);

		tooltip.add(text);
	}

	private static void addArmorCoatTemperatureText(ItemStack stack, List<Component> tooltip) {
		String coatId = TemperatureUtil.getArmorCoatTag(stack);
		CoatEnum coat = CoatEnum.getFromId(coatId);

		Component text;

		if (coat != null && coat.modifier() > 0) {
			text = Component.translatable("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".armor_coat." + coatId);
		}
		else if (coat != null && coat.modifier() == 0)
			text = Component.literal("Error");
		else {
			return;
		}

		text = Component.literal("")
				.withStyle(ChatFormatting.BLUE)
				.append(text);

		tooltip.add(text);
	}

	private static void addCoatItemDescText(CoatItem coatItem, List<Component> tooltip) {

		CoatEnum coat = coatItem.coat;

		Component text;
		if (KeyMappingRegistry.showAddedDesc.isDown()) {
			if (coat != null && coat.modifier() > 0) {
				text = Component.translatable("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".coat_item." + coat.type() + ".desc")
						.append(" " + coat.modifier() + "\u00B0C");
			} else if (coat != null && coat.modifier() == 0)
				text = Component.literal("Error");
			else {
				return;
			}

			text = Component.literal("")
					.withStyle(ChatFormatting.DARK_GRAY)
					.append(text);
		} else {
			text = Component.literal(ChatFormatting.GRAY + I18n.get("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".added_desc.activate", ChatFormatting.LIGHT_PURPLE, I18n.get(KeyMappingRegistry.showAddedDesc.getTranslatedKeyMessage().getString()), ChatFormatting.GRAY));
		}

		tooltip.add(text);
	}

	private static void addFoodEffectText(ItemStack stack, List<Component> tooltip) {
		ResourceLocation itemRegistryName = ForgeRegistries.ITEMS.getKey(stack.getItem());
		assert itemRegistryName != null;
		List<JsonConsumableTemperature> jcts = JsonConfig.consumableTemperature.get(itemRegistryName.toString());

		if (jcts != null) {
			for (JsonConsumableTemperature jct: jcts) {
				MobEffectInstance effectInstance = new MobEffectInstance(jct.getEffect(), jct.duration, Math.abs(jct.temperatureLevel));
				MutableComponent mutableComponent = Component.translatable(effectInstance.getDescriptionId());

				if (Math.abs(jct.temperatureLevel) > 1) {
					mutableComponent = Component.translatable("potion.withAmplifier", mutableComponent, Component.translatable("potion.potency." + (Math.abs(jct.temperatureLevel) - 1)));
				}

				if (jct.duration > 20) {
					mutableComponent = Component.translatable("potion.withDuration", mutableComponent, MobEffectUtil.formatDuration(effectInstance, 1.0f));
				}

				if (jct.getEffect() == MobEffectRegistry.COLD_FOOD.get() || jct.getEffect() == MobEffectRegistry.COLD_DRINK.get())
					tooltip.add(mutableComponent.withStyle(Style.EMPTY.withColor(TextColor.fromRgb(6466303))));

				if (jct.getEffect() == MobEffectRegistry.HOT_FOOD.get() || jct.getEffect() == MobEffectRegistry.HOT_DRINk.get())
					tooltip.add(mutableComponent.withStyle(Style.EMPTY.withColor(TextColor.fromRgb(16420407))));

			}
		}
	}

	private static void addHydrationTooltip(ItemStack stack, List<Either<FormattedText, TooltipComponent>> tooltips) {

		ResourceLocation itemRegistryName = ForgeRegistries.ITEMS.getKey(stack.getItem());
		assert itemRegistryName != null;
		JsonThirst jsonThirst = JsonConfig.consumableThirst.get(itemRegistryName.toString());

		HydrationTooltip hydrationTooltip = null;
		if (jsonThirst != null) {
			hydrationTooltip = new HydrationTooltip(jsonThirst.hydration, jsonThirst.saturation, jsonThirst.dirty);
		} else if (stack.getItem() == Items.POTION) {
			Potion potion = PotionUtils.getPotion(stack);
			if(potion == Potions.WATER || potion == Potions.AWKWARD || potion == Potions.MUNDANE || potion == Potions.THICK)
			{
				hydrationTooltip = new HydrationTooltip(HydrationEnum.NORMAL);
			}
			else if (potion != Potions.EMPTY)
			{
				hydrationTooltip = new HydrationTooltip(HydrationEnum.POTION);
			}
		} else if (stack.getItem() instanceof DrinkItem) {
			HydrationEnum hydrationEnum = ThirstUtil.getHydrationEnumTag(stack);
			if (hydrationEnum != null)
				hydrationTooltip = new HydrationTooltip(hydrationEnum);
		}

		if (hydrationTooltip != null) {
			tooltips.add(Either.right(hydrationTooltip));
		}
	}
}
