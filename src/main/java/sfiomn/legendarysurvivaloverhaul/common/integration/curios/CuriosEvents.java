package sfiomn.legendarysurvivaloverhaul.common.integration.curios;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.event.CurioDropsEvent;
import top.theillusivec4.curios.api.event.CurioEquipEvent;
import top.theillusivec4.curios.api.event.CurioUnequipEvent;

public class CuriosEvents {

    @SubscribeEvent
    public static void onEquipCurio(CurioEquipEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            CuriosModifier.addAttribute((PlayerEntity) event.getEntityLiving(),
                    ForgeRegistries.ITEMS.getKey(event.getStack().getItem()));
        }
    }

    @SubscribeEvent
    public static void onUnequipCurio(CurioUnequipEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            CuriosModifier.removeAttribute((PlayerEntity) event.getEntityLiving(),
                    ForgeRegistries.ITEMS.getKey(event.getStack().getItem()));
        }
    }

    @SubscribeEvent
    public static void onDropCurio(CurioDropsEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            for (ItemEntity itemEntity : event.getDrops()) {
                CuriosModifier.removeAttribute((PlayerEntity) event.getEntityLiving(),
                        ForgeRegistries.ITEMS.getKey(itemEntity.getItem().getItem()));
            }
        }
    }
}
