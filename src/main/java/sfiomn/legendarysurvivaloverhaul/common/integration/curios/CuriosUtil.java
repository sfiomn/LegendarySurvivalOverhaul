package sfiomn.legendarysurvivaloverhaul.common.integration.curios;

import net.minecraft.world.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class CuriosUtil {
    public static boolean isCuriosItem(ItemStack stack) {
        if (LegendarySurvivalOverhaul.curiosLoaded) {
            return stack.getItem() instanceof ICurioItem;
        }
        return false;
    }
}
