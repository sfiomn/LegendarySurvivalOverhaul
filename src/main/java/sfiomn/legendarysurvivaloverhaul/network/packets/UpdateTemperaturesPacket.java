package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureProvider;

import java.util.function.Supplier;

public class UpdateTemperaturesPacket
{
	private CompoundTag compound;
	
	public UpdateTemperaturesPacket(Tag compound)
	{
		this.compound = (CompoundTag) compound;
	}
	
	public UpdateTemperaturesPacket() {}
	
	public static UpdateTemperaturesPacket decode(FriendlyByteBuf buffer)
	{
		return new UpdateTemperaturesPacket(buffer.readNbt());
	}
	
	public static void encode(UpdateTemperaturesPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeNbt(message.compound);
	}
	
	public static void handle(UpdateTemperaturesPacket message, Supplier<NetworkEvent.Context> supplier)
	{
		final NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> syncTemperature(message.compound)));
		
		supplier.get().setPacketHandled(true);
	}
	
	public static DistExecutor.SafeRunnable syncTemperature(CompoundTag compound)
	{
		return new DistExecutor.SafeRunnable()
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void run()
			{
				LocalPlayer player = Minecraft.getInstance().player;

				if (player != null) {
					TemperatureCapability temperature = player.getCapability(TemperatureProvider.TEMPERATURE_CAPABILITY).orElse(new TemperatureCapability());

					temperature.readNBT(compound);
				}
			}
		};
	}
}
