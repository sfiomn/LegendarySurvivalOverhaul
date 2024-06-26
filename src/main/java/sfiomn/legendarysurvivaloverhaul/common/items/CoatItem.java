package sfiomn.legendarysurvivaloverhaul.common.items;

import net.minecraft.world.item.Item;
import sfiomn.legendarysurvivaloverhaul.api.item.CoatEnum;

public class CoatItem extends Item {
    public CoatEnum coat;

    public CoatItem(CoatEnum coat, Item.Properties properties) {
        super(properties);
        this.coat = coat;
    }
}
