package icey.survivaloverhaul.data;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.data.client.ModBlockStateProvider;
import icey.survivaloverhaul.data.client.ModItemModelProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DataGenerators
{
	private DataGenerators() {}
	
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		DataGenerator gen = event.getGenerator();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		
		gen.addProvider(new ModItemModelProvider(gen, existingFileHelper));
		gen.addProvider(new ModBlockStateProvider(gen, existingFileHelper));
	}
}
