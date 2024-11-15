package sfiomn.legendarysurvivaloverhaul.api.temperature;

import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistryEntry;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonTemperatureResistance;

public class AttributeModifierBase extends ForgeRegistryEntry<AttributeModifierBase> {

    public AttributeModifierBase()
    {
    }

    //  Stack up the temperature values in the JsonItemTemperature object to apply all of them to the player
    //  Will automatically be applied to the preferred equipment slot
    public JsonTemperatureResistance getItemAttributes(ItemStack stack)
    {
        return new JsonTemperatureResistance();
    }

    @Override
    public String toString()
    {
        return this.getRegistryName().toString();
    }
}
