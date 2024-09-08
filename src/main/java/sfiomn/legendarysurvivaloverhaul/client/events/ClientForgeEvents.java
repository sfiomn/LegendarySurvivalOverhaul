package sfiomn.legendarysurvivaloverhaul.client.events;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sereneseasons.api.SSItems;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.client.integration.sereneseasons.RenderSeasonCards;
import sfiomn.legendarysurvivaloverhaul.client.render.*;
import sfiomn.legendarysurvivaloverhaul.client.screens.ClientHooks;
import sfiomn.legendarysurvivaloverhaul.client.effects.TemperatureBreathEffect;
import sfiomn.legendarysurvivaloverhaul.client.sounds.TemperatureBreathSound;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureItemCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.EffectRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.KeybindingRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;
import sfiomn.legendarysurvivaloverhaul.util.WorldUtil;

import java.util.ListIterator;

import static sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsUtil.formatSeasonName;
import static sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsUtil.plantCanGrow;
import static sfiomn.legendarysurvivaloverhaul.util.WorldUtil.timeInGame;

@Mod.EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEvents {
    private static final Minecraft minecraft = Minecraft.getInstance();

    @SubscribeEvent
    public static void onItemRightClick(PlayerInteractEvent.RightClickItem event){
        PlayerEntity player = minecraft.player;
        if (player == null) {
            return;
        }
        if (LegendarySurvivalOverhaul.sereneSeasonsLoaded && event.getItemStack().getItem() == SSItems.calendar) {
            player.displayClientMessage(formatSeasonName(player.blockPosition(), player.level), true);
        } else if (event.getItemStack().getItem() == Items.CLOCK) {
            player.displayClientMessage(new StringTextComponent(timeInGame(Minecraft.getInstance())), true);
        } else if (event.getItemStack().getItem() == Items.COMPASS) {
            player.displayClientMessage(new StringTextComponent("XYZ: " + player.blockPosition().getX() +
                    " / " + player.blockPosition().getY() + " / " + player.blockPosition().getZ()), true);
        }
    }

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL || minecraft.isPaused() || minecraft.level == null || !Minecraft.renderNames()) return;

        MatrixStack matrixStack = event.getMatrixStack();
        PlayerEntity player = Minecraft.getInstance().player;
        if (player != null) {

            Entity entity = WorldUtil.getEntityLookedAt(player, player.getAttributeValue(ForgeMod.REACH_DISTANCE.get()));
            if (entity instanceof ItemFrameEntity && !((ItemFrameEntity) entity).getItem().isEmpty()) {
                Item itemInFrame = ((ItemFrameEntity) entity).getItem().getItem();

                if (LegendarySurvivalOverhaul.sereneSeasonsLoaded && (itemInFrame == SSItems.calendar.getItem() || itemInFrame == ItemRegistry.SEASONAL_CALENDAR.get())) {
                    RenderFrame.render(minecraft, matrixStack, formatSeasonName(entity.blockPosition(), entity.level));
                } else if (itemInFrame == ItemRegistry.THERMOMETER.get()) {
                    TemperatureItemCapability tempItemCap = CapabilityUtil.getTempItemCapability(((ItemFrameEntity) entity).getItem());
                    RenderFrame.render(minecraft, matrixStack, new StringTextComponent(tempItemCap.getWorldTemperatureLevel() + "\u00B0C"));
                } else if (itemInFrame == Items.COMPASS) {
                    RenderFrame.render(minecraft, matrixStack, new StringTextComponent("XYZ: " + entity.blockPosition().getX() +
                            " / " + entity.blockPosition().getY() + " / " + entity.blockPosition().getZ()));
                } else if (itemInFrame == Items.CLOCK) {
                    RenderFrame.render(minecraft, matrixStack, new StringTextComponent(timeInGame(minecraft)));
                }
            }
        }

        if (LegendarySurvivalOverhaul.sereneSeasonsLoaded && Config.Baked.seasonCardsEnabled) {
            int scaledWidth = minecraft.getWindow().getGuiScaledWidth();
            int scaledHeight = minecraft.getWindow().getGuiScaledHeight();

            RenderSeasonCards.render(matrixStack, scaledWidth, scaledHeight);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onApplyBonemeal(BonemealEvent event)
    {
        Block plant = event.getBlock().getBlock();
        if (event.getPlayer() == null || plant.getRegistryName() == null) {
            return;
        }
        if (LegendarySurvivalOverhaul.sereneSeasonsLoaded && !plantCanGrow(event.getWorld(), event.getPos(), plant)) {
            event.getPlayer().displayClientMessage(new TranslationTextComponent("message." + LegendarySurvivalOverhaul.MOD_ID + ".bonemeal.not_correct_season"), true);
        }
    }

    @SubscribeEvent
    public static void onKeyPress(InputEvent event) {
        if (KeybindingRegistry.showBodyHealth != null && KeybindingRegistry.showBodyHealth.consumeClick()) {
            ClientHooks.openBodyHealthScreen(Minecraft.getInstance().player);
        }
    }

    @SubscribeEvent
    public static void preRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.FOOD) {

            // Cancel the vanilla food rendering when cold hunger effect active (the mod redraws a custom food bar)
            if (minecraft.player != null && minecraft.player.hasEffect(EffectRegistry.COLD_HUNGER.get()))
                ForgeIngameGui.renderFood = false;
        }
    }

    @SubscribeEvent
    public static void onRenderGameOverlayEffects(RenderGameOverlayEvent.Post event) {
        if (event.isCanceled() || minecraft.gameMode == null || !minecraft.gameMode.hasExperience() || minecraft.player == null) return;

        int scaledWidth = minecraft.getWindow().getGuiScaledWidth();
        int scaledHeight = minecraft.getWindow().getGuiScaledHeight();

        if (event.getType() == RenderGameOverlayEvent.ElementType.FOOD && !minecraft.options.hideGui) {

            if (shouldApplyThirst(minecraft.player)) {
                RenderThirstGui.render(event.getMatrixStack(), minecraft.player, scaledWidth, scaledHeight);
            }

            if (Config.Baked.localizedBodyDamageEnabled) {
                RenderBodyDamageGui.render(event.getMatrixStack(), minecraft.player, scaledWidth, scaledHeight);
            }

            if (Config.Baked.temperatureEnabled) {
                RenderTemperatureGui.renderFoodBarEffect(event.getMatrixStack(), minecraft.player, scaledWidth, scaledHeight);
            }

            if (Config.Baked.temperatureEnabled) {
                RenderTemperatureGui.render(event.getMatrixStack(), minecraft.player, scaledWidth, scaledHeight);
            }
        }

        //  Render overlay after all rendering
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;

        if (Config.Baked.temperatureEnabled) {
            RenderTemperatureOverlay.render(event.getMatrixStack(), scaledWidth, scaledHeight);
        }
    }

    @SubscribeEvent
    public static void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.END || minecraft.player == null)
            return;

        if (shouldApplyThirst(minecraft.player) && Config.Baked.lowHydrationEffect) {
            try {
                RenderThirstOverlay.render();
            } catch (Exception ignored) {}
        } else {
            RenderThirstOverlay.stopRender();
        }
    }

    @SubscribeEvent
    public static void onRenderOverlayEventHideDebugInfo(RenderGameOverlayEvent.Text event) {
        PlayerEntity player = minecraft.player;
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
            if (!minecraft.isPaused() && minecraft.player != null) {
                if (Config.Baked.temperatureEnabled) {
                    RenderTemperatureGui.updateTimer();
                    RenderTemperatureOverlay.updateTemperatureEffect(minecraft.player);
                    if (Config.Baked.coldBreathEffectThreshold != -1000)
                        TemperatureBreathEffect.tickPlay(minecraft.player);
                    if (Config.Baked.breathingSoundEnabled)
                        TemperatureBreathSound.tickPlay(minecraft.player);
                }
                if (shouldApplyThirst(minecraft.player)) {
                    RenderThirstGui.updateTimer();
                    if (Config.Baked.lowHydrationEffect)
                        RenderThirstOverlay.updateThirstEffect(minecraft.player);
                }
                if (LegendarySurvivalOverhaul.sereneSeasonsLoaded && Config.Baked.seasonCardsEnabled) {
                    RenderSeasonCards.updateSeasonCardFading(minecraft.player);
                }
                if (Config.Baked.localizedBodyDamageEnabled) {
                    RenderBodyDamageGui.updateTimer();
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getPlayer().level.isClientSide && LegendarySurvivalOverhaul.sereneSeasonsLoaded)
            RenderSeasonCards.init();
    }

    private static boolean shouldApplyThirst(PlayerEntity player)
    {
        return Config.Baked.thirstEnabled && ThirstUtil.isThirstActive(player);
    }
}
