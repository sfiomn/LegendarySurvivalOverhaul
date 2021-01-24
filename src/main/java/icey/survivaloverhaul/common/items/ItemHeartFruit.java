package icey.survivaloverhaul.common.items;

import icey.survivaloverhaul.Main;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.world.World;

public class ItemHeartFruit extends Item
{
	public static final Food FOOD_STATS = (new Food.Builder()).hunger(6).saturation(2.5f).setAlwaysEdible().build();
	
	public ItemHeartFruit()
	{
		super(new Item.Properties().group(ItemGroup.FOOD).rarity(Rarity.RARE).food(FOOD_STATS));
		this.setRegistryName(Main.MOD_ID, "heart_fruit");
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity entity)
	{
		
		return entity.onFoodEaten(world, stack);
	}
	
}
