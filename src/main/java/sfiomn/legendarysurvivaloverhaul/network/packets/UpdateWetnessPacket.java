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
import sfiomn.legendarysurvivaloverhaul.common.capability.wetness.WetnessCapability;

import java.util.function.Supplier;

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
		return new UpdateWetnessPacket(buffer.readNbt());
	}
	
	public static void encode(UpdateWetnessPacket message, PacketBuffer buffer)
	{
		buffer.writeNbt(message.compound);
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
				
				WetnessCapability wetness = player.getCapability(LegendarySurvivalOverhaul.WETNESS_CAP).orElse(new WetnessCapability());
				
				wetness.readNBT(compound);
			}
		};
	}
}
