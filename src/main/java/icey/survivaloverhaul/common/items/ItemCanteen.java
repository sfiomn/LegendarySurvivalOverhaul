package icey.survivaloverhaul.common.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.math.MathHelper;

import icey.survivaloverhaul.Main;

public class ItemCanteen extends Item
{

	public ItemCanteen()
	{
		super(new Item.Properties().maxStackSize(1).group(ItemGroup.TOOLS));
		this.setRegistryName(Main.MOD_ID, "canteen");
	}

}
