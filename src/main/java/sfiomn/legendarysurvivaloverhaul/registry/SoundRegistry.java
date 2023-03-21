package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class SoundRegistry
{
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
			DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, LegendarySurvivalOverhaul.MOD_ID);

	public static final RegistryObject<SoundEvent> HEAT_STROKE = registerSoundEvent("heat_stroke");
	public static final RegistryObject<SoundEvent> FROSTBITE = registerSoundEvent("frostbite");
	public static final RegistryObject<SoundEvent> SEWING_TABLE = registerSoundEvent("sewing_table");
	public static final RegistryObject<SoundEvent> COOLER_BLOCK = registerSoundEvent("cooler_block");

	private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
		return SOUND_EVENTS.register(name, () -> new SoundEvent(
				new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, name)
		));
	}
	public static void register(IEventBus eventBus) {
		SOUND_EVENTS.register(eventBus);
	}
}
