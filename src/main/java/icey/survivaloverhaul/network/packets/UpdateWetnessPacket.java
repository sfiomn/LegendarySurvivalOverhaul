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
import icey.survivaloverhaul.common.capability.wetness.WetnessCapability;

public class UpdateWetnessPacket
{
	private CompoundNBT compound;
	
	public UpdateWetnessPacket(INBT compound)
	{
		this.compound = (CompoundNBT) compound;
	}
	
	public UpdateWetnessPacket() {}
	
	public static UpdateWetnessPacket decode(PacketBuffer buffer)
	{
		return new UpdateWetnessPacket(buffer.readCompoundTag());
	}
	
	public static void encode(UpdateWetnessPacket message, PacketBuffer buffer)
	{
		buffer.writeCompoundTag(message.compound);
	}
	
	public static void handle(UpdateWetnessPacket message, Supplier<NetworkEvent.Context> supplier)
	{
		final NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> syncWetness(message.compound)));
		
		supplier.get().setPacketHandled(true);
	}
	
	public static DistExecutor.SafeRunnable syncWetness(CompoundNBT compound)
	{
		return new DistExecutor.SafeRunnable()
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void run()
			{
				ClientPlayerEntity player = Minecraft.getInstance().player;
				
				WetnessCapability wetness = player.getCapability(Main.WETNESS_CAP).orElse(new WetnessCapability());
				
				wetness.readNBT(compound);
			}
		};
	}
}
