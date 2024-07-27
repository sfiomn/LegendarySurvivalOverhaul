package sfiomn.legendarysurvivaloverhaul.client.tooltips;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;

public class HydrationTooltipComponent implements TooltipComponent {
    public final int hydration;
    public final float saturation;

    public HydrationTooltipComponent(HydrationEnum hydrationEnum) {
        this(hydrationEnum.getHydration(), (float) hydrationEnum.getSaturation());
    }

    public HydrationTooltipComponent(int hydration, float saturation) {
        this.hydration = hydration;
        this.saturation = saturation;
    }
}

