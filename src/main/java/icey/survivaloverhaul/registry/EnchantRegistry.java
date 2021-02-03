package icey.survivaloverhaul.registry;

import java.lang.reflect.Field;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.common.enchantments.GenericMagic.EnchantOptions;
import icey.survivaloverhaul.common.enchantments.InsulationMagic;
import icey.survivaloverhaul.common.enchantments.InsulationMagic.MagicType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnchantRegistry 
{
	public static final class ModEnchants
	{
		public static final InsulationMagic ADAPTIVE_BARRIER = new InsulationMagic("adaptive_barrier", MagicType.Both, Rarity.VERY_RARE, new EnchantOptions(4));
		public static final InsulationMagic THERMAL_BARRIER = new InsulationMagic("thermal_barrier", MagicType.Heat, Rarity.RARE, new EnchantOptions(3));
		public static final InsulationMagic COLD_BARRIER = new InsulationMagic("cold_barrier", MagicType.Cool, Rarity.RARE, new EnchantOptions(3));
	}
	
	@SubscribeEvent
	public static void registerEnchants(RegistryEvent.Register<Enchantment> event) {
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
		} 
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}
}
