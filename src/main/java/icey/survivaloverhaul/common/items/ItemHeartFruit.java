package icey.survivaloverhaul.common.items;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.common.capability.heartmods.HeartModifierCapability;
import icey.survivaloverhaul.config.Config;
import icey.survivaloverhaul.network.NetworkHandler;
import icey.survivaloverhaul.network.packets.UpdateHeartsPacket;
import icey.survivaloverhaul.network.packets.UpdateTemperaturesPacket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

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
		if (entity instanceof PlayerEntity && Config.BakedConfigValues.heartFruitsEnabled)
		{
			PlayerEntity player = (PlayerEntity) entity;
			HeartModifierCapability cap = HeartModifierCapability.getHeartModCapability(player);
			
			if (!world.isRemote)
			{
				cap.addMaxHealth(Config.BakedConfigValues.additionalHeartsPerFruit);
				cap.updateMaxHealth(world, player);
				
				if (Config.BakedConfigValues.heartFruitsGiveRegen)
				{
					player.addPotionEffect(new EffectInstance(Effects.REGENERATION, 200, 2));
				}
			}
			
			if (player instanceof ServerPlayerEntity)
			{			
				UpdateHeartsPacket packet = new UpdateHeartsPacket(Main.HEART_MOD_CAP.getStorage().writeNBT(Main.HEART_MOD_CAP, cap, null));
				
				NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)player), packet);
			}
		}
		
		return entity.onFoodEaten(world, stack);
	}
	
}
