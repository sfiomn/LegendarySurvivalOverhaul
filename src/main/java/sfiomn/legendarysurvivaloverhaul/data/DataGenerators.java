package sfiomn.legendarysurvivaloverhaul.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.data.providers.*;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DataGenerators
{
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void gatherData(GatherDataEvent event)
	{
		DataGenerator gen = event.getGenerator();
		PackOutput packOutput = gen.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		gen.addProvider(event.includeClient(), new ModBlockStateProvider(packOutput, existingFileHelper));
		gen.addProvider(event.includeClient(), new ModItemModelProvider(packOutput, existingFileHelper));
		gen.addProvider(event.includeClient(), new ModParticleProvider(packOutput, existingFileHelper));

		gen.addProvider(event.includeServer(), new ModDatapackBuiltinEntriesProvider(packOutput, lookupProvider));
		gen.addProvider(event.includeServer(), new ModEntityTypesTagProvider(packOutput, lookupProvider, existingFileHelper));
		gen.addProvider(event.includeServer(), new ModDamageTypeTagsProvider(packOutput, lookupProvider, existingFileHelper));
		gen.addProvider(event.includeServer(), new ModRecipeProvider(packOutput));
		gen.addProvider(event.includeServer(), ModLootTableProvider.createLootTables(packOutput));
		ModBlockTagProvider blockTagProvider = gen.addProvider(event.includeServer(),
				new ModBlockTagProvider(packOutput, lookupProvider, existingFileHelper));
		gen.addProvider(event.includeServer(), new ModItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), existingFileHelper));
	}
}
