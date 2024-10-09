package sfiomn.legendarysurvivaloverhaul.common.capabilities;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.network.PacketDistributor;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.bodydamage.BodyDamageCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.bodydamage.BodyDamageProvider;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.food.FoodCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.food.FoodProvider;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.heartmods.HeartModifierCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.heartmods.HeartModifierProvider;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureProvider;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstProvider;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessMode;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessProvider;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;
import sfiomn.legendarysurvivaloverhaul.network.packets.*;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

@Mod.EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = EventBusSubscriber.Bus.FORGE)
public class ModCapabilities
{
	public static final ResourceLocation TEMPERATURE_RES = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "temperature");
	public static final ResourceLocation WETNESS_RES = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "wetness");
	public static final ResourceLocation THIRST_RES = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "thirst");
	public static final ResourceLocation HEART_MOD_RES = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "heart_modifier");
	public static final ResourceLocation FOOD_RES = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "food");
	public static final ResourceLocation BODY_DAMAGE_RES = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "body_damage");
	
	@SubscribeEvent
	public static void attachCapabilityPlayer(AttachCapabilitiesEvent<Entity> event)
	{
		if (event.getObject() instanceof LivingEntity)
		{
			if (event.getObject() instanceof Player)
			{
				event.addCapability(TEMPERATURE_RES, new TemperatureProvider());
				event.addCapability(WETNESS_RES, new WetnessProvider());
				event.addCapability(THIRST_RES, new ThirstProvider());
				event.addCapability(HEART_MOD_RES, new HeartModifierProvider());
				event.addCapability(FOOD_RES, new FoodProvider());
				event.addCapability(BODY_DAMAGE_RES, new BodyDamageProvider());
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent event)
	{
		if (event.side.isClient())
		{
			// Client Side
			Player player = event.player;

			if (shouldSkipTick(player)) return;

			if (Config.Baked.temperatureEnabled) {
				TemperatureCapability tempCap = CapabilityUtil.getTempCapability(player);

				tempCap.tickClient(player, event.phase);
			}
		}
		else
		{
			// Server Side
			Player player = event.player;
			Level level = player.level();

			if (shouldSkipTick(player)) return;

			if (Config.Baked.temperatureEnabled) {
				TemperatureCapability tempCap = CapabilityUtil.getTempCapability(player);
				
				tempCap.tickUpdate(player, level, event.phase);
				
				if(event.phase == Phase.START && (tempCap.isDirty() || tempCap.getPacketTimer() % Config.Baked.routinePacketSync == 0))
				{
					tempCap.setClean();
					sendTemperatureUpdate(player);
				}
			}
			
			if (Config.Baked.wetnessEnabled) {
				WetnessCapability wetCap = CapabilityUtil.getWetnessCapability(player);
				
				wetCap.tickUpdate(player, level, event.phase);
				
				/**
				 * Because of the way wetness is ticked, if it's dirty, it's probably going to be dirty next tick,
				 * and if it's clean, it's probably going to be clean the next tick
				 * Thus, we don't want to clean up the wetness capability every single tick
				 * just because the player is standing out in the rain
				 * since it's not good for performance
				 */
				if (event.phase == Phase.START && wetCap.getPacketTimer() % Config.Baked.routinePacketSync == 0 && wetCap.isDirty())
				{
					wetCap.setClean();
					sendWetnessUpdate(player);
				}
			}

			if (Config.Baked.thirstEnabled) {
				ThirstCapability thirstCap = CapabilityUtil.getThirstCapability(player);

				thirstCap.tickUpdate(player, level, event.phase);

				if (event.phase == Phase.START && (thirstCap.isDirty() || thirstCap.getPacketTimer() % Config.Baked.routinePacketSync == 0))
				{
					thirstCap.setClean();
					sendThirstUpdate(player);
				}
			}

			if (Config.Baked.baseFoodExhaustion > 0) {
				FoodCapability foodCapability = CapabilityUtil.getFoodCapability(player);

				foodCapability.tickUpdate(player, level, event.phase);
			}

			if (Config.Baked.localizedBodyDamageEnabled) {
				BodyDamageCapability bodyDamageCapability = CapabilityUtil.getBodyDamageCapability(player);

				bodyDamageCapability.tickUpdate(player, level, event.phase);

				if(event.phase == Phase.START && (bodyDamageCapability.isDirty() || bodyDamageCapability.getPacketTimer() % Config.Baked.routinePacketSync == 0))
				{
					bodyDamageCapability.setClean();
					sendBodyDamageUpdate(player);
				}
			}
		}
	}

	@SubscribeEvent
	public static void deathHandler(PlayerEvent.Clone event)
	{
		Player orig = event.getOriginal();
		Player player = event.getEntity();

		if (event.isWasDeath())
		{
			if (Config.Baked.heartFruitsEnabled && Config.Baked.heartsLostOnDeath >= 0)
			{
				HeartModifierCapability oldCap = CapabilityUtil.getHeartModCapability(orig);
				HeartModifierCapability newCap = CapabilityUtil.getHeartModCapability(player);
				
				int oldHearts = oldCap.getAdditionalHearts();
				
				newCap.setMaxHealth(oldHearts - Config.Baked.heartsLostOnDeath);
				
				newCap.updateMaxHealth(player.getCommandSenderWorld(), player);
				sendHeartsUpdate(player);
			}

			if (Config.Baked.localizedBodyDamageEnabled) {

				sendBodyDamageUpdate(player);
			}
		}
		else
		{
			if (Config.Baked.temperatureEnabled)
			{
				TemperatureCapability oldCap = CapabilityUtil.getTempCapability(orig);
				TemperatureCapability newCap = CapabilityUtil.getTempCapability(player);
				newCap.readNBT(oldCap.writeNBT());
				sendTemperatureUpdate(player);
			}

			if (Config.Baked.wetnessEnabled)
			{
				WetnessCapability oldCap = CapabilityUtil.getWetnessCapability(orig);
				WetnessCapability newCap = CapabilityUtil.getWetnessCapability(player);
				newCap.readNBT(oldCap.writeNBT());
				sendWetnessUpdate(player);
			}

			if (Config.Baked.thirstEnabled)
			{
				ThirstCapability oldCap = CapabilityUtil.getThirstCapability(orig);
				ThirstCapability newCap = CapabilityUtil.getThirstCapability(player);
				newCap.readNBT(oldCap.writeNBT());
				sendThirstUpdate(player);
			}
			
			if (Config.Baked.heartFruitsEnabled)
			{
				HeartModifierCapability oldCap = CapabilityUtil.getHeartModCapability(orig);
				HeartModifierCapability newCap = CapabilityUtil.getHeartModCapability(player);
				newCap.readNBT(oldCap.writeNBT());
				newCap.updateMaxHealth(player.getCommandSenderWorld(), player);
				sendHeartsUpdate(player);
			}

			if (Config.Baked.localizedBodyDamageEnabled)
			{
				BodyDamageCapability oldCap = CapabilityUtil.getBodyDamageCapability(orig);
				BodyDamageCapability newCap = CapabilityUtil.getBodyDamageCapability(player);
				newCap.readNBT(oldCap.writeNBT());
				sendBodyDamageUpdate(player);
			}
		}
	}

	private static void sendTemperatureUpdate(Player player)
	{
		if (!player.level().isClientSide())
		{
			UpdateTemperaturesPacket packet = new UpdateTemperaturesPacket(CapabilityUtil.getTempCapability(player).writeNBT());

			NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), packet);
		}
	}

	private static void sendWetnessUpdate(Player player)
	{
		if (!player.level().isClientSide)
		{
			UpdateWetnessPacket packet = new UpdateWetnessPacket(CapabilityUtil.getWetnessCapability(player).writeNBT());

			NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), packet);
		}
	}

	private static void sendThirstUpdate(Player player)
	{
		if (!player.level().isClientSide)
		{
			UpdateThirstPacket packet = new UpdateThirstPacket(CapabilityUtil.getThirstCapability(player).writeNBT());

			NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), packet);
		}
	}

	private static void sendBodyDamageUpdate(Player player)
	{
		if (!player.level().isClientSide)
		{
			UpdateBodyDamagePacket packet = new UpdateBodyDamagePacket(CapabilityUtil.getBodyDamageCapability(player).writeNBT());

			NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), packet);
		}
	}

	private static void sendHeartsUpdate(Player player)
	{
		if (!player.level().isClientSide)
		{
			UpdateHeartsPacket packet = new UpdateHeartsPacket(CapabilityUtil.getHeartModCapability(player).writeNBT());
			
			NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), packet);
		}
	}

	@SubscribeEvent
	public static void syncCapsOnDimensionChange(PlayerChangedDimensionEvent event)
	{
		Player player = event.getEntity();
		if (Config.Baked.temperatureEnabled)
			sendTemperatureUpdate(player);
		if (Config.Baked.wetnessEnabled)
			sendWetnessUpdate(player);
		if (Config.Baked.thirstEnabled)
			sendThirstUpdate(player);
		if (Config.Baked.heartFruitsEnabled)
			sendHeartsUpdate(player);
		if (Config.Baked.localizedBodyDamageEnabled)
			sendBodyDamageUpdate(player);
	}

	@SubscribeEvent
	public static void syncCapsOnLogin(PlayerLoggedInEvent event)
	{
		Player player = event.getEntity();
		if (Config.Baked.temperatureEnabled)
			sendTemperatureUpdate(player);
		if (Config.Baked.wetnessEnabled)
			sendWetnessUpdate(player);
		if (Config.Baked.thirstEnabled)
			sendThirstUpdate(player);
		if (Config.Baked.heartFruitsEnabled)
			sendHeartsUpdate(player);
		if (Config.Baked.localizedBodyDamageEnabled)
			sendBodyDamageUpdate(player);
	}
	
	protected static boolean shouldSkipTick(Player player)
	{
		return player.isCreative() || player.isSpectator();
	}
}
