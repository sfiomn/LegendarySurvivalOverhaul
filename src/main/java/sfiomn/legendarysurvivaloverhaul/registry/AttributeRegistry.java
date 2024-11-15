package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

public class AttributeRegistry {
    public static DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(ForgeRegistries.ATTRIBUTES, LegendarySurvivalOverhaul.MOD_ID);

    public static final RegistryObject<Attribute> HEATING_TEMPERATURE = ATTRIBUTES.register("heating_temperature", () -> new RangedAttribute("attribute.name." + LegendarySurvivalOverhaul.MOD_ID + ".heating_temperature", 0.0, -10000, 10000).setSyncable(true));
    public static final RegistryObject<Attribute> COOLING_TEMPERATURE = ATTRIBUTES.register("cooling_temperature", () -> new RangedAttribute("attribute.name." + LegendarySurvivalOverhaul.MOD_ID + ".cooling_temperature", 0.0, -10000, 10000).setSyncable(true));
    public static final RegistryObject<Attribute> HEAT_RESISTANCE = ATTRIBUTES.register("heat_resistance", () -> new RangedAttribute("attribute.name." + LegendarySurvivalOverhaul.MOD_ID + ".heat_resistance", 0.0, 0.0, 10000.0).setSyncable(true));
    public static final RegistryObject<Attribute> COLD_RESISTANCE = ATTRIBUTES.register("cold_resistance", () -> new RangedAttribute("attribute.name." + LegendarySurvivalOverhaul.MOD_ID + ".cold_resistance", 0.0, 0.0, 10000.0).setSyncable(true));
    public static final RegistryObject<Attribute> THERMAL_RESISTANCE = ATTRIBUTES.register("thermal_resistance", () -> new RangedAttribute("attribute.name." + LegendarySurvivalOverhaul.MOD_ID + ".thermal_resistance", 0.0, 0.0, 10000.0).setSyncable(true));

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }
}
