package icey.survivaloverhaul.common.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.math.MathHelper;

import icey.survivaloverhaul.Main;

public class ItemCanteen extends Item
{
	public ItemCanteen(boolean isNetherite, Item.Properties properties)
	{
		super(properties.maxStackSize(1).group(ItemGroup.TOOLS).maxDamage(99).setNoRepair());
		if (isNetherite)
		{
			this.setRegistryName(Main.MOD_ID, "netherite_canteen");
		}
		else
		{
			this.setRegistryName(Main.MOD_ID, "canteen");
		}
	}
	
	
}
