package icey.survivaloverhaul.registry;

import java.lang.reflect.Field;
import java.util.function.Supplier;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.common.blocks.*;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.WallOrFloorItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.Item.Properties;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.item.ItemGroup;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@SuppressWarnings("unused")
public class BlockRegistry
{
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Main.MOD_ID);
	
	public static final RegistryObject<Block> CINNABAR_ORE = BLOCKS.register("cinnabar_ore", () -> new BlockGeneric(Material.ROCK, "pickaxe", 2, 3, 3, SoundType.STONE));
	
	public static final RegistryObject<Block> HEATING_COIL = BLOCKS.register("heating_coil", () -> new BlockTemperatureCoil(BlockTemperatureCoil.CoilType.HEATING));
	public static final RegistryObject<Block> COOLING_COIL = BLOCKS.register("cooling_coil", () -> new BlockTemperatureCoil(BlockTemperatureCoil.CoilType.COOLING));
}
