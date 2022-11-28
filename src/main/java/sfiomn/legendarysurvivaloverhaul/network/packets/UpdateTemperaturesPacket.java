package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.capability.temperature.TemperatureCapability;

import java.util.function.Supplier;

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
		return new UpdateTemperaturesPacket(buffer.readNbt());
	}
	
	public static void encode(UpdateTemperaturesPacket message, PacketBuffer buffer)
	{
		buffer.writeNbt(message.compound);
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
				
				TemperatureCapability temperature = player.getCapability(LegendarySurvivalOverhaul.TEMPERATURE_CAP).orElse(new TemperatureCapability());
				
				temperature.readNBT(compound);
			}
		};
	}
}
