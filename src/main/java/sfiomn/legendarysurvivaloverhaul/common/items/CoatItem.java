package sfiomn.legendarysurvivaloverhaul.common.items;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.item.CoatEnum;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.KeyMappingRegistry;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;
import sfiomn.legendarysurvivaloverhaul.util.WorldUtil;

import java.util.List;
import java.util.Objects;

public class CoatItem extends Item {
    public CoatEnum coat;

    public CoatItem(CoatEnum coat, Item.Properties properties) {
        super(properties);
        this.coat = coat;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);

        MutableComponent text;

        if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), KeyMappingRegistry.showAddedDesc.getKey().getValue())) {
            if (this.coat != null) {
                MutableComponent effectComponent = Component.translatable("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".coat_item." + this.coat.type() + ".effect").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(6466303)));
                MutableComponent temperatureComponent;
                if (Config.Baked.renderTemperatureInFahrenheit)
                    temperatureComponent = Component.literal(" " + MathUtil.round(WorldUtil.toFahrenheit((float) this.coat.modifier()) - 32, 1) + "\u00B0F");
                else
                    temperatureComponent = Component.literal(" " + this.coat.modifier() + "\u00B0C");

                if (Objects.equals(coat.type(), "cooling")) {
                    temperatureComponent = temperatureComponent.withStyle(Style.EMPTY.withColor(TextColor.fromRgb(6466303)));
                    effectComponent = effectComponent.withStyle(Style.EMPTY.withColor(TextColor.fromRgb(6466303)));
                } else if (Objects.equals(coat.type(), "heating")) {
                    temperatureComponent = temperatureComponent.withStyle(Style.EMPTY.withColor(TextColor.fromRgb(16420407)));
                    effectComponent = effectComponent.withStyle(Style.EMPTY.withColor(TextColor.fromRgb(16420407)));
                } else {
                    temperatureComponent = temperatureComponent.withStyle(Style.EMPTY.withColor(TextColor.fromRgb(10040319)));
                    effectComponent = effectComponent.withStyle(Style.EMPTY.withColor(TextColor.fromRgb(10040319)));
                }

                text = Component.translatable("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".coat_item.desc", effectComponent).append(temperatureComponent);
            } else {
                return;
            }

        } else {
            text = Component.literal(ChatFormatting.GRAY + I18n.get("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".added_desc.activate", ChatFormatting.LIGHT_PURPLE, I18n.get(KeyMappingRegistry.showAddedDesc.getTranslatedKeyMessage().getString()), ChatFormatting.GRAY));
        }

        tooltipComponents.add(text);
    }
}
