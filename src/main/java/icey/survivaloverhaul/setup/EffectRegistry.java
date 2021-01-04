package icey.survivaloverhaul.setup;

import java.lang.reflect.Field;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.common.effects.*;
import net.minecraft.potion.Effect;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EffectRegistry
{
	public static final Effect HYPOTHERMIA = new HypothermiaEffect();
	public static final Effect HYPERTHERMIA = new HyperthermiaEffect();
	public static final Effect COLD_RESISTANCE = new ColdResistanceEffect();
	public static final Effect HEAT_RESISTANCE = new HeatResistanceEffect();
	
	@SubscribeEvent
	public static void registerEffects(RegistryEvent.Register<Effect> event)
	{
		try {
			for (Field f : EffectRegistry.class.getDeclaredFields()) {
				Object obj = f.get(null);
				if (obj instanceof Effect) {
					event.getRegistry().register((Effect) obj);
				} else if (obj instanceof Effect[]) {
					for (Effect effect : (Effect[]) obj) {
						event.getRegistry().register(effect);
					}
				}
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
