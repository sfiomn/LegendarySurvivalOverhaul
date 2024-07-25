package sfiomn.legendarysurvivaloverhaul.client.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;

public class ClientHooks {
    public static void openBodyHealthScreen(Player player, InteractionHand hand, boolean alreadyConsumed, int healingCharges, float healingValue, int healingTime) {
        Minecraft.getInstance().setScreen(new BodyHealthScreen(player, hand, alreadyConsumed, healingCharges, healingValue, healingTime));
    }

    public static void openBodyHealthScreen(Player player) {
        openBodyHealthScreen(player, null, false, 0, 0, 0);
    }
}
