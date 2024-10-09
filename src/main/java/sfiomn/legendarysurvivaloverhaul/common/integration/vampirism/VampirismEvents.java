package sfiomn.legendarysurvivaloverhaul.common.integration.vampirism;

import de.teamlapen.vampirism.api.event.PlayerFactionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;

import static de.teamlapen.vampirism.api.VReference.VAMPIRE_FACTION;

public class VampirismEvents {

    @SubscribeEvent
    public static void onFactionChanged(PlayerFactionEvent.FactionLevelChanged.FactionLevelChangePre event) {
        if (LegendarySurvivalOverhaul.vampirismLoaded && event.getNewFaction() == VAMPIRE_FACTION && !Config.Baked.thirstEnabledIfVampire)
            ThirstUtil.deactivateThirst(event.getPlayer().getPlayer());
        else
            ThirstUtil.activateThirst(event.getPlayer().getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (LegendarySurvivalOverhaul.vampirismLoaded && !Config.Baked.thirstEnabledIfVampire && VampirismUtil.isVampire(event.getEntity())) {
            ThirstUtil.deactivateThirst(event.getEntity());
        } else {
            ThirstUtil.activateThirst(event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!ThirstUtil.isThirstActive(event.getEntity()))
            ThirstUtil.activateThirst(event.getEntity());
    }
}
