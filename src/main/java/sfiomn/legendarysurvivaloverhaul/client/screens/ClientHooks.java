package sfiomn.legendarysurvivaloverhaul.client.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

public class ClientHooks {
    public static void openBodyHealthScreen(PlayerEntity player, Hand hand, boolean alreadyConsumed, int healingCharges, float healingValue, int healingTime) {
        Minecraft.getInstance().setScreen(new BodyHealthScreen(player, hand, alreadyConsumed, healingCharges, healingValue, healingTime));
    }

    public static void openBodyHealthScreen(PlayerEntity player) {
        openBodyHealthScreen(player, null, false, 0, 0, 0);
    }
}
