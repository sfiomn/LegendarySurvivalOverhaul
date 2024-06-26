package sfiomn.legendarysurvivaloverhaul.client.events;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import sereneseasons.api.SSItems;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.client.integration.sereneseasons.RenderSeasonCards;
import sfiomn.legendarysurvivaloverhaul.client.render.*;
import sfiomn.legendarysurvivaloverhaul.client.screens.ClientHooks;
import sfiomn.legendarysurvivaloverhaul.client.sounds.TemperatureEffectSound;
import sfiomn.legendarysurvivaloverhaul.client.tooltips.HydrationTooltip;
import sfiomn.legendarysurvivaloverhaul.client.tooltips.HydrationTooltipClient;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureItemCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.KeyMappingRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;
import sfiomn.legendarysurvivaloverhaul.util.WorldUtil;

import java.util.ListIterator;

import static sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsUtil.formatSeasonName;
import static sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsUtil.plantCanGrow;
import static sfiomn.legendarysurvivaloverhaul.util.WorldUtil.timeInGame;

@Mod.EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ModClientEvents {
    @SubscribeEvent
    public static void onItemRightClick(PlayerInteractEvent.RightClickItem event){
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        if (LegendarySurvivalOverhaul.sereneSeasonsLoaded && event.getItemStack().getItem() == SSItems.CALENDAR.get()) {
            player.displayClientMessage(formatSeasonName(player.blockPosition(), player.level()), true);
        } else if (event.getItemStack().getItem() == Items.CLOCK) {
            player.displayClientMessage(Component.literal(timeInGame(Minecraft.getInstance())), true);
        } else if (event.getItemStack().getItem() == Items.COMPASS) {
            player.displayClientMessage(Component.literal("XYZ: " + player.blockPosition().getX() +
                    " / " + player.blockPosition().getY() + " / " + player.blockPosition().getZ()), true);
        }
    }

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay() != VanillaGuiOverlay.ITEM_NAME.type() || Minecraft.getInstance().isPaused() || Minecraft.getInstance().level == null || !Minecraft.renderNames()) return;

        Player player = Minecraft.getInstance().player;
        if (player != null) {

            Entity entity = WorldUtil.getEntityLookedAt(player, player.getAttributeValue(ForgeMod.ENTITY_REACH.get()));
            if (entity instanceof ItemFrame && !((ItemFrame) entity).getItem().isEmpty()) {
                Item itemInFrame = ((ItemFrame) entity).getItem().getItem();

                if (LegendarySurvivalOverhaul.sereneSeasonsLoaded && (itemInFrame == SSItems.CALENDAR.get() || itemInFrame == ItemRegistry.SEASONAL_CALENDAR.get())) {
                    RenderFrame.render(Minecraft.getInstance(), event.getGuiGraphics(), formatSeasonName(entity.blockPosition(), entity.level()));
                } else if (itemInFrame == ItemRegistry.THERMOMETER.get()) {
                    TemperatureItemCapability tempItemCap = CapabilityUtil.getTempItemCapability(((ItemFrame) entity).getItem());
                    RenderFrame.render(Minecraft.getInstance(), event.getGuiGraphics(), Component.literal(tempItemCap.getWorldTemperatureLevel() + "\u00B0C"));
                } else if (itemInFrame == Items.COMPASS) {
                    RenderFrame.render(Minecraft.getInstance(), event.getGuiGraphics(), Component.literal("XYZ: " + entity.blockPosition().getX() +
                            " / " + entity.blockPosition().getY() + " / " + entity.blockPosition().getZ()));
                } else if (itemInFrame == Items.CLOCK) {
                    RenderFrame.render(Minecraft.getInstance(), event.getGuiGraphics(), Component.literal(timeInGame(Minecraft.getInstance())));
                }
            }
        }

        if (LegendarySurvivalOverhaul.sereneSeasonsLoaded && Config.Baked.seasonCardsEnabled) {
            int scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int scaledHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

            RenderSeasonCards.render(event.getGuiGraphics(), scaledWidth, scaledHeight);
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
    public static void onKeyPress(InputEvent event) {
        if (KeyMappingRegistry.showBodyHealth != null && KeyMappingRegistry.showBodyHealth.consumeClick()) {
            ClientHooks.openBodyHealthScreen(Minecraft.getInstance().player);
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
    public static void onRenderGameOverlayEffects(RenderGuiOverlayEvent.Post event) {
        if (Minecraft.getInstance().gameMode == null || !Minecraft.getInstance().gameMode.hasExperience()) return;

        int scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int scaledHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        if (event.getOverlay() == VanillaGuiOverlay.FOOD_LEVEL.type()) {

            if (Config.Baked.thirstEnabled) {
                if (!Minecraft.getInstance().options.hideGui) {
                    RenderThirstGui.render(event.getGuiGraphics(), Minecraft.getInstance().player, scaledWidth, scaledHeight);
                }
            }

            if (Config.Baked.localizedBodyDamageEnabled) {
                if (!Minecraft.getInstance().options.hideGui) {
                    RenderBodyDamageGui.render(event.getGuiGraphics(), Minecraft.getInstance().player, scaledWidth, scaledHeight);
                }
            }
        }

        //  Render overlay after all rendering
        if (event.getOverlay() == VanillaGuiOverlay.ITEM_NAME.type()) {
            if (Config.Baked.temperatureEnabled) {
                if (!Minecraft.getInstance().options.hideGui) {
                    RenderTemperatureGui.render(event.getGuiGraphics(), Minecraft.getInstance().player, scaledWidth, scaledHeight);
                }
                RenderTemperatureOverlay.render(event.getGuiGraphics(), scaledWidth, scaledHeight);
            }
        }
    }

    @SubscribeEvent
    public static void onRenderThirstEffect(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_WEATHER || Minecraft.getInstance().gameMode == null || !Minecraft.getInstance().gameMode.hasExperience() || Minecraft.getInstance().player == null) return;

        if (Config.Baked.thirstEnabled) {
            RenderThirstOverlay.render();
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRenderGameOverlayColdHungerEffect(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay() != VanillaGuiOverlay.FOOD_LEVEL.type() || Minecraft.getInstance().gameMode == null || !Minecraft.getInstance().gameMode.hasExperience())
            return;

        if (event.isCanceled() || !Config.Baked.temperatureEnabled)
            return;

        int scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int scaledHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        if (!Minecraft.getInstance().options.hideGui) {
            assert Minecraft.getInstance().player != null;
            RenderTemperatureGui.renderFoodBarEffect(event.getGuiGraphics(), Minecraft.getInstance().player, scaledWidth, scaledHeight);
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
            if (!Minecraft.getInstance().isPaused()) {
                if (Config.Baked.temperatureEnabled) {
                    RenderTemperatureGui.updateTimer();
                    RenderTemperatureOverlay.updateTemperatureEffect(Minecraft.getInstance().player);
                    TemperatureEffectSound.tickPlay(Minecraft.getInstance().player);
                }
                if (Config.Baked.thirstEnabled) {
                    RenderThirstGui.updateTimer();
                    RenderThirstOverlay.updateThirstEffect(Minecraft.getInstance().player);
                }
                if (LegendarySurvivalOverhaul.sereneSeasonsLoaded && Config.Baked.seasonCardsEnabled) {
                    RenderSeasonCards.updateSeasonCardFading(Minecraft.getInstance().player);
                }
                if (Config.Baked.localizedBodyDamageEnabled) {
                    RenderBodyDamageGui.updateTimer();
                }
            }
        }
    }

    @SubscribeEvent
    public static void onTooltipRegistration(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(HydrationTooltip.class, component -> new HydrationTooltipClient(component.hydration, component.saturation, component.dirty));
    }
}
