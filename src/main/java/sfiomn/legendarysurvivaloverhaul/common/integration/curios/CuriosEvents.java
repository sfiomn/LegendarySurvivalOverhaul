package sfiomn.legendarysurvivaloverhaul.common.integration.curios;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;
import top.theillusivec4.curios.api.event.CurioDropsEvent;
import top.theillusivec4.curios.api.event.CurioEquipEvent;
import top.theillusivec4.curios.api.event.CurioUnequipEvent;

public class CuriosEvents {

    @SubscribeEvent
    public static void onEquipCurio(CurioEquipEvent event) {
        if (event.getStack().is(ItemRegistry.THERMOMETER.get()) && !CuriosUtil.isThermometerEquipped)
            CuriosUtil.isThermometerEquipped = true;
    }

    @SubscribeEvent
    public static void onUnequipCurio(CurioUnequipEvent event) {
        if (event.getStack().is(ItemRegistry.THERMOMETER.get()) && CuriosUtil.isThermometerEquipped)
            CuriosUtil.isThermometerEquipped = false;
    }

    @SubscribeEvent
    public static void onDropCurio(CurioDropsEvent event) {
        if (CuriosUtil.isThermometerEquipped)
            CuriosUtil.isThermometerEquipped = false;
    }
}
