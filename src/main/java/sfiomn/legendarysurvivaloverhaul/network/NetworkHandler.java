package sfiomn.legendarysurvivaloverhaul.network;

import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.network.packets.*;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkHandler
{
	private static final String PROTOCOL_VERSION = "1";
	
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "main"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals);
	
	public static void register()
	{
		int id = -1;
		
		INSTANCE.registerMessage(id++, UpdateTemperaturesPacket.class, UpdateTemperaturesPacket::encode, UpdateTemperaturesPacket::decode, UpdateTemperaturesPacket::handle);
		INSTANCE.registerMessage(id++, UpdateWetnessPacket.class, UpdateWetnessPacket::encode, UpdateWetnessPacket::decode, UpdateWetnessPacket::handle);
		INSTANCE.registerMessage(id++, UpdateThirstPacket.class, UpdateThirstPacket::encode, UpdateThirstPacket::decode, UpdateThirstPacket::handle);
		INSTANCE.registerMessage(id++, UpdateHeartsPacket.class, UpdateHeartsPacket::encode, UpdateHeartsPacket::decode, UpdateHeartsPacket::handle);
		INSTANCE.registerMessage(id++, MessageDrinkWater.class, MessageDrinkWater::encode, MessageDrinkWater::decode, MessageDrinkWater::handle);
	}
}