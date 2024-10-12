package sfiomn.legendarysurvivaloverhaul.common.temperature.dynamic;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.DynamicModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.registry.AttributeRegistry;

public class ResistanceAttributeModifier extends DynamicModifierBase {

    public ResistanceAttributeModifier()
    {
        super();
    }

    @Override
    public float applyDynamicPlayerInfluence(Player player, float currentTemperature, float currentResistance) {
        float diffToAverage = currentTemperature - TemperatureEnum.NORMAL.getMiddle();

        float effectiveResistance = 0.0f;
        double maxResistance = player.getAttributeValue(AttributeRegistry.THERMAL_RESISTANCE.get());

        if (diffToAverage > 0) {
            maxResistance += player.getAttributeValue(AttributeRegistry.HEAT_RESISTANCE.get());
            effectiveResistance = (float) Mth.clamp(maxResistance, currentResistance, diffToAverage + currentResistance);
            effectiveResistance = -effectiveResistance;
        } else if (diffToAverage < 0) {
            maxResistance += player.getAttributeValue(AttributeRegistry.COLD_RESISTANCE.get());
            diffToAverage = -diffToAverage;
            currentResistance = -currentResistance;
            effectiveResistance = (float) Mth.clamp(maxResistance, currentResistance, diffToAverage + currentResistance);
        }

        return effectiveResistance;
    }
}
