package icey.survivaloverhaul.common.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.math.MathHelper;

import icey.survivaloverhaul.Main;

public class ItemCanteen extends Item
{
	public ItemCanteen(boolean isNetherite)
	{
		super(isNetherite ? 
				new Item.Properties().maxStackSize(1).group(ItemGroup.TOOLS).maxDamage(99).setNoRepair().isImmuneToFire() : 
				new Item.Properties().maxStackSize(1).group(ItemGroup.TOOLS).maxDamage(99).setNoRepair());

		this.setRegistryName(Main.MOD_ID, isNetherite ? "netherite_canteen" : "canteen");
	}
	
	
}
