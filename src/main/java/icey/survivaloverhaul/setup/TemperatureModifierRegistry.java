package icey.survivaloverhaul.setup;

import java.lang.reflect.Field;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.common.temperature.*;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TemperatureModifierRegistry
{
	public static final ModifierBase DEFAULT = new ModifierDefault();
	public static final ModifierBase BIOME = new ModifierBiome();
	
	@SubscribeEvent
	public static void registerEffects(RegistryEvent.Register<ModifierBase> event)
	{
		try {
			for (Field f : TemperatureModifierRegistry.class.getDeclaredFields()) {
				Object obj = f.get(null);
				if (obj instanceof ModifierBase) {
					event.getRegistry().register((ModifierBase) obj);
				} else if (obj instanceof ModifierBase[]) {
					for (ModifierBase modifier : (ModifierBase[]) obj) {
						event.getRegistry().register(modifier);
					}
				}
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
