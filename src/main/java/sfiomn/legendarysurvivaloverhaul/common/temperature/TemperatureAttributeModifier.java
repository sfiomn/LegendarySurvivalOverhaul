package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.registry.AttributeRegistry;

public class TemperatureAttributeModifier extends ModifierBase
{
    public TemperatureAttributeModifier()
    {
        super();
    }

    @Override
    public float getPlayerInfluence(Player player)
    {
        return (float) (player.getAttributeValue(AttributeRegistry.HEATING_TEMPERATURE.get()) + player.getAttributeValue(AttributeRegistry.COOLING_TEMPERATURE.get()));
    }
}
