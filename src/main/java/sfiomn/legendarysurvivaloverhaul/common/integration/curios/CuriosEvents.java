package sfiomn.legendarysurvivaloverhaul.common.integration.curios;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;
import top.theillusivec4.curios.api.event.CurioAttributeModifierEvent;
import top.theillusivec4.curios.api.event.CurioDropsEvent;
import top.theillusivec4.curios.api.event.CurioEquipEvent;
import top.theillusivec4.curios.api.event.CurioUnequipEvent;


public class CuriosEvents {

    @SubscribeEvent
    public static void onEquipCurio(CurioEquipEvent event) {
        if (event.getStack().is(ItemRegistry.THERMOMETER.get()))
            CuriosUtil.isThermometerEquipped = true;
    }

    @SubscribeEvent
    public static void onUnequipCurio(CurioUnequipEvent event) {
        if (event.getStack().is(ItemRegistry.THERMOMETER.get()))
            CuriosUtil.isThermometerEquipped = false;
    }

    @SubscribeEvent
    public static void onDropCurio(CurioDropsEvent event) {
        if (event.getDrops().stream().anyMatch(itemEntity -> itemEntity.getItem().is(ItemRegistry.THERMOMETER.get())))
            CuriosUtil.isThermometerEquipped = false;
    }

    @SubscribeEvent
    public static void onCurioAttributeModifierEvent(CurioAttributeModifierEvent event) {
        CuriosModifier.addAttribute(event);
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        CuriosUtil.isThermometerEquipped = CuriosUtil.isCurioItemEquipped(event.getEntity(), ItemRegistry.THERMOMETER.get());
    }
}
