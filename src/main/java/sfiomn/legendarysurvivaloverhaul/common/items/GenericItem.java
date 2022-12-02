package sfiomn.legendarysurvivaloverhaul.common.items;

import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.math.MathHelper;

@Deprecated
public class GenericItem extends Item
{
	public GenericItem(String name)
	{
		super(new Item.Properties().tab(ItemGroup.TAB_MISC));
		this.setRegistryName(LegendarySurvivalOverhaul.MOD_ID, name);
	}
	
	public GenericItem(String name, int stacksize)
	{
		super(new Item.Properties().tab(ItemGroup.TAB_MISC).stacksTo(MathHelper.clamp(stacksize, 1, 64)));
		this.setRegistryName(LegendarySurvivalOverhaul.MOD_ID, name);
	}
	
	public GenericItem(String name, ItemGroup group)
	{
		super(new Item.Properties().tab(group));
		
		this.setRegistryName(LegendarySurvivalOverhaul.MOD_ID, name);
	}
	
	public GenericItem(String name, ItemGroup group, int stacksize)
	{
		super(new Item.Properties().tab(group).stacksTo(MathHelper.clamp(stacksize, 1, 64)));
		
		this.setRegistryName(LegendarySurvivalOverhaul.MOD_ID, name);
	}
	
	public GenericItem(String name, boolean hidden, int stacksize)
	{
		super(new Item.Properties().stacksTo(MathHelper.clamp(stacksize, 1, 64)));
		this.setRegistryName(LegendarySurvivalOverhaul.MOD_ID, name);
	}

}