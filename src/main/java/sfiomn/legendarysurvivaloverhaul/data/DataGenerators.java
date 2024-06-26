package sfiomn.legendarysurvivaloverhaul.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.data.recipes.ModRecipeProvider;

@Mod.EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DataGenerators
{
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void gatherData(GatherDataEvent event)
	{
		DataGenerator gen = event.getGenerator();

		gen.addProvider(new ModRecipeProvider(gen));
	}

}
