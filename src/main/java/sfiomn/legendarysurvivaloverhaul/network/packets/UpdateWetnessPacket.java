package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessProvider;

import java.util.function.Supplier;

public class UpdateWetnessPacket
{
	private CompoundTag compound;
	
	public UpdateWetnessPacket(Tag compound)
	{
		this.compound = (CompoundTag) compound;
	}
	
	public UpdateWetnessPacket() {}
	
	public static UpdateWetnessPacket decode(FriendlyByteBuf buffer)
	{
		return new UpdateWetnessPacket(buffer.readNbt());
	}
	
	public static void encode(UpdateWetnessPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeNbt(message.compound);
	}
	
	public static void handle(UpdateWetnessPacket message, Supplier<NetworkEvent.Context> supplier)
	{
		final NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> syncWetness(message.compound)));
		
		supplier.get().setPacketHandled(true);
	}
	
	public static DistExecutor.SafeRunnable syncWetness(CompoundTag compound)
	{
		return new DistExecutor.SafeRunnable()
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void run()
			{
				LocalPlayer player = Minecraft.getInstance().player;

				if (player != null) {
					WetnessCapability wetness = player.getCapability(WetnessProvider.WETNESS_CAPABILITY).orElse(new WetnessCapability());

					wetness.readNBT(compound);
				}
			}
		};
	}
}
