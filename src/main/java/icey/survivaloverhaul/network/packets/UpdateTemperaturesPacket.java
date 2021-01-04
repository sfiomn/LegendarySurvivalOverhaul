package icey.survivaloverhaul.network.packets;

import net.minecraft.network.PacketBuffer;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

import icey.survivaloverhaul.network.ClientSidedMethods;

public class UpdateTemperaturesPacket
{
	private CompoundNBT compound;
	
	public UpdateTemperaturesPacket(INBT compound)
	{
		this.compound = (CompoundNBT) compound;
	}
	
	public UpdateTemperaturesPacket() {}
	
	public static UpdateTemperaturesPacket decode(PacketBuffer buffer)
	{
		return new UpdateTemperaturesPacket(buffer.readCompoundTag());
	}
	
	public static void encode(UpdateTemperaturesPacket message, PacketBuffer buffer)
	{
		buffer.writeCompoundTag(message.compound);
	}
	
	public static void handle(UpdateTemperaturesPacket message, Supplier<NetworkEvent.Context> supplier)
	{
		//TODO: get this fixed up once i get the proxies up and working
		final NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientSidedMethods.syncTemperature(message.compound)));
		
		supplier.get().setPacketHandled(true);
	}
}
