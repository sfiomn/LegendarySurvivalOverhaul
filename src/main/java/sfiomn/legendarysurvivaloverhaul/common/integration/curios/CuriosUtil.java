package sfiomn.legendarysurvivaloverhaul.common.integration.curios;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class CuriosUtil {
    public static boolean isThermometerEquipped = false;

    public static boolean isCuriosItem(ItemStack stack) {
        if (LegendarySurvivalOverhaul.curiosLoaded) {
            return stack.getItem() instanceof ICurioItem;
        }
        return false;
    }

    public static boolean isCurioItemEquipped(PlayerEntity player, Item item) {
        if (LegendarySurvivalOverhaul.curiosLoaded) {
            List<SlotResult> slotResults = CuriosApi.getCuriosHelper().findCurios(player, item);

            return !slotResults.isEmpty();
        }
        return false;
    }
}
