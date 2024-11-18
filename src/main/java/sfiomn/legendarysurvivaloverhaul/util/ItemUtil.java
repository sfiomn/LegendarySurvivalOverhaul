package sfiomn.legendarysurvivaloverhaul.util;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import sfiomn.legendarysurvivaloverhaul.common.integration.curios.CuriosUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;

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

    public static String compassLocation(Entity entity) {
        switch (Config.Baked.compassInfoMode) {
            case FULL:
                return "XYZ: " + entity.blockPosition().getX() +
                    " / " + entity.blockPosition().getY() + " / " + entity.blockPosition().getZ();
            case HORIZONTAL:
                return "XZ: " + entity.blockPosition().getX() + " / " + entity.blockPosition().getZ();
            default:
                return "";
        }
    }

    public enum CompassInfo {
        FULL,
        HORIZONTAL,
        NONE
    }
}
