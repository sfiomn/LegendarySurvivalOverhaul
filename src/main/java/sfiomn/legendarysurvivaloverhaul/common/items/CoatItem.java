package sfiomn.legendarysurvivaloverhaul.common.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.item.CoatEnum;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.KeybindingRegistry;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;
import sfiomn.legendarysurvivaloverhaul.util.WorldUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class CoatItem extends Item {
    public CoatEnum coat;

    public CoatItem(CoatEnum coat, Properties properties) {
        super(properties);
        this.coat = coat;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltips, ITooltipFlag isAdvanced) {
        super.appendHoverText(stack, world, tooltips, isAdvanced);

        if (world == null || !world.isClientSide)
            return;

        ITextComponent text;
        if (InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), KeybindingRegistry.showAddedDesc.getKey().getValue())) {
            IFormattableTextComponent effectComponent = new TranslationTextComponent("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".coat_item." + this.coat.type() + ".effect").withStyle(Style.EMPTY.withColor(Color.fromRgb(6466303)));
            IFormattableTextComponent temperatureComponent;
            if (Config.Baked.renderTemperatureInFahrenheit)
                temperatureComponent = new StringTextComponent(" " + MathUtil.round(WorldUtil.toFahrenheit((float) this.coat.modifier()) - 32, 1) + "\u00B0F");
            else
                temperatureComponent = new StringTextComponent(" " + this.coat.modifier() + "\u00B0C");

            if (Objects.equals(coat.type(), "cooling")) {
                temperatureComponent = temperatureComponent.withStyle(Style.EMPTY.withColor(Color.fromRgb(6466303)));
                effectComponent = effectComponent.withStyle(Style.EMPTY.withColor(Color.fromRgb(6466303)));
            } else if (Objects.equals(coat.type(), "heating")) {
                temperatureComponent = temperatureComponent.withStyle(Style.EMPTY.withColor(Color.fromRgb(16420407)));
                effectComponent = effectComponent.withStyle(Style.EMPTY.withColor(Color.fromRgb(16420407)));
            } else {
                temperatureComponent = temperatureComponent.withStyle(Style.EMPTY.withColor(Color.fromRgb(10040319)));
                effectComponent = effectComponent.withStyle(Style.EMPTY.withColor(Color.fromRgb(10040319)));
            }

            text = new TranslationTextComponent("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".coat_item.desc", effectComponent).append(temperatureComponent);

        } else {
            text = new StringTextComponent(TextFormatting.GRAY + I18n.get("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".added_desc.activate", TextFormatting.LIGHT_PURPLE, I18n.get(KeybindingRegistry.showAddedDesc.getTranslatedKeyMessage().getString()), TextFormatting.GRAY));
        }

        tooltips.add(text);
    }
}
