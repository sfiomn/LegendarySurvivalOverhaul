package sfiomn.legendarysurvivaloverhaul.client.tooltips;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;

public class HydrationTooltipComponent implements TooltipComponent {
    public final int hydration;
    public final float saturation;
    public final float dirty;

    public HydrationTooltipComponent(HydrationEnum hydrationEnum) {
        this(hydrationEnum.getHydration(), (float) hydrationEnum.getSaturation(), (float) hydrationEnum.getDirtiness());
    }

    public HydrationTooltipComponent(int hydration, float saturation, float dirty) {
        this.hydration = hydration;
        this.saturation = saturation;
        this.dirty = dirty;
    }
}

