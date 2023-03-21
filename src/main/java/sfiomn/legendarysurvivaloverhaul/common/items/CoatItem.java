package sfiomn.legendarysurvivaloverhaul.common.items;

import net.minecraft.item.Item;
import sfiomn.legendarysurvivaloverhaul.api.item.CoatEnum;

public class CoatItem extends Item {
    public CoatEnum coat;

    public CoatItem(CoatEnum coat, Properties properties) {
        super(properties);
        this.coat = coat;
    }
}
