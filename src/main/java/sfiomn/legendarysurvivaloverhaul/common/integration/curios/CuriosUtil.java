package sfiomn.legendarysurvivaloverhaul.common.integration.curios;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.ISlotType;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.Map;

public class CuriosUtil {
    public static boolean isCuriosItem(ItemStack stack) {
        if (LegendarySurvivalOverhaul.curiosLoaded) {
            return stack.getItem() instanceof ICurioItem;
        }
        return false;
    }

    public static boolean isCurioItemEquippedInSlot(Player player, Item item, String slotIdentifier) {
        if (LegendarySurvivalOverhaul.curiosLoaded) {
            LazyOptional<ICuriosItemHandler> curiosInventory = CuriosApi.getCuriosInventory(player);

            if (curiosInventory.isPresent() && curiosInventory.resolve().isPresent()) {
                ICurioStacksHandler slotInventory = curiosInventory.resolve().get().getCurios().get(slotIdentifier);

                for (int i = 0; i < slotInventory.getStacks().getSlots(); i++) {
                    ItemStack stack = slotInventory.getStacks().getStackInSlot(i);
                    if (stack.is(item))
                        return true;
                }
            }
        }
        return false;
    }
}
