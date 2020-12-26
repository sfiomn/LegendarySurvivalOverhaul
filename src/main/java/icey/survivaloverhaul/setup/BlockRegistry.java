package icey.survivaloverhaul.setup;

import java.lang.reflect.Field;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.common.blocks.BlockGeneric;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.WallOrFloorItem;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.item.ItemGroup;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@SuppressWarnings("unused")
public class BlockRegistry
{
	// public static final Block EXAMPLE_BLOCK = new BlockGeneric(Material.ROCK, "example_block", "pickaxe", 0, 1.0f, 2.5f, SoundType.STONE);
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		// Code taken from https://github.com/Alex-the-666/Ice_and_Fire/tree/1.16.3
		try {
			for (Field f : BlockRegistry.class.getDeclaredFields()) {
				Object obj = f.get(null);
				if (obj instanceof Block) {
					event.getRegistry().register((Block) obj);
				} else if (obj instanceof Block[]) {
					for (Block block : (Block[]) obj) {
						event.getRegistry().register(block);
					}
				}
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
	@SubscribeEvent
	public static void registerBlockItems(RegistryEvent.Register<Item> event) {
		// Code taken from https://github.com/Alex-the-666/Ice_and_Fire/tree/1.16.3
		// ItemBlocks
		try {
			for (Field f : BlockRegistry.class.getDeclaredFields()) {
				Object obj = f.get(null);
				if (obj instanceof Block && !(obj instanceof WallTorchBlock)) {
					Item.Properties props;
					
					if (obj instanceof BlockGeneric)
					{
						BlockGeneric genBlock = (BlockGeneric) obj; 
						
						if (!genBlock.associatedItem)
						{
							continue;
						}
						
						props = new Item.Properties().group(genBlock.group);
					}
					else
					{
						props = new Item.Properties();
					}
					
					BlockItem itemBlock;
					itemBlock = new BlockItem((Block) obj, props);
					itemBlock.setRegistryName(((Block) obj).getRegistryName());
					event.getRegistry().register(itemBlock);
				} else if (obj instanceof Block[]) {
					for (Block block : (Block[]) obj) {
						Item.Properties props;
						
						if (block instanceof BlockGeneric)
						{
							BlockGeneric genBlock = (BlockGeneric) block; 
							
							if (!genBlock.associatedItem)
							{
								continue;
							}
							
							props = new Item.Properties().group(genBlock.group);
						}
						else
						{
							props = new Item.Properties();
						}
						
						if (block.getRegistryName() != null) {
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
