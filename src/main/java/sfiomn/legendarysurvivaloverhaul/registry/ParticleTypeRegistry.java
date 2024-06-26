package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

public class ParticleTypeRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, LegendarySurvivalOverhaul.MOD_ID);

    public static final RegistryObject<SimpleParticleType> SUN_FERN_BLOSSOM = PARTICLE_TYPES.register("sun_fern_blossom", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> ICE_FERN_BLOSSOM = PARTICLE_TYPES.register("ice_fern_blossom", () -> new SimpleParticleType(true));

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
