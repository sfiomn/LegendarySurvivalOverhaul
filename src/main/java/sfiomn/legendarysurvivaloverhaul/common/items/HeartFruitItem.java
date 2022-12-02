package sfiomn.legendarysurvivaloverhaul.common.items;

import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.capability.heartmods.HeartModifierCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;
import sfiomn.legendarysurvivaloverhaul.network.packets.UpdateHeartsPacket;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;
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

public class HeartFruitItem extends Item
{
	public static final Food FOOD_STATS = (new Food.Builder()).nutrition(6).saturationMod(2.5f).alwaysEat().build();
	
	public HeartFruitItem()
	{
		super(new Item.Properties().tab(ItemGroup.TAB_FOOD).rarity(Rarity.RARE).food(FOOD_STATS));
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entity)
	{
		if (entity instanceof PlayerEntity && Config.Baked.heartFruitsEnabled)
		{
			PlayerEntity player = (PlayerEntity) entity;
			HeartModifierCapability cap = CapabilityUtil.getHeartModCapability(player);
			
			if (!world.isClientSide)
			{
				cap.addMaxHealth(Config.Baked.additionalHeartsPerFruit);
				cap.updateMaxHealth(world, player);
				
				if (Config.Baked.heartFruitsGiveRegen)
				{
					player.addEffect(new EffectInstance(Effects.REGENERATION, 200, 1));
				}
			}
			
			if (player instanceof ServerPlayerEntity)
			{			
				UpdateHeartsPacket packet = new UpdateHeartsPacket(LegendarySurvivalOverhaul.HEART_MOD_CAP.getStorage().writeNBT(LegendarySurvivalOverhaul.HEART_MOD_CAP, cap, null));
				
				NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)player), packet);
			}
		}
		
		return entity.eat(world, stack);
	}
	
}
