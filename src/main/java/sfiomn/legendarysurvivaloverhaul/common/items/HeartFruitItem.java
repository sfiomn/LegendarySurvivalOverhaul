package sfiomn.legendarysurvivaloverhaul.common.items;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.heartmods.HeartModifierCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;
import sfiomn.legendarysurvivaloverhaul.network.packets.UpdateHeartsPacket;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class HeartFruitItem extends Item
{
	public static final FoodProperties FOOD_STATS = (new FoodProperties.Builder()).nutrition(6).saturationMod(2.5f).alwaysEat().build();
	
	public HeartFruitItem(Item.Properties properties)
	{
		super(properties.food(FOOD_STATS));
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity)
	{
		if (entity instanceof Player && Config.Baked.heartFruitsEnabled)
		{
			Player player = (Player) entity;
			HeartModifierCapability cap = CapabilityUtil.getHeartModCapability(player);
			
			if (!level.isClientSide)
			{
				cap.addMaxHealth(Config.Baked.additionalHeartsPerFruit);
				cap.updateMaxHealth(level, player);
				
				if (Config.Baked.heartFruitsGiveRegen)
				{
					player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1));
				}
			}
			
			if (player instanceof ServerPlayer)
			{			
				UpdateHeartsPacket packet = new UpdateHeartsPacket(CapabilityUtil.getHeartModCapability(player).writeNBT());
				
				NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)player), packet);
			}
		}
		
		return entity.eat(level, stack);
	}
	
}
