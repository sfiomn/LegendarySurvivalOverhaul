package icey.survivaloverhaul.common.capability;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.common.capability.temperature.TemperatureCapability;
import icey.survivaloverhaul.common.capability.temperature.TemperatureProvider;
import icey.survivaloverhaul.config.Config;
import icey.survivaloverhaul.network.NetworkHandler;
import icey.survivaloverhaul.network.packets.UpdateTemperaturesPacket;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
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
	
	@SubscribeEvent
	public static void attachCapability(AttachCapabilitiesEvent<Entity> event)
	{
		if (event.getObject() instanceof PlayerEntity)
		{
			event.addCapability(TEMPERATURE_RES, new TemperatureProvider());
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent event)
	{
		PlayerEntity player = event.player;
		World world = player.world;
		
		if (Config.BakedConfigValues.temperatureEnabled && shouldSkipTick(player))
		{
			TemperatureCapability tempCap = TemperatureCapability.getTempCapability(player);
			tempCap.tickUpdate(player, world, event.phase);
			
			if(event.phase == Phase.START && (tempCap.isDirty() || tempCap.getPacketTimer() % Config.BakedConfigValues.routinePacketSync == 0))
			{
				tempCap.setClean();
				sendTemperatureUpdate(player);
			}
		}
	}

	private static void sendTemperatureUpdate(PlayerEntity player)
	{
		if (player instanceof ServerPlayerEntity)
		{			
			UpdateTemperaturesPacket packet = new UpdateTemperaturesPacket(Main.TEMPERATURE_CAP.getStorage().writeNBT(Main.TEMPERATURE_CAP, TemperatureCapability.getTempCapability(player), null));
			
			// Main.LOGGER.info("Syncing NBT Data: " + Temperature.getTempCapability(player).getTemporaryModifiers().keySet());
			
			NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)player), packet);
		}
	}

	@SubscribeEvent
	public static void syncCapabilitiesOnDimensionChange(PlayerChangedDimensionEvent event)
	{
		PlayerEntity player = event.getPlayer();
		if (Config.BakedConfigValues.temperatureEnabled)
				sendTemperatureUpdate(player);
	}

	@SubscribeEvent
	public static void syncCapabilitiesOnLogin(PlayerLoggedInEvent event)
	{
		PlayerEntity player = event.getPlayer();
		if (Config.BakedConfigValues.temperatureEnabled)
			sendTemperatureUpdate(player);
	}
	
	protected static boolean shouldSkipTick(PlayerEntity player)
	{
		return player.isCreative() || player.isSpectator();
	}
}
