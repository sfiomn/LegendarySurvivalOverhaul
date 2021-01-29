package icey.survivaloverhaul.common.world;

import java.util.Arrays;
import java.util.List;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.config.Config;
import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.TableLootEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MOD_ID)
public class LootTableInjectors
{
	public static final List<String> HEART_FRUIT_LOOT_TABLES = Arrays.asList(
			"chests/buried_treasure",
			"chests/jungle_temple",
			"chests/abandoned_mineshaft",
			"chests/bastion_treasure",
			"chests/stronghold_corridor"
			);
	
	@SubscribeEvent
	public static void onLootTableLoad(LootTableLoadEvent event)
	{
		String prefix = "minecraft:";
		String name = event.getName().toString();
		
		if (name.startsWith(prefix))
		{
			String location = name.substring(name.indexOf(prefix) + prefix.length());
			
			if (Config.Baked.heartFruitsEnabled && HEART_FRUIT_LOOT_TABLES.contains(location))
			{
				event.getTable().addPool(getInjectPool("heart_fruits"));
			}
		}
	}
	
	public static LootPool getInjectPool(String entryName)
	{
		return LootPool.builder()
				.addEntry(getInjectEntry(entryName))
				.name("survivaloverhaul_inject")
				.build();
	}
	
	private static LootEntry.Builder<?> getInjectEntry(String name)
	{
		ResourceLocation table = new ResourceLocation(Main.MOD_ID, "inject/" + name);
		return TableLootEntry.builder(table).weight(1);
	}
}
