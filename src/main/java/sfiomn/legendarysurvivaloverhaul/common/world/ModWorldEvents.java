package sfiomn.legendarysurvivaloverhaul.common.world;

import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.world.gen.ModPlantGeneration;

@Mod.EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID)
public class ModWorldEvents {

    @SubscribeEvent
    public static void biomeLoadingEvent(final BiomeLoadingEvent event) {
        ModPlantGeneration.generatePlants(event);
    }
}
