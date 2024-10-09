package sfiomn.legendarysurvivaloverhaul.common.events;


import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.registry.AttributeRegistry;

@Mod.EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonModBusEvents {

    @SubscribeEvent
    public static void onEntityAttributesChange(EntityAttributeModificationEvent event) {
        if (!event.has(EntityType.PLAYER, AttributeRegistry.HEATING_TEMPERATURE.get())) {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.HEATING_TEMPERATURE.get()
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.COOLING_TEMPERATURE.get())) {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.COOLING_TEMPERATURE.get()
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.HEAT_RESISTANCE.get())) {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.HEAT_RESISTANCE.get()
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.COLD_RESISTANCE.get())) {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.COLD_RESISTANCE.get()
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.THERMAL_RESISTANCE.get())) {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.THERMAL_RESISTANCE.get()
            );
        }
    }
}
