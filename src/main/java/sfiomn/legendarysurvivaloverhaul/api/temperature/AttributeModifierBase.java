package sfiomn.legendarysurvivaloverhaul.api.temperature;

import net.minecraft.world.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonTemperatureResistance;

public class AttributeModifierBase {

    public AttributeModifierBase()
    {
    }

    //  Stack up the temperature values in the JsonItemTemperature object to apply all of them to the player
    //  Will automatically be applied to the preferred equipment slot
    public JsonTemperatureResistance getItemAttributes(ItemStack stack)
    {
        return new JsonTemperatureResistance();
    }
}
