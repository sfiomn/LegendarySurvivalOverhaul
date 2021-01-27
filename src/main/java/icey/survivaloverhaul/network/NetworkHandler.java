package icey.survivaloverhaul.network;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.network.packets.*;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkHandler
{
	private static final String PROTOCOL_VERSION = "1";
	
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(Main.MOD_ID, "main"), 
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals);
	
	public static void register()
	{
		int id = -1;
		
		INSTANCE.registerMessage(id++, UpdateTemperaturesPacket.class, UpdateTemperaturesPacket::encode, UpdateTemperaturesPacket::decode, UpdateTemperaturesPacket::handle);
		INSTANCE.registerMessage(id++, UpdateHeartsPacket.class, UpdateHeartsPacket::encode, UpdateHeartsPacket::decode, UpdateHeartsPacket::handle);
		INSTANCE.registerMessage(id++, UpdateStaminaPacket.class, UpdateStaminaPacket::encode, UpdateStaminaPacket::decode, UpdateStaminaPacket::handle);
	}
}