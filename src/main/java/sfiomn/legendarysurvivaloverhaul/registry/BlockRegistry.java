package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.common.blocks.SewingTableBlock;
import sfiomn.legendarysurvivaloverhaul.common.blocks.ThermalBlock;

public class BlockRegistry
{
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, LegendarySurvivalOverhaul.MOD_ID);

	public static final RegistryObject<Block> HEATER = BLOCKS.register("heater", () -> new ThermalBlock(ThermalTypeEnum.HEATING));
	public static final RegistryObject<Block> COOLER = BLOCKS.register("cooler", () -> new ThermalBlock(ThermalTypeEnum.COOLING));
	public static final RegistryObject<Block> SEWING_TABLE = BLOCKS.register("sewing_table", SewingTableBlock::new);

	public static void register(IEventBus eventBus){
		BLOCKS.register(eventBus);
	}
}
