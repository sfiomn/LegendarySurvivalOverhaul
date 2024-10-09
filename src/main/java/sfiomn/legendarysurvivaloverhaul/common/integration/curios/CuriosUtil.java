package sfiomn.legendarysurvivaloverhaul.common.integration.curios;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.loading.FMLLoader;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public class CuriosUtil {
    public static boolean isThermometerEquipped = false;

    public static boolean isCurioItemEquipped(Player player, Item item) {
        if (LegendarySurvivalOverhaul.curiosLoaded) {
            LazyOptional<ICuriosItemHandler> curiosInventory = CuriosApi.getCuriosInventory(player);

            if (curiosInventory.isPresent() && curiosInventory.resolve().isPresent()) {
                return curiosInventory.resolve().get().isEquipped(item);
            }
        }
        return false;
    }

    public static boolean isCuriosItem(ItemStack stack) {
        return LegendarySurvivalOverhaul.curiosLoaded && !CuriosApi.getItemStackSlots(stack, FMLLoader.getDist() == Dist.CLIENT).values().isEmpty();
    }
}
