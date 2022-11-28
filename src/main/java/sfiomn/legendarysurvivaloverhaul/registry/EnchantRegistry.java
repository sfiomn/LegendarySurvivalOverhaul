package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.enchantments.GenericMagic.EnchantOptions;
import sfiomn.legendarysurvivaloverhaul.common.enchantments.InsulationMagic;
import sfiomn.legendarysurvivaloverhaul.common.enchantments.InsulationMagic.MagicType;

public class EnchantRegistry 
{
	public static final DeferredRegister<Enchantment> ENCHANTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, LegendarySurvivalOverhaul.MOD_ID);

	public static final RegistryObject<Enchantment> ADAPTIVE_BARRIER = ENCHANTS.register("adaptive_barrier", () -> new InsulationMagic(MagicType.Both, Rarity.VERY_RARE, new EnchantOptions(4)));
	public static final RegistryObject<Enchantment> THERMAL_BARRIER = ENCHANTS.register("thermal_barrier", () -> new InsulationMagic(MagicType.Heat, Rarity.RARE, new EnchantOptions(3)));
	public static final RegistryObject<Enchantment> COLD_BARRIER = ENCHANTS.register("cold_barrier", () -> new InsulationMagic(MagicType.Cool, Rarity.RARE, new EnchantOptions(3)));
	
	public static void register(IEventBus eventBus){
		ENCHANTS.register(eventBus);
	}
}
