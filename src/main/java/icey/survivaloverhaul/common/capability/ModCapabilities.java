package icey.survivaloverhaul.common.capability;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.common.capability.heartmods.HeartModifierCapability;
import icey.survivaloverhaul.common.capability.heartmods.HeartModifierProvider;
import icey.survivaloverhaul.common.capability.temperature.TemperatureCapability;
import icey.survivaloverhaul.common.capability.temperature.TemperatureProvider;
import icey.survivaloverhaul.config.Config;
import icey.survivaloverhaul.network.NetworkHandler;
import icey.survivaloverhaul.network.packets.UpdateHeartsPacket;
import icey.survivaloverhaul.network.packets.UpdateTemperaturesPacket;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = EventBusSubscriber.Bus.FORGE)
public class ModCapabilities
{
	public static final ResourceLocation TEMPERATURE_RES = new ResourceLocation(Main.MOD_ID, "temperature");
	public static final ResourceLocation HEART_MOD_RES = new ResourceLocation(Main.MOD_ID, "heart_modifier");
	public static final ResourceLocation STAMINA_RES = new ResourceLocation(Main.MOD_ID, "stamina");
	
	@SubscribeEvent
	public static void attachCapability(AttachCapabilitiesEvent<Entity> event)
	{
		if (event.getObject() instanceof PlayerEntity)
		{
			event.addCapability(TEMPERATURE_RES, new TemperatureProvider());
			event.addCapability(HEART_MOD_RES, new HeartModifierProvider());
			// event.addCapability(STAMINA_RES, new StaminaProvider());
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent event)
	{
		PlayerEntity player = event.player;
		World world = player.world;
		
		if (world.isRemote)
		{
			// Client Side
			/*
			if (Config.Baked.staminaEnabled && !shouldSkipTick(player))
			{
				StaminaCapability staminaCap = StaminaCapability.getStaminaCapability(player);
				staminaCap.clientTickUpdate(player, world, event.phase);
			}
			*/
			return;
		}
		else
		{
			// Server Side
			
			if (Config.Baked.temperatureEnabled && !shouldSkipTick(player))
			{
				TemperatureCapability tempCap = TemperatureCapability.getTempCapability(player);
				tempCap.tickUpdate(player, world, event.phase);
				
				if(event.phase == Phase.START && (tempCap.isDirty() || tempCap.getPacketTimer() % Config.Baked.routinePacketSync == 0))
				{
					tempCap.setClean();
					sendTemperatureUpdate(player);
				}
			}
			/*
			if (Config.Baked.staminaEnabled && !shouldSkipTick(player))
			{
				StaminaCapability staminaCap = StaminaCapability.getStaminaCapability(player);
				staminaCap.tickUpdate(player, world, event.phase);
				
				if (event.phase == Phase.START && (staminaCap.getPacketTimer() % Config.Baked.routinePacketSync == 0))
				{
					sendStaminaUpdate(player);
				}
			}
			*/
		}
	}

	@SubscribeEvent
	public static void deathHandler(PlayerEvent.Clone event)
	{
		PlayerEntity orig = event.getOriginal();
		PlayerEntity player = event.getPlayer();
		
		if (event.isWasDeath())
		{
			if (Config.Baked.heartFruitsEnabled && Config.Baked.heartsLostOnDeath != -1)
			{
				HeartModifierCapability oldCap = HeartModifierCapability.getHeartModCapability(orig);
				HeartModifierCapability newCap = HeartModifierCapability.getHeartModCapability(player);
				
				int oldHearts = oldCap.getAdditionalHearts();
				
				newCap.setMaxHealth(oldHearts - Config.Baked.heartsLostOnDeath);
				
				newCap.updateMaxHealth(player.getEntityWorld(), player);
				sendHeartsUpdate(player);
			}
		}
		else
		{
			if (Config.Baked.temperatureEnabled)
			{
				TemperatureCapability oldCap = TemperatureCapability.getTempCapability(orig);
				TemperatureCapability newCap = TemperatureCapability.getTempCapability(player);
				newCap.load(oldCap.save());
				sendTemperatureUpdate(player);
			}
			/*
			if (Config.Baked.staminaEnabled)
			{
				StaminaCapability oldCap = StaminaCapability.getStaminaCapability(orig);
				StaminaCapability newCap = StaminaCapability.getStaminaCapability(player);
				newCap.load(oldCap.save());
				sendStaminaUpdate(player);
			}
			*/
			
			if (Config.Baked.heartFruitsEnabled)
			{
				HeartModifierCapability oldCap = HeartModifierCapability.getHeartModCapability(orig);
				HeartModifierCapability newCap = HeartModifierCapability.getHeartModCapability(player);
				newCap.load(oldCap.save());
				newCap.updateMaxHealth(player.getEntityWorld(), player);
				sendHeartsUpdate(player);
			}
		}
	}

	private static void sendTemperatureUpdate(PlayerEntity player)
	{
		if (!player.world.isRemote())
		{			
			UpdateTemperaturesPacket packet = new UpdateTemperaturesPacket(Main.TEMPERATURE_CAP.getStorage().writeNBT(Main.TEMPERATURE_CAP, TemperatureCapability.getTempCapability(player), null));
			
			NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)player), packet);
		}
	}

	private static void sendHeartsUpdate(PlayerEntity player)
	{
		if (!player.world.isRemote())
		{			
			UpdateHeartsPacket packet = new UpdateHeartsPacket(Main.HEART_MOD_CAP.getStorage().writeNBT(Main.HEART_MOD_CAP, HeartModifierCapability.getHeartModCapability(player), null));
			
			NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)player), packet);
		}
	}
	/*
	private static void sendStaminaUpdate(PlayerEntity player)
	{
		if (!player.world.isRemote())
		{			
			UpdateStaminaPacket packet = new UpdateStaminaPacket(Main.STAMINA_CAP.getStorage().writeNBT(Main.STAMINA_CAP, StaminaCapability.getStaminaCapability(player), null));
			
			NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)player), packet);
		}
	}
	*/

	@SubscribeEvent
	public static void syncCapsOnDimensionChange(PlayerChangedDimensionEvent event)
	{
		PlayerEntity player = event.getPlayer();
		if (Config.Baked.temperatureEnabled)
			sendTemperatureUpdate(player);
		if (Config.Baked.heartFruitsEnabled)
			sendHeartsUpdate(player);
		/*
		if (Config.Baked.staminaEnabled)
			sendStaminaUpdate(player);
		*/
	}

	@SubscribeEvent
	public static void syncCapsOnLogin(PlayerLoggedInEvent event)
	{
		PlayerEntity player = event.getPlayer();
		if (Config.Baked.temperatureEnabled)
			sendTemperatureUpdate(player);
		if (Config.Baked.heartFruitsEnabled)
			sendHeartsUpdate(player);
		/*
		if (Config.Baked.staminaEnabled)
			sendStaminaUpdate(player);
		*/
	}
	
	protected static boolean shouldSkipTick(PlayerEntity player)
	{
		return player.isCreative() || player.isSpectator();
	}
}
