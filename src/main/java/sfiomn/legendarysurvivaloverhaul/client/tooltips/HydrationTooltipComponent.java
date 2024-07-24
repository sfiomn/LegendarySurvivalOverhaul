package sfiomn.legendarysurvivaloverhaul.client.tooltips;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;

public class HydrationTooltipComponent implements TooltipComponent {
    public final int hydration;
    public final float saturation;
    public final float effectChance;
    public final String effect;

    public HydrationTooltipComponent(HydrationEnum hydrationEnum) {
        this(hydrationEnum.getHydration(), (float) hydrationEnum.getSaturation(),
                (float) hydrationEnum.getEffectChance(), hydrationEnum.getEffect());
    }

    public HydrationTooltipComponent(int hydration, float saturation, float effectChance, String effect) {
        this.hydration = hydration;
        this.saturation = saturation;
        this.effectChance = effectChance;
        this.effect = effect;
    }
}

