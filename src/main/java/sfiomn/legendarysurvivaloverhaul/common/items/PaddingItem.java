package sfiomn.legendarysurvivaloverhaul.common.items;

import net.minecraft.item.Item;
import sfiomn.legendarysurvivaloverhaul.api.item.PaddingEnum;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;

public class PaddingItem extends Item {
    public PaddingEnum padding;

    public PaddingItem(PaddingEnum padding, Properties properties) {
        super(properties);
        this.padding = padding;
    }

    public static PaddingItem getItemPadding(PaddingEnum padding) {
        if (padding == PaddingEnum.COOLING_1) {
            return (PaddingItem) ItemRegistry.COOLER_PADDING_1.get();

        } else if (padding == PaddingEnum.COOLING_2) {
            return (PaddingItem) ItemRegistry.COOLER_PADDING_2.get();

        } else if (padding == PaddingEnum.COOLING_3) {
            return (PaddingItem) ItemRegistry.COOLER_PADDING_3.get();

        } else if (padding == PaddingEnum.HEATING_1) {
            return (PaddingItem) ItemRegistry.HEATER_PADDING_1.get();

        } else if (padding == PaddingEnum.HEATING_2) {
            return (PaddingItem) ItemRegistry.HEATER_PADDING_2.get();

        } else if (padding == PaddingEnum.HEATING_3) {
            return (PaddingItem) ItemRegistry.HEATER_PADDING_3.get();

        } else if (padding == PaddingEnum.THERMAL_1) {
            return (PaddingItem) ItemRegistry.THERMAL_PADDING_1.get();

        } else if (padding == PaddingEnum.THERMAL_2) {
            return (PaddingItem) ItemRegistry.THERMAL_PADDING_2.get();

        } else {
            return (PaddingItem) ItemRegistry.THERMAL_PADDING_3.get();
        }
    }
}
