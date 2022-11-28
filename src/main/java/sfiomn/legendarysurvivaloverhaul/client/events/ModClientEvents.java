package sfiomn.legendarysurvivaloverhaul.client.events;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sereneseasons.api.SSItems;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.client.gui.RenderTooltipFrame;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;
import sfiomn.legendarysurvivaloverhaul.util.WorldUtil;

import static sfiomn.legendarysurvivaloverhaul.client.gui.TemperatureGUI.renderGUI;
import static sfiomn.legendarysurvivaloverhaul.client.gui.TemperatureGUI.updateTemperatureGui;
import static sfiomn.legendarysurvivaloverhaul.common.compat.sereneseasons.SereneSeasonsUtil.formatSeasonName;
import static sfiomn.legendarysurvivaloverhaul.common.compat.sereneseasons.SereneSeasonsUtil.plantCanGrow;
import static sfiomn.legendarysurvivaloverhaul.util.WorldUtil.timeInGame;

@Mod.EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ModClientEvents {
    public static RenderTooltipFrame renderer;

    @SubscribeEvent
    public static void onItemRightClick(PlayerInteractEvent.RightClickItem event){
        if(LegendarySurvivalOverhaul.sereneSeasonsLoaded) {
            PlayerEntity player = Minecraft.getInstance().player;
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
    public static void onRenderGameOverlayPost(RenderGameOverlayEvent.Post event) {
        if (Minecraft.renderNames() && !Minecraft.getInstance().isPaused()) {
            if(event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
                Minecraft mc = Minecraft.getInstance();
                Entity entityLookedAt = WorldUtil.getEntityLookedAt(mc.player, 6);
                MatrixStack matrixStack = event.getMatrixStack();
                if (entityLookedAt instanceof ItemFrameEntity && !((ItemFrameEntity) entityLookedAt).getItem().isEmpty()) {
                    Item itemInFrame = ((ItemFrameEntity) entityLookedAt).getItem().getItem();
                    if (renderer == null || !renderer.hasSameEntity(entityLookedAt)) {
                        renderer = new RenderTooltipFrame(mc, matrixStack, entityLookedAt);
                    }

                    // data that doesn't need to be updated while looking at the item frame
                    if (!renderer.hasText()) {
                        if (LegendarySurvivalOverhaul.sereneSeasonsLoaded) {
                            if (itemInFrame == SSItems.calendar.getItem()) {
                                renderer.setText(formatSeasonName(entityLookedAt.blockPosition(), entityLookedAt.level));
                            }
                        }
                        if (itemInFrame == ItemRegistry.THERMOMETER.get()) {
                            renderer.setText(new StringTextComponent(WorldUtil.calculateClientWorldEntityTemperature(mc.level, entityLookedAt) + "\u00B0C"));
                        } else if (itemInFrame == Items.COMPASS) {
                            renderer.setText(new StringTextComponent("XYZ: " + entityLookedAt.blockPosition().getX() +
                                    " / " + entityLookedAt.blockPosition().getY() + " / " + entityLookedAt.blockPosition().getZ()));
                        }
                    }

                    if (itemInFrame == Items.CLOCK) {
                        renderer.setText(new StringTextComponent(timeInGame(mc)));
                    }

                    if (renderer.hasText())
                        renderer.drawFrame();
                } else {
                    renderer = null;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent event) {
        if ((event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE || event.getType() == RenderGameOverlayEvent.ElementType.JUMPBAR)
                && Config.Baked.temperatureEnabled
                && Minecraft.getInstance().gameMode != null && Minecraft.getInstance().gameMode.hasExperience())
        {
            int scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int scaledHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

            renderGUI(event.getMatrixStack(), Minecraft.getInstance().player, scaledWidth, scaledHeight);

            Minecraft.getInstance().textureManager.bind(AbstractGui.GUI_ICONS_LOCATION);
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (!Minecraft.getInstance().isPaused()) {
                updateTemperatureGui();
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
            event.getPlayer().displayClientMessage(new StringTextComponent("This crop can't grow during season"), true);
        }
    }
}
