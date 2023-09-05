package sfiomn.legendarysurvivaloverhaul.common.items;

import net.minecraft.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public class LargeCanteenItem extends CanteenItem {

    public LargeCanteenItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getMaxCapacity() {
        return Config.Baked.largeCanteenCapacity;
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        if(ThirstUtil.getCapacityTag(stack) == 0)
            return "item."+ LegendarySurvivalOverhaul.MOD_ID+"."+"large_canteen_empty";

        if (ThirstUtil.getHydrationEnumTag(stack) == HydrationEnum.PURIFIED)
            return "item."+LegendarySurvivalOverhaul.MOD_ID+"."+"large_canteen_purified";
        else
            return "item."+LegendarySurvivalOverhaul.MOD_ID+"."+"large_canteen";
    }
}
