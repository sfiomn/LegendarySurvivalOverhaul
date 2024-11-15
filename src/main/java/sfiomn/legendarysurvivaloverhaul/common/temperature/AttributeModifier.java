package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.entity.player.PlayerEntity;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.registry.AttributeRegistry;

public class AttributeModifier extends ModifierBase
{
    public AttributeModifier()
    {
        super();
    }

    @Override
    public float getPlayerInfluence(PlayerEntity player)
    {
        return (float) (player.getAttributeValue(AttributeRegistry.HEATING_TEMPERATURE.get()) + player.getAttributeValue(AttributeRegistry.COOLING_TEMPERATURE.get()));
    }
}
