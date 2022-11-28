package sfiomn.legendarysurvivaloverhaul.client;

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
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;

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
			else
			{
				return;
			}
			
			ITextComponent tempTranslation;
			
			if (temperature > 0.0f)
				tempTranslation = new TranslationTextComponent("legendarysurvivaloverhaul.armorTooltip.heating");
			else if (temperature < 0.0f)
				tempTranslation = new TranslationTextComponent("legendarysurvivaloverhaul.armorTooltip.cooling");
			else 
				tempTranslation = new StringTextComponent("Error");
			
			if (temperature != 0.0f)
			{
				String tempTxt = (temperature % 1.0f == 0f ? (int) Math.abs(temperature) : Math.abs(temperature) ) + " ";
				
				ITextComponent text = new StringTextComponent("+")
						.withStyle(TextFormatting.BLUE)
						.append(tempTxt)
						.append(tempTranslation);

				tooltip.add(text);
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
}
