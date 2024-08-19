package sfiomn.legendarysurvivaloverhaul.client.tooltips;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.datafixers.util.Either;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
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
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonEffectParameter;
import sfiomn.legendarysurvivaloverhaul.api.item.CoatEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.common.items.CoatItem;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.KeyMappingRegistry;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;

import java.util.ArrayList;
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
			List<Component> tooltips = event.getToolTip();

			if (Config.Baked.temperatureEnabled) {
				addItemBaseTemperatureText(itemRegistryName, tooltips);

				if (stack.getItem() instanceof ArmorItem) {
					addCoatTemperatureText(stack, tooltips);

				} else if (stack.getItem() instanceof CoatItem coatItem) {
					addCoatItemDescText(coatItem, tooltips);
				}

				addFoodEffectText(stack, tooltips);
			}

			if (Config.Baked.localizedBodyDamageEnabled)
				addHealingText(stack, tooltips);
		}
	}

	@SuppressWarnings("unused")
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
	}

	private static void addItemBaseTemperatureText(ResourceLocation itemRegistryName, List<Component> tooltips) {
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

		tooltips.add(text);
	}

	private static void addCoatTemperatureText(ItemStack stack, List<Component> tooltips) {
		String coatId = TemperatureUtil.getArmorCoatTag(stack);
		CoatEnum coat = CoatEnum.getFromId(coatId);

		Component text;

		if (coat != null) {
			text = Component.translatable("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".armor_coat." + coatId);
		} else {
			return;
		}

		text = Component.literal("")
				.withStyle(ChatFormatting.BLUE)
				.append(text);

		tooltips.add(text);
	}

	private static void addCoatItemDescText(CoatItem coatItem, List<Component> tooltips) {

		CoatEnum coat = coatItem.coat;

		Component text;

		if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), KeyMappingRegistry.showAddedDesc.getKey().getValue())) {
			if (coat != null) {
				text = Component.translatable("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".coat_item." + coat.type() + ".desc")
						.append(" " + coat.modifier() + "\u00B0C");
			} else {
				return;
			}

			text = Component.literal("")
					.withStyle(ChatFormatting.DARK_GRAY)
					.append(text);
		} else {
			text = Component.literal(ChatFormatting.GRAY + I18n.get("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".added_desc.activate", ChatFormatting.LIGHT_PURPLE, I18n.get(KeyMappingRegistry.showAddedDesc.getTranslatedKeyMessage().getString()), ChatFormatting.GRAY));
		}

		tooltips.add(text);
	}

	private static void addFoodEffectText(ItemStack stack, List<Component> tooltips) {
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
					tooltips.add(mutableComponent.withStyle(Style.EMPTY.withColor(TextColor.fromRgb(6466303))));

				if (jct.getEffect() == MobEffectRegistry.HOT_FOOD.get() || jct.getEffect() == MobEffectRegistry.HOT_DRINk.get())
					tooltips.add(mutableComponent.withStyle(Style.EMPTY.withColor(TextColor.fromRgb(16420407))));
			}
		}
	}

	private static void addHealingText(ItemStack stack, List<Component> tooltips) {

		ResourceLocation itemRegistryName = ForgeRegistries.ITEMS.getKey(stack.getItem());
		assert itemRegistryName != null;
		JsonConsumableHeal jsonConsumableHeal = JsonConfig.consumableHeal.get(itemRegistryName.toString());

		if (jsonConsumableHeal != null) {
			if (jsonConsumableHeal.healingCharges > 0) {
				tooltips.add(Component.translatable("tooltip.legendarysurvivaloverhaul.body_heal_item.body_part", jsonConsumableHeal.healingCharges));
			} else if (jsonConsumableHeal.healingCharges == 0) {
				tooltips.add(Component.translatable("tooltip.legendarysurvivaloverhaul.body_heal_item.whole_body"));
			}
			tooltips.add(Component.translatable("tooltip.legendarysurvivaloverhaul.body_heal_item.healing_value", jsonConsumableHeal.healingValue, MathUtil.round(jsonConsumableHeal.healingTime / 20.0f, 1)));
		}
	}

	private static void addHydrationTooltip(ItemStack stack, List<Either<FormattedText, TooltipComponent>> tooltips) {

		ResourceLocation itemRegistryName = ForgeRegistries.ITEMS.getKey(stack.getItem());
		assert itemRegistryName != null;
		JsonConsumableThirst jsonConsumableThirst = ThirstUtil.getThirstJsonConfig(itemRegistryName, stack);

		HydrationTooltipComponent hydrationTooltipComponent = null;
		List<MutableComponent> hydrationEffectComponents = new ArrayList<>();

		if (jsonConsumableThirst != null) {
			hydrationTooltipComponent = new HydrationTooltipComponent(jsonConsumableThirst.hydration, jsonConsumableThirst.saturation);
			for (JsonEffectParameter effect: jsonConsumableThirst.effects) {
				if (effect.chance > 0 && effect.duration > 0 && !effect.name.isEmpty()) {
					hydrationEffectComponents.add(getHydrationEffectTooltip(effect.chance, effect.name, effect.amplifier, effect.duration));
				}
			}
		} else {
			HydrationEnum hydrationEnum = ThirstUtil.getHydrationEnumTag(stack);
			if (hydrationEnum != null) {
				hydrationTooltipComponent = new HydrationTooltipComponent(hydrationEnum);
				hydrationEffectComponents.add(getHydrationEffectTooltip(hydrationEnum));
			}
		}

		if (hydrationTooltipComponent != null) {
			tooltips.add(Either.right(hydrationTooltipComponent));
		}

		for (MutableComponent hydrationEffectComponent: hydrationEffectComponents) {
			if (hydrationEffectComponent != null)
				tooltips.add(Either.left(hydrationEffectComponent));
		}
	}

	private static MutableComponent getHydrationEffectTooltip(HydrationEnum hydrationEnum) {
		return getHydrationEffectTooltip(hydrationEnum.getEffectChance(), hydrationEnum.getEffectName(), 0, hydrationEnum.getEffectDuration());
	}

	private static MutableComponent getHydrationEffectTooltip(double effectChance, String effectName, int amplifier, int duration) {
		MobEffect effect = null;
		if (effectName != null && !effectName.isEmpty() && effectChance > 0)
			effect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(effectName));

		if (effect == null)
			return null;

		MobEffectInstance effectInstance = new MobEffectInstance(effect, duration, amplifier, false, true);
		MutableComponent mutableComponent = Component.translatable(effectInstance.getDescriptionId());

		if (effectInstance.getAmplifier() > 1) {
			mutableComponent = Component.translatable("potion.withAmplifier", mutableComponent, Component.translatable("potion.potency." + effectInstance.getAmplifier()));
		}

		if (effectInstance.getDuration() > 20) {
			mutableComponent = Component.translatable("potion.withDuration", mutableComponent, MobEffectUtil.formatDuration(effectInstance, 1.0f));
		}

		if (effectChance < 1)
			mutableComponent = Component.translatable("tooltip.legendarysurvivaloverhaul.potion_with_effectChance", (int) (effectChance*100), mutableComponent);

		return mutableComponent.withStyle(Style.EMPTY.withColor(ChatFormatting.BLUE));
	}
}
