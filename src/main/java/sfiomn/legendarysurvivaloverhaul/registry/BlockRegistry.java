package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.common.blocks.*;
import sfiomn.legendarysurvivaloverhaul.itemgroup.ModItemGroup;

import java.util.function.Supplier;

public class BlockRegistry
{
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, LegendarySurvivalOverhaul.MOD_ID);
	public static final RegistryObject<Block> HEATER = registerBlock("heater", () -> new HeaterBaseBlock(ThermalTypeEnum.HEATING));
	public static final RegistryObject<Block> HEATER_TOP = BLOCKS.register("heater_top", HeaterTopBlock::new);
	public static final RegistryObject<Block> COOLER = registerBlock("cooler", () -> new CoolerBlock(ThermalTypeEnum.COOLING));
	public static final RegistryObject<Block> SEWING_TABLE = registerBlock("sewing_table", SewingTableBlock::new);
	public static final RegistryObject<Block> SUN_FERN_CROP = BLOCKS.register("sun_fern_crop", SunFernBlock::new);
	public static final RegistryObject<Block> ICE_FERN_CROP = BLOCKS.register("ice_fern_crop", () -> new IceFernBlock());
	public static final RegistryObject<Block> WATER_PLANT_CROP = BLOCKS.register("water_plant_crop", WaterPlantBlock::new);

	private static <T extends Block> RegistryObject<Block> registerBlock(String name, Supplier<T> block) {
		RegistryObject<Block> newBlock = BLOCKS.register(name, block);
		registerBlockItem(name, newBlock);
		return newBlock;
	}

	private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
		return ItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(ModItemGroup.LEGENDARY_SURVIVAL_OVERHAUL_GROUP)));
	}
	public static void register(IEventBus eventBus){
		BLOCKS.register(eventBus);
	}
}
