package sfiomn.legendarysurvivaloverhaul.client.events;


import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.client.particles.BreathParticle;
import sfiomn.legendarysurvivaloverhaul.client.particles.FernBlossomParticle;
import sfiomn.legendarysurvivaloverhaul.registry.ParticleTypeRegistry;

@Mod.EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModBusEvents {

    @SubscribeEvent
    public static void registerParticleFactories(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(ParticleTypeRegistry.SUN_FERN_BLOSSOM.get(), FernBlossomParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(ParticleTypeRegistry.ICE_FERN_BLOSSOM.get(), FernBlossomParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(ParticleTypeRegistry.COLD_BREATH.get(), BreathParticle.Factory::new);
    }
}
