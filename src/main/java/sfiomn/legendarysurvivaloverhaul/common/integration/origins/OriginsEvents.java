package sfiomn.legendarysurvivaloverhaul.common.integration.origins;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class OriginsEvents {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer()) {
            if (event.player.tickCount % 20 == 0) {
                OriginsUtil.assignOriginsFeatures(event.player);
            }
        }
    }
}
