package sfiomn.legendarysurvivaloverhaul.common.temperature.dynamic;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
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

        if (diffToAverage > 0) {
            return (float) Mth.clamp(-player.getAttributeValue(AttributeRegistry.HEAT_RESISTANCE.get()) - player.getAttributeValue(AttributeRegistry.THERMAL_RESISTANCE.get()), -diffToAverage - currentResistance, -currentResistance);
        } else if (diffToAverage < 0) {
            return (float) Mth.clamp(player.getAttributeValue(AttributeRegistry.COLD_RESISTANCE.get()) + player.getAttributeValue(AttributeRegistry.THERMAL_RESISTANCE.get()), diffToAverage - currentResistance, -currentResistance);
        } else
            return 0.0f;
    }
}
