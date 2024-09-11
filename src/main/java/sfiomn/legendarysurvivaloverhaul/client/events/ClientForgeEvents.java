package sfiomn.legendarysurvivaloverhaul.client.events;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import sereneseasons.api.SSItems;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.client.integration.sereneseasons.RenderSeasonCards;
import sfiomn.legendarysurvivaloverhaul.client.render.*;
import sfiomn.legendarysurvivaloverhaul.client.screens.ClientHooks;
import sfiomn.legendarysurvivaloverhaul.client.effects.TemperatureBreathEffect;
import sfiomn.legendarysurvivaloverhaul.client.sounds.TemperatureBreathSound;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;
import sfiomn.legendarysurvivaloverhaul.network.packets.MessageDrinkWater;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.KeyMappingRegistry;

import java.util.ListIterator;

import static sfiomn.legendarysurvivaloverhaul.common.events.CommonForgeEvents.playerDrinkEffect;
import static sfiomn.legendarysurvivaloverhaul.common.events.CommonForgeEvents.playerGetHydrationEnum;
import static sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsUtil.seasonTooltip;
import static sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsUtil.plantCanGrow;
import static sfiomn.legendarysurvivaloverhaul.util.WorldUtil.timeInGame;

@Mod.EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEvents {

    @SubscribeEvent
    public static void onItemRightClick(PlayerInteractEvent.RightClickItem event){
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        if (LegendarySurvivalOverhaul.sereneSeasonsLoaded && event.getItemStack().getItem() == SSItems.CALENDAR.get()) {
            player.displayClientMessage(seasonTooltip(player.blockPosition(), player.level()), true);
        } else if (event.getItemStack().getItem() == Items.CLOCK) {
            player.displayClientMessage(Component.literal(timeInGame(Minecraft.getInstance())), true);
        } else if (event.getItemStack().getItem() == Items.COMPASS) {
            player.displayClientMessage(Component.literal("XYZ: " + player.blockPosition().getX() +
                    " / " + player.blockPosition().getY() + " / " + player.blockPosition().getZ()), true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onApplyBonemeal(BonemealEvent event)
    {
        Block plant = event.getBlock().getBlock();
        if (event.getEntity() == null || ForgeRegistries.BLOCKS.getKey(plant) == null) {
            return;
        }

        if (LegendarySurvivalOverhaul.sereneSeasonsLoaded && !plantCanGrow(event.getLevel(), event.getPos(), plant)) {
            event.getEntity().displayClientMessage(Component.translatable("message." + LegendarySurvivalOverhaul.MOD_ID + ".bonemeal.not_correct_season"), true);
        }
    }

    @SubscribeEvent
    public static void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        if (shouldApplyThirst(event.getEntity())) {
            // Only run on main hand (otherwise it runs twice)
            if(event.getHand() == InteractionHand.MAIN_HAND)
            {
                HydrationEnum water = playerGetHydrationEnum(event.getEntity());

                if (water != null) {
                    playerDrinkEffect(event.getEntity());
                    MessageDrinkWater messageDrinkToServer = new MessageDrinkWater();
                    NetworkHandler.INSTANCE.sendToServer(messageDrinkToServer);
                }
            }
        }
    }

    @SubscribeEvent
    public static void preRenderGameOverlay(RenderGuiOverlayEvent.Pre event) {
        if (event.getOverlay() == VanillaGuiOverlay.FOOD_LEVEL.type()) {

            // Cancel the vanilla food rendering when cold hunger effect active (the mod redraws a custom food bar)
            if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.hasEffect(MobEffectRegistry.COLD_HUNGER.get()))
                event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRenderOverlayEventHideDebugInfo(CustomizeGuiOverlayEvent.DebugText event) {
        Player player = Minecraft.getInstance().player;
        if (player == null || player.isCreative() || player.isSpectator() || !Config.Baked.hideInfoFromDebug)
            return;

        for (ListIterator<String> it = event.getRight().listIterator(); it.hasNext(); ) {
            String line = it.next();
            if (line.contains("Targeted")) {
                line = line.split(":")[0] + ":";
                it.remove();
                it.add(line);
            }
        }

        event.getLeft().removeIf(textLine -> textLine.startsWith("XYZ:") ||
                textLine.startsWith("Chunk:") ||
                textLine.startsWith("Block:") ||
                textLine.startsWith("Facing:"));
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Player player = Minecraft.getInstance().player;
            if (!Minecraft.getInstance().isPaused() && player != null) {
                if (Config.Baked.temperatureEnabled) {
                    RenderTemperatureGui.updateTimer();
                    RenderTemperatureOverlay.updateTemperatureEffect(player);
                    if (Config.Baked.coldBreathEffectThreshold != -1000)
                        TemperatureBreathEffect.tickPlay(player);
                    if (Config.Baked.breathingSoundEnabled)
                        TemperatureBreathSound.tickPlay(player);
                }
                if (shouldApplyThirst(player) && Config.Baked.lowHydrationEffect) {
                    RenderThirstOverlay.updateThirstEffect(player);
                }
                if (LegendarySurvivalOverhaul.sereneSeasonsLoaded && Config.Baked.seasonCardsEnabled) {
                    RenderSeasonCards.updateSeasonCardFading(player);
                }
                if (Config.Baked.localizedBodyDamageEnabled) {
                    RenderBodyDamageGui.updateFlashingTimer();

                    if (KeyMappingRegistry.showBodyHealth.consumeClick())
                        ClientHooks.openBodyHealthScreen(player);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player != null && event.getStage() == RenderLevelStageEvent.Stage.AFTER_WEATHER
                && Config.Baked.lowHydrationEffect && shouldApplyThirst(player)) {
            RenderThirstOverlay.render(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity().level().isClientSide && LegendarySurvivalOverhaul.sereneSeasonsLoaded)
            RenderSeasonCards.init();
    }

    private static boolean shouldApplyThirst(Player player)
    {
        return Config.Baked.thirstEnabled && ThirstUtil.isThirstActive(player);
    }
}
