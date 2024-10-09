package sfiomn.legendarysurvivaloverhaul.util;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import sfiomn.legendarysurvivaloverhaul.common.integration.curios.CuriosUtil;

public class ItemUtil {

    private ItemUtil() {}

    public static boolean canBeEquippedInSlot(ItemStack stack, EquipmentSlot slot) {
        if (stack.getItem() instanceof ArmorItem armorItem) {
            return armorItem.getEquipmentSlot() == slot;
        }

        if (stack.getItem() instanceof ShieldItem) {
            return slot == EquipmentSlot.OFFHAND;
        }

        if (CuriosUtil.isCuriosItem(stack))
            return false;

        return slot == EquipmentSlot.MAINHAND;
    }

    public static EquipmentSlot getEquippableSlot(ItemStack stack) {
        if (stack.getItem() instanceof ArmorItem armorItem) {
            return armorItem.getEquipmentSlot();
        }

        if (stack.getItem() instanceof ShieldItem) {
            return EquipmentSlot.OFFHAND;
        }

        return EquipmentSlot.MAINHAND;
    }
}
