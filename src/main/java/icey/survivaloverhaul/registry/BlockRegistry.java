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
import net.minecraftforge.fml.common.Mod;
import net.minecraft.item.ItemGroup;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@SuppressWarnings("unused")
public class BlockRegistry
{
	// public static final Block EXAMPLE_BLOCK = new BlockGeneric(Material.ROCK, "example_block", "pickaxe", 0, 1.0f, 2.5f, SoundType.STONE);
	
	public static final class ModBlocks
	{
		public static final Block CINNABAR_ORE = new BlockGeneric(Material.ROCK, "cinnabar_ore", "pickaxe", 2, 3, 3, SoundType.STONE);
		public static final Block HEATING_COIL = new BlockTemperatureCoil(BlockTemperatureCoil.CoilType.HEATING);
		public static final Block COOLING_COIL = new BlockTemperatureCoil(BlockTemperatureCoil.CoilType.COOLING);
	}
	
	public static final class ModTileEntities
	{
		// public static final TileEntityType<?> TEMP_COIL = registerTileEntity(TileEntityType.Builder.create(CoilTileEntity::new, ModBlocks.HEATING_COIL, ModBlocks.COOLING_COIL), "temperature_coil");
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		// Code taken from https://github.com/Alex-the-666/Ice_and_Fire/tree/1.16.3
		try 
		{
			for (Field f : BlockRegistry.ModBlocks.class.getDeclaredFields()) 
			{
				Object obj = f.get(null);
				if (obj instanceof Block) 
				{
					event.getRegistry().register((Block) obj);
				} 
				else if (obj instanceof Block[]) 
				{
					for (Block block : (Block[]) obj) 
					{
						event.getRegistry().register(block);
					}
				}
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static TileEntityType registerTileEntity(TileEntityType.Builder builder, String entityName) {
		ResourceLocation nameLoc = new ResourceLocation(Main.MOD_ID, entityName);
		return (TileEntityType) builder.build(null).setRegistryName(nameLoc);
	}
	
	@SuppressWarnings("rawtypes")
	public static void registerTileEntities(final RegistryEvent.Register<TileEntityType<?>> event) {
		try 
		{
			for (Field f : ModTileEntities.class.getDeclaredFields())
			{
				Object obj = f.get(null);
				if (obj instanceof TileEntityType)
				{
					event.getRegistry().register((TileEntityType) obj);
				} 
				else if (obj instanceof TileEntityType[]) 
				{
					for (TileEntityType te : (TileEntityType[]) obj) 
					{
						event.getRegistry().register(te);
					}
				}
			}
		} 
		catch (IllegalAccessException e) 
		{
			throw new RuntimeException(e);
		}
	}
	
	@SubscribeEvent
	public static void registerBlockItems(RegistryEvent.Register<Item> event) 
	{
		// Code taken from https://github.com/Alex-the-666/Ice_and_Fire/tree/1.16.3
		// ItemBlocks
		try 
		{
			for (Field f : BlockRegistry.ModBlocks.class.getDeclaredFields())
			{
				Object obj = f.get(null);
				if (obj instanceof Block && !(obj instanceof WallTorchBlock) && !(obj instanceof FlowingFluidBlock))
				{
					Item.Properties props;
					
					if (obj instanceof BlockGeneric)
					{
						BlockGeneric genBlock = (BlockGeneric) obj;
						
						props = new Item.Properties().group(genBlock.group);
					}
					else if (obj instanceof BlockTemperatureCoil)
					{
						props = new Item.Properties().group(ItemGroup.REDSTONE);
					}
					else
					{
						props = new Item.Properties();
					}
					
					BlockItem itemBlock;
					itemBlock = new BlockItem((Block) obj, props);
					itemBlock.setRegistryName(((Block) obj).getRegistryName());
					event.getRegistry().register(itemBlock);
				} 
				else if (obj instanceof Block[]) 
				{
					for (Block block : (Block[]) obj) 
					{
						Item.Properties props;
						
						if (block instanceof BlockGeneric)
						{
							BlockGeneric genBlock = (BlockGeneric) block;
							
							props = new Item.Properties().group(genBlock.group);
						}
						else
						{
							props = new Item.Properties();
						}
						
						if (block.getRegistryName() != null) 
						{
							BlockItem itemBlock = new BlockItem(block, props);
							itemBlock.setRegistryName(block.getRegistryName());
							event.getRegistry().register(itemBlock);
						}
					}
				}
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
