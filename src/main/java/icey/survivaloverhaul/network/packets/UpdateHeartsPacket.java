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
import icey.survivaloverhaul.common.capability.heartmods.HeartModifierCapability;

public class UpdateHeartsPacket
{
	private CompoundNBT compound;
	
	public UpdateHeartsPacket(INBT compound)
	{
		this.compound = (CompoundNBT) compound;
	}
	
	public UpdateHeartsPacket() {}
	
	public static UpdateHeartsPacket decode(PacketBuffer buffer)
	{
		return new UpdateHeartsPacket(buffer.readCompoundTag());
	}
	
	public static void encode(UpdateHeartsPacket message, PacketBuffer buffer)
	{
		buffer.writeCompoundTag(message.compound);
	}
	
	public static void handle(UpdateHeartsPacket message, Supplier<NetworkEvent.Context> supplier)
	{
		final NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> syncHearts(message.compound)));
		
		supplier.get().setPacketHandled(true);
	}
	
	public static DistExecutor.SafeRunnable syncHearts(CompoundNBT compound)
	{
		return new DistExecutor.SafeRunnable()
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void run()
			{
				ClientPlayerEntity player = Minecraft.getInstance().player;
				
				HeartModifierCapability hearts = player.getCapability(Main.HEART_MOD_CAP).orElse(new HeartModifierCapability());
				
				hearts.load(compound);
			}
		};
	}
}
