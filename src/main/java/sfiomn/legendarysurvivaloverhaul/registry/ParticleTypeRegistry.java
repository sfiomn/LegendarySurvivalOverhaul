package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

public class ParticleTypeRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, LegendarySurvivalOverhaul.MOD_ID);

    public static final RegistryObject<BasicParticleType> SUN_FERN_BLOSSOM = PARTICLE_TYPES.register("sun_fern_blossom", () -> new BasicParticleType(true));
    public static final RegistryObject<BasicParticleType> ICE_FERN_BLOSSOM = PARTICLE_TYPES.register("ice_fern_blossom", () -> new BasicParticleType(true));
    public static final RegistryObject<BasicParticleType> COLD_BREATH = PARTICLE_TYPES.register("cold_breath", () -> new BasicParticleType(true));

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
