package sfiomn.legendarysurvivaloverhaul.data;

import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

@Mod.EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DataGenerators
{
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void gatherData(GatherDataEvent event)
	{

	}

}
