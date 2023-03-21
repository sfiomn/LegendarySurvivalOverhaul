package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.potion.Effects;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.common.blocks.*;

public class BlockRegistry
{
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, LegendarySurvivalOverhaul.MOD_ID);
	public static final RegistryObject<Block> HEATER = BLOCKS.register("heater", () -> new HeaterBaseBlock(ThermalTypeEnum.HEATING));
	public static final RegistryObject<Block> HEATER_TOP = BLOCKS.register("heater_top", HeaterTopBlock::new);
	public static final RegistryObject<Block> COOLER = BLOCKS.register("cooler", () -> new CoolerBlock(ThermalTypeEnum.COOLING));
	public static final RegistryObject<Block> SEWING_TABLE = BLOCKS.register("sewing_table", SewingTableBlock::new);
	public static final RegistryObject<Block> SUN_FERN = BLOCKS.register("sun_fern", () -> new SunFernBlock(Effects.WEAKNESS, 1, AbstractBlock.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS)));
	public static final RegistryObject<Block> ICE_FERN = BLOCKS.register("ice_fern", () -> new IceFernBlock(Effects.WEAKNESS, 1, AbstractBlock.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS)));

	public static void register(IEventBus eventBus){
		BLOCKS.register(eventBus);
	}
}
