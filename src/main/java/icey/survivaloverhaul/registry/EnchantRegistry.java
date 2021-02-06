package icey.survivaloverhaul.registry;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.common.enchantments.GenericMagic.EnchantOptions;
import icey.survivaloverhaul.common.enchantments.InsulationMagic;
import icey.survivaloverhaul.common.enchantments.InsulationMagic.MagicType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnchantRegistry 
{
	public static final DeferredRegister<Enchantment> ENCHANTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Main.MOD_ID);

	public static final RegistryObject<Enchantment> ADAPTIVE_BARRIER = ENCHANTS.register("adaptive_barrier", () -> new InsulationMagic(MagicType.Both, Rarity.VERY_RARE, new EnchantOptions(4)));
	public static final RegistryObject<Enchantment> THERMAL_BARRIER = ENCHANTS.register("thermal_barrier", () -> new InsulationMagic(MagicType.Heat, Rarity.RARE, new EnchantOptions(3)));
	public static final RegistryObject<Enchantment> COLD_BARRIER = ENCHANTS.register("cold_barrier", () -> new InsulationMagic(MagicType.Cool, Rarity.RARE, new EnchantOptions(3)));
	
	
}
