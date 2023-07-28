package sfiomn.legendarysurvivaloverhaul.common.items;

import net.minecraft.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstEnum;
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

        if (ThirstUtil.getThirstEnumTag(stack) == ThirstEnum.PURIFIED)
            return "item."+LegendarySurvivalOverhaul.MOD_ID+"."+"large_canteen_purified";
        else
            return "item."+LegendarySurvivalOverhaul.MOD_ID+"."+"large_canteen";
    }
}
