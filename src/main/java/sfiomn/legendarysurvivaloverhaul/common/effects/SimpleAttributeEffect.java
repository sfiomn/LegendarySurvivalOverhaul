package sfiomn.legendarysurvivaloverhaul.common.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class SimpleAttributeEffect extends MobEffect {
    public static final String HOT_FOOD_ATTRIBUTE_UUID = "dbac2b95-f979-4104-a4ba-3039c1015ae7";
    public static final String HOT_DRINK_ATTRIBUTE_UUID = "5f1f294d-86f4-42e8-ae78-67223f29e9e8";
    public static final String COLD_FOOD_ATTRIBUTE_UUID = "344cffd9-9a50-4bd5-9ac1-aa4830c3128a";
    public static final String COLD_DRINK_ATTRIBUTE_UUID = "10db1b22-eda8-4cc2-90df-c1e3b06fa460";

    protected final double amplifierMultiplier;

    public SimpleAttributeEffect(MobEffectCategory pCategory, int pColor, double amplifierMultiplier) {
        super(pCategory, pColor);
        this.amplifierMultiplier = amplifierMultiplier;
    }

    @Override
    public double getAttributeModifierValue(int amplifier, AttributeModifier modifier) {
        return modifier.getAmount() + this.amplifierMultiplier * (double)(amplifier);
    }
}
