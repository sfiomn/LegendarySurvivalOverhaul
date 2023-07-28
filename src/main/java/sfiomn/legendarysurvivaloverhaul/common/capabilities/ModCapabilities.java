package sfiomn.legendarysurvivaloverhaul.common.capabilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.network.PacketDistributor;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.heartmods.HeartModifierCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.heartmods.HeartModifierProvider;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureProvider;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstProvider;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessMode;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;
import sfiomn.legendarysurvivaloverhaul.network.packets.UpdateHeartsPacket;
import sfiomn.legendarysurvivaloverhaul.network.packets.UpdateTemperaturesPacket;
import sfiomn.legendarysurvivaloverhaul.network.packets.UpdateThirstPacket;
import sfiomn.legendarysurvivaloverhaul.network.packets.UpdateWetnessPacket;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

@Mod.EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = EventBusSubscriber.Bus.FORGE)
public class ModCapabilities
{
	public static final ResourceLocation TEMPERATURE_RES = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "temperature");
	public static final ResourceLocation WETNESS_RES = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "wetness");
	public static final ResourceLocation THIRST_RES = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "thirst");
	public static final ResourceLocation HEART_MOD_RES = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "heart_modifier");
	
	@SubscribeEvent
	public static void attachCapabilityPlayer(AttachCapabilitiesEvent<Entity> event)
	{
		if (event.getObject() instanceof LivingEntity)
		{
			if (event.getObject() instanceof PlayerEntity)
			{
				event.addCapability(TEMPERATURE_RES, new TemperatureProvider());
				event.addCapability(WETNESS_RES, new WetnessCapability.Provider());
				event.addCapability(THIRST_RES, new ThirstProvider());
				event.addCapability(HEART_MOD_RES, new HeartModifierProvider());
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent event)
	{
		if (event.player.level.isClientSide)
		{
			// Client Side
			return;
		}
		else
		{
			// Server Side
			PlayerEntity player = event.player;
			World world = player.level;
			
			if (Config.Baked.temperatureEnabled && !shouldSkipTick(player))
			{
				TemperatureCapability tempCap = CapabilityUtil.getTempCapability(player);
				
				tempCap.tickUpdate(player, world, event.phase);
				
				if(event.phase == Phase.START && (tempCap.isDirty() || tempCap.getPacketTimer() % Config.Baked.routinePacketSync == 0))
				{
					tempCap.setClean();
					sendTemperatureUpdate(player);
				}
			}
			
			if (Config.Baked.wetnessMode == WetnessMode.DYNAMIC && !shouldSkipTick(player))
			{
				WetnessCapability wetCap = CapabilityUtil.getWetnessCapability(player);
				
				wetCap.tickUpdate(player, world, event.phase);
				
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

			if (Config.Baked.thirstEnabled && !shouldSkipTick(player))
			{
				ThirstCapability thirstCap = CapabilityUtil.getThirstCapability(player);

				thirstCap.tickUpdate(player, world, event.phase);

				if(event.phase == Phase.START && (thirstCap.isDirty() || thirstCap.getPacketTimer() % Config.Baked.routinePacketSync == 0))
				{
					thirstCap.setClean();
					sendThirstUpdate(player);
				}
			}
		}
	}

	@SubscribeEvent
	public static void deathHandler(PlayerEvent.Clone event)
	{
		PlayerEntity orig = event.getOriginal();
		PlayerEntity player = event.getPlayer();
		
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

			if (Config.Baked.wetnessMode == WetnessMode.DYNAMIC)
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
		}
	}

	private static void sendTemperatureUpdate(PlayerEntity player)
	{
		if (!player.level.isClientSide())
		{
			UpdateTemperaturesPacket packet = new UpdateTemperaturesPacket(LegendarySurvivalOverhaul.TEMPERATURE_CAP.getStorage().writeNBT(LegendarySurvivalOverhaul.TEMPERATURE_CAP, CapabilityUtil.getTempCapability(player), null));
			
			NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)player), packet);
		}
	}

	private static void sendWetnessUpdate(PlayerEntity player)
	{
		if (!player.level.isClientSide)
		{
			UpdateWetnessPacket packet = new UpdateWetnessPacket(LegendarySurvivalOverhaul.WETNESS_CAP.getStorage().writeNBT(LegendarySurvivalOverhaul.WETNESS_CAP, CapabilityUtil.getWetnessCapability(player), null));

			NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)player), packet);
		}
	}

	private static void sendThirstUpdate(PlayerEntity player)
	{
		if (!player.level.isClientSide)
		{
			UpdateThirstPacket packet = new UpdateThirstPacket(LegendarySurvivalOverhaul.THIRST_CAP.getStorage().writeNBT(LegendarySurvivalOverhaul.THIRST_CAP, CapabilityUtil.getThirstCapability(player), null));

			NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)player), packet);
		}
	}

	private static void sendHeartsUpdate(PlayerEntity player)
	{
		if (!player.level.isClientSide)
		{
			UpdateHeartsPacket packet = new UpdateHeartsPacket(LegendarySurvivalOverhaul.HEART_MOD_CAP.getStorage().writeNBT(LegendarySurvivalOverhaul.HEART_MOD_CAP, CapabilityUtil.getHeartModCapability(player), null));
			
			NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)player), packet);
		}
	}

	@SubscribeEvent
	public static void syncCapsOnDimensionChange(PlayerChangedDimensionEvent event)
	{
		PlayerEntity player = event.getPlayer();
		if (Config.Baked.temperatureEnabled)
			sendTemperatureUpdate(player);
		if (Config.Baked.wetnessMode == WetnessMode.DYNAMIC)
			sendWetnessUpdate(player);
		if (Config.Baked.thirstEnabled)
			sendThirstUpdate(player);
		if (Config.Baked.heartFruitsEnabled)
			sendHeartsUpdate(player);
	}

	@SubscribeEvent
	public static void syncCapsOnLogin(PlayerLoggedInEvent event)
	{
		PlayerEntity player = event.getPlayer();
		if (Config.Baked.temperatureEnabled)
			sendTemperatureUpdate(player);
		if (Config.Baked.wetnessMode == WetnessMode.DYNAMIC)
			sendWetnessUpdate(player);
		if (Config.Baked.thirstEnabled)
			sendThirstUpdate(player);
		if (Config.Baked.heartFruitsEnabled)
			sendHeartsUpdate(player);
	}
	
	protected static boolean shouldSkipTick(PlayerEntity player)
	{
		return player.isCreative() || player.isSpectator();
	}
}
