package icey.survivaloverhaul.common.items;

import icey.survivaloverhaul.Main;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class ItemHeartFruitSeeds extends Item
{

	public ItemHeartFruitSeeds()
	{
		super(new Properties().group(ItemGroup.MISC));
		this.setRegistryName(Main.MOD_ID, "heartfruit_seed");
	}

}
