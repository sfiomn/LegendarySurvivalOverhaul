package icey.survivaloverhaul.common.capability;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.common.capability.temperature.TemperatureCapability;
import icey.survivaloverhaul.config.Config;
import icey.survivaloverhaul.network.packets.UpdateTemperaturesPacket;
import icey.survivaloverhaul.network.NetworkHandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CapabilityUpdateAndSync
{
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent event)
	{
		PlayerEntity player = event.player;
		World world = player.world;
		
		if (Config.BakedConfigValues.temperatureEnabled)
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
	public static void syncCapabilitiesOnLogin(PlayerLoggedInEvent event)
	{
		PlayerEntity player = event.getPlayer();
		sendTemperatureUpdate(player);
	}
	
	@SubscribeEvent
	public static void syncCapabilitiesOnDimensionChange(PlayerChangedDimensionEvent event)
	{
		PlayerEntity player = event.getPlayer();
		sendTemperatureUpdate(player);
	}
}
