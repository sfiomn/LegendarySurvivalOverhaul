package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import net.minecraft.resources.ResourceLocation;

public class SoundRegistry
{
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
			DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, LegendarySurvivalOverhaul.MOD_ID);

	public static final RegistryObject<SoundEvent> HEAT_STROKE_EARLY = registerSoundEvent("heat_stroke_early");
	public static final RegistryObject<SoundEvent> HEAT_STROKE = registerSoundEvent("heat_stroke");
	public static final RegistryObject<SoundEvent> PANTING = registerSoundEvent("panting");
	public static final RegistryObject<SoundEvent> FROSTBITE_EARLY = registerSoundEvent("frostbite_early");
	public static final RegistryObject<SoundEvent> FROSTBITE = registerSoundEvent("frostbite");
	public static final RegistryObject<SoundEvent> SHIVERING = registerSoundEvent("shivering");
	public static final RegistryObject<SoundEvent> SEWING_TABLE = registerSoundEvent("sewing_table");
	public static final RegistryObject<SoundEvent> COOLER_BLOCK = registerSoundEvent("cooler_block");

	public static final RegistryObject<SoundEvent> HEADSHOT = registerSoundEvent("headshot");
	public static final RegistryObject<SoundEvent> HEAL_BODY_PART = registerSoundEvent("heal_body_part");
	public static final RegistryObject<SoundEvent> HARD_FALLING_HURT = registerSoundEvent("hard_falling_hurt");
	public static final RegistryObject<SoundEvent> HEADACHE_HEARTBEAT = registerSoundEvent("headache_heartbeat");

	private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
		return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(
				new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, name)
		));
	}
	public static void register(IEventBus eventBus) {
		SOUND_EVENTS.register(eventBus);
	}
}
