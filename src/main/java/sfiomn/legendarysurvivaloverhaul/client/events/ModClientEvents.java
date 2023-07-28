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
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sereneseasons.api.SSItems;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.client.gui.RenderTemperatureEffect;
import sfiomn.legendarysurvivaloverhaul.client.gui.RenderTemperatureGui;
import sfiomn.legendarysurvivaloverhaul.client.gui.RenderThirstGui;
import sfiomn.legendarysurvivaloverhaul.client.gui.TooltipFrame;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureItemCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.EffectRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;
import sfiomn.legendarysurvivaloverhaul.util.WorldUtil;

import static sfiomn.legendarysurvivaloverhaul.client.gui.RenderTemperatureEffect.updateTemperatureEffect;
import static sfiomn.legendarysurvivaloverhaul.client.gui.RenderTemperatureGui.updateTemperatureGui;
import static sfiomn.legendarysurvivaloverhaul.client.gui.RenderThirstGui.updateThirstGui;
import static sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsUtil.formatSeasonName;
import static sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsUtil.plantCanGrow;
import static sfiomn.legendarysurvivaloverhaul.util.WorldUtil.timeInGame;

@Mod.EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ModClientEvents {
    private static final Minecraft minecraft = Minecraft.getInstance();
    public static TooltipFrame renderer;
    public static Entity entityLookedAt;
    public static int clientUpdateTimer;

    @SubscribeEvent
    public static void onItemRightClick(PlayerInteractEvent.RightClickItem event){
        if(LegendarySurvivalOverhaul.sereneSeasonsLoaded) {
            PlayerEntity player = minecraft.player;
            if (player == null) {
                return;
            }
            if (event.getItemStack().getItem() == SSItems.calendar) {
                player.displayClientMessage(formatSeasonName(player.blockPosition(), player.level), true);
            } else if (event.getItemStack().getItem() == Items.CLOCK) {
                player.displayClientMessage(new StringTextComponent(timeInGame(Minecraft.getInstance())), true);
            } else if (event.getItemStack().getItem() == Items.COMPASS) {
                player.displayClientMessage(new StringTextComponent("XYZ: " + player.blockPosition().getX() +
                        " / " + player.blockPosition().getY() + " / " + player.blockPosition().getZ()), true);
            }
        }
    }

    @SubscribeEvent
    public static void onRenderGameOverlayTooltip(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;

        if (Minecraft.renderNames() && !minecraft.isPaused() && minecraft.level != null) {
            if(event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
                MatrixStack matrixStack = event.getMatrixStack();
                if (entityLookedAt instanceof ItemFrameEntity && !((ItemFrameEntity) entityLookedAt).getItem().isEmpty()) {
                    Item itemInFrame = ((ItemFrameEntity) entityLookedAt).getItem().getItem();
                    if (renderer == null || !renderer.hasSameEntity(entityLookedAt)) {
                        renderer = new TooltipFrame(minecraft, matrixStack, entityLookedAt);
                    }

                    // data that doesn't need to be updated while looking at the item frame
                    if (!renderer.hasText()) {
                        if (LegendarySurvivalOverhaul.sereneSeasonsLoaded) {
                            if (itemInFrame == SSItems.calendar.getItem()) {
                                renderer.setText(formatSeasonName(entityLookedAt.blockPosition(), entityLookedAt.level));
                            }
                        }
                        if (itemInFrame == ItemRegistry.THERMOMETER.get()) {
                            TemperatureItemCapability tempItemCap = CapabilityUtil.getTempItemCapability(((ItemFrameEntity) entityLookedAt).getItem());
                            renderer.setText(new StringTextComponent(tempItemCap.getWorldTemperatureLevel() + "\u00B0C"));
                        } else if (itemInFrame == Items.COMPASS) {
                            renderer.setText(new StringTextComponent("XYZ: " + entityLookedAt.blockPosition().getX() +
                                    " / " + entityLookedAt.blockPosition().getY() + " / " + entityLookedAt.blockPosition().getZ()));
                        }
                    }

                    if (itemInFrame == Items.CLOCK) {
                        renderer.setText(new StringTextComponent(timeInGame(minecraft)));
                    }

                    if (renderer.hasText())
                        renderer.drawFrame();
                } else {
                    renderer = null;
                }
            }
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
    public static void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL || minecraft.gameMode == null || !minecraft.gameMode.hasExperience()) return;

        int scaledWidth = minecraft.getWindow().getGuiScaledWidth();
        int scaledHeight = minecraft.getWindow().getGuiScaledHeight();

        if (Config.Baked.temperatureEnabled) {
            if (!minecraft.options.hideGui) {
                RenderTemperatureGui.render(event.getMatrixStack(), minecraft.player, scaledWidth, scaledHeight);
            }
            RenderTemperatureEffect.render(event.getMatrixStack(), minecraft.player, scaledWidth, scaledHeight);
        }

        if (Config.Baked.thirstEnabled) {
            if (!minecraft.options.hideGui) {
                RenderThirstGui.render(event.getMatrixStack(), minecraft.player, scaledWidth, scaledHeight);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRenderGameOverlaySecondaryTemperatureEffect(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.FOOD || minecraft.gameMode == null || !minecraft.gameMode.hasExperience())
            return;

        if (event.isCanceled() || !Config.Baked.temperatureEnabled)
            return;

        int scaledWidth = minecraft.getWindow().getGuiScaledWidth();
        int scaledHeight = minecraft.getWindow().getGuiScaledHeight();

        if (!minecraft.options.hideGui) {
            assert minecraft.player != null;
            RenderTemperatureGui.renderFoodBarEffect(event.getMatrixStack(), minecraft.player, scaledWidth, scaledHeight);
        }
    }

    @SubscribeEvent
    public static void preRenderFood(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.FOOD) {
            if (minecraft.player != null && minecraft.player.hasEffect(EffectRegistry.COLD_SECONDARY_EFFECT.get()))
                ForgeIngameGui.renderFood = false;
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (!minecraft.isPaused()) {
                updateTemperatureGui();
                updateTemperatureEffect();
                updateThirstGui();
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player.level.isClientSide) {
            if (!minecraft.isPaused()) {
                clientUpdateTimer++;
                if (clientUpdateTimer >= 5) {
                    entityLookedAt = WorldUtil.getEntityLookedAt(event.player, 6);
                    clientUpdateTimer = 0;
                }
            }
        }
    }
}
