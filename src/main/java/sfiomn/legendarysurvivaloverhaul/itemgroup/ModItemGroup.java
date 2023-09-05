package sfiomn.legendarysurvivaloverhaul.itemgroup;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;

public class ModItemGroup {

    public static final ItemGroup LEGENDARY_SURVIVAL_OVERHAUL_GROUP = new ItemGroup("legendarysurvivaloverhaul") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemRegistry.THERMOMETER.get());
        }
    };
}
