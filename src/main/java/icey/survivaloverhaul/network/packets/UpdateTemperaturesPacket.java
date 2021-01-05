package icey.survivaloverhaul.network.packets;

import net.minecraft.network.PacketBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.common.capability.temperature.Temperature;

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
		final NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> syncTemperature(message.compound)));
		
		supplier.get().setPacketHandled(true);
	}
	
	public static DistExecutor.SafeRunnable syncTemperature(CompoundNBT compound)
	{
		return new DistExecutor.SafeRunnable()
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void run()
			{
				ClientPlayerEntity player = Minecraft.getInstance().player;
				
				Temperature temperature = player.getCapability(Main.TEMPERATURE_CAP).orElse(new Temperature());
				
				temperature.load(compound);
			}
		};
	}
}
