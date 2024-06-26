package sfiomn.legendarysurvivaloverhaul.client.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;

public class ClientHooks {
    public static void openBodyHealthScreen(Player player, InteractionHand hand) {
        Minecraft.getInstance().setScreen(new BodyHealthScreen(player, hand));
    }

    public static void openBodyHealthScreen(Player player) {
        Minecraft.getInstance().setScreen(new BodyHealthScreen(player));
    }
}
