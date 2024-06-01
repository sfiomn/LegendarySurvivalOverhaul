package sfiomn.legendarysurvivaloverhaul.client.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

public class ClientHooks {
    public static void openBodyHealthScreen(PlayerEntity player, Hand hand) {
        Minecraft.getInstance().setScreen(new BodyHealthScreen(player, hand));
    }

    public static void openBodyHealthScreen(PlayerEntity player) {
        Minecraft.getInstance().setScreen(new BodyHealthScreen(player));
    }
}
