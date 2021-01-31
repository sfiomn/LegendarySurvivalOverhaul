package icey.survivaloverhaul.registry;

import java.lang.reflect.Field;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.common.enchantments.InsulationMagic;
import icey.survivaloverhaul.common.enchantments.InsulationMagic.MagicType;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnchantRegistry 
{
	public static final class ModEnchants
	{
		// Doesn't work at the moment - icey | but it do - birb
		public static final InsulationMagic ADAPTIVE_BARRIER = new InsulationMagic("adaptive_barrier", MagicType.Both);
		public static final InsulationMagic THERMAL_BARRIER = new InsulationMagic("thermal_barrier", MagicType.Heat);
		public static final InsulationMagic COLD_BARRIER = new InsulationMagic("cold_barrier", MagicType.Cool);
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Enchantment> event) {
		try 
		{
			for (Field f : EnchantRegistry.ModEnchants.class.getDeclaredFields()) 
			{
				Object obj = f.get(null);
				if (obj instanceof Enchantment) 
				{
					event.getRegistry().register((Enchantment) obj);
				} 
				else if (obj instanceof Enchantment[]) 
				{
					for (Enchantment Magic : (Enchantment[]) obj) 
					{
						event.getRegistry().register(Magic);
					}
				}
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
