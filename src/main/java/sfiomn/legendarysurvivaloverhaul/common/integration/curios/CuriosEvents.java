package sfiomn.legendarysurvivaloverhaul.common.integration.curios;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;
import top.theillusivec4.curios.api.event.CurioChangeEvent;
import top.theillusivec4.curios.api.event.CurioEquipEvent;
import top.theillusivec4.curios.api.event.CurioUnequipEvent;

public class CuriosEvents {

    @SubscribeEvent
    public static void onEquipCurio(CurioEquipEvent event) {
        LegendarySurvivalOverhaul.LOGGER.debug("equip curio");
        if (event.getStack().getItem() == ItemRegistry.THERMOMETER.get() && !CuriosUtil.isThermometerEquipped)
            CuriosUtil.isThermometerEquipped = true;
    }

    @SubscribeEvent
    public static void onUnequipCurio(CurioUnequipEvent event) {
        if (event.getStack().getItem() == ItemRegistry.THERMOMETER.get() && CuriosUtil.isThermometerEquipped)
            CuriosUtil.isThermometerEquipped = false;
    }
}
