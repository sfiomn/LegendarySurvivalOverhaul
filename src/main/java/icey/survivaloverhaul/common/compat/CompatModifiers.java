package icey.survivaloverhaul.common.compat;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.api.temperature.ModifierBase;
import icey.survivaloverhaul.common.compat.curios.CuriosModifier;
import icey.survivaloverhaul.common.compat.sereneseasons.SereneSeasonsModifier;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CompatModifiers
{
	public static final ModifierBase SERENE_SEASONS = new SereneSeasonsModifier();
	public static final ModifierBase CURIOS = new CuriosModifier();
	
	@SubscribeEvent
	public static void registerModifiers(RegistryEvent.Register<ModifierBase> event)
	{
		if (Main.sereneSeasonsLoaded)
			event.getRegistry().register(SERENE_SEASONS);
		if (Main.curiosLoaded)
			event.getRegistry().register(CURIOS);
	}
}
