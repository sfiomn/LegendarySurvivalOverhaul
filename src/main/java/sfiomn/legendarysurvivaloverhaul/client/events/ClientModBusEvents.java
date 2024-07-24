package sfiomn.legendarysurvivaloverhaul.client.events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.client.tooltips.HydrationClientTooltipComponent;
import sfiomn.legendarysurvivaloverhaul.client.tooltips.HydrationTooltipComponent;
import sfiomn.legendarysurvivaloverhaul.common.particles.FernBlossomParticle;
import sfiomn.legendarysurvivaloverhaul.registry.ParticleTypeRegistry;

import static sfiomn.legendarysurvivaloverhaul.client.integration.sereneseasons.RenderSeasonCards.SEASON_CARD_GUI;
import static sfiomn.legendarysurvivaloverhaul.client.render.RenderBodyDamageGui.BODY_DAMAGE_GUI;
import static sfiomn.legendarysurvivaloverhaul.client.render.RenderTooltipFrame.TOOLTIP_ITEM_FRAME;
import static sfiomn.legendarysurvivaloverhaul.client.render.RenderTemperatureGui.FOOD_BAR_COLD_EFFECT_GUI;
import static sfiomn.legendarysurvivaloverhaul.client.render.RenderTemperatureGui.TEMPERATURE_GUI;
import static sfiomn.legendarysurvivaloverhaul.client.render.RenderTemperatureOverlay.TEMPERATURE_OVERLAY;
import static sfiomn.legendarysurvivaloverhaul.client.render.RenderThirstGui.THIRST_GUI;

@Mod.EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModBusEvents {

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerBelow(VanillaGuiOverlay.FOOD_LEVEL.id(), "cold_hunger", FOOD_BAR_COLD_EFFECT_GUI);

        event.registerAbove(VanillaGuiOverlay.FOOD_LEVEL.id(), "thirst", THIRST_GUI);

        event.registerAbove(VanillaGuiOverlay.FOOD_LEVEL.id(), "temperature", TEMPERATURE_GUI);

        event.registerAbove(VanillaGuiOverlay.FOOD_LEVEL.id(), "body_damage", BODY_DAMAGE_GUI);

        event.registerAbove(VanillaGuiOverlay.FROSTBITE.id(), "temperature_overlay", TEMPERATURE_OVERLAY);

        event.registerAbove(VanillaGuiOverlay.ITEM_NAME.id(), "item_frame_tooltip", TOOLTIP_ITEM_FRAME);

        event.registerAbove(VanillaGuiOverlay.ITEM_NAME.id(), "season_card", SEASON_CARD_GUI);
    }

    @SubscribeEvent
    public static void onTooltipRegistration(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(HydrationTooltipComponent.class, component -> new HydrationClientTooltipComponent(component.hydration, component.saturation, component.effectChance, component.effect));
    }

    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleTypeRegistry.SUN_FERN_BLOSSOM.get(), FernBlossomParticle.Factory::new);
        event.registerSpriteSet(ParticleTypeRegistry.ICE_FERN_BLOSSOM.get(), FernBlossomParticle.Factory::new);
    }
}
