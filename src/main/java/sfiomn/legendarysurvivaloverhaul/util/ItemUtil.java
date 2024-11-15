package sfiomn.legendarysurvivaloverhaul.util;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import sfiomn.legendarysurvivaloverhaul.common.integration.curios.CuriosUtil;

public class ItemUtil {

    private ItemUtil() {}

    public static boolean canBeEquippedInSlot(ItemStack stack, EquipmentSlotType slot) {

        if (stack.getItem() instanceof ArmorItem) {
            return ((ArmorItem) stack.getItem()).getSlot() == slot;
        }

        if (stack.getItem() instanceof ShieldItem) {
            return slot == EquipmentSlotType.OFFHAND;
        }

        if (CuriosUtil.isCuriosItem(stack))
            return false;

        return slot == EquipmentSlotType.MAINHAND;
    }

    public static EquipmentSlotType getEquippableSlot(ItemStack stack) {
        if (stack.getItem() instanceof ArmorItem) {
            return ((ArmorItem) stack.getItem()).getSlot();
        }

        if (stack.getItem() instanceof ShieldItem) {
            return EquipmentSlotType.OFFHAND;
        }

        return EquipmentSlotType.MAINHAND;
    }
}
