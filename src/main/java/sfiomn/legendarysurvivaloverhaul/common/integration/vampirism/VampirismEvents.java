package sfiomn.legendarysurvivaloverhaul.common.integration.vampirism;

import de.teamlapen.vampirism.api.event.FactionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import static de.teamlapen.vampirism.api.VReference.VAMPIRE_FACTION;

public class VampirismEvents {

    @SubscribeEvent
    public static void onFactionChanged(FactionEvent.ChangeLevelOrFaction event) {
        if (LegendarySurvivalOverhaul.vampirismLoaded && event.getNewFaction() == VAMPIRE_FACTION && !Config.Baked.thirstEnabledIfVampire)
            ThirstUtil.deactivateThirst(event.getPlayer().getPlayer());
        else
            ThirstUtil.activateThirst(event.getPlayer().getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (LegendarySurvivalOverhaul.vampirismLoaded && !Config.Baked.thirstEnabledIfVampire && VampirismUtil.isVampire(event.getPlayer())) {
            ThirstUtil.deactivateThirst(event.getPlayer());
        } else {
            ThirstUtil.activateThirst(event.getPlayer());
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!ThirstUtil.isThirstActive(event.getPlayer()))
            ThirstUtil.activateThirst(event.getPlayer());
    }
}
