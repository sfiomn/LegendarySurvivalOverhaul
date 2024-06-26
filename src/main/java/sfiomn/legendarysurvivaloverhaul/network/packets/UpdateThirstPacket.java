package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstProvider;

import java.util.function.Supplier;

public class UpdateThirstPacket
{
	private CompoundTag compound;

	public UpdateThirstPacket(Tag compound)
	{
		this.compound = (CompoundTag) compound;
	}

	public UpdateThirstPacket() {}
	
	public static UpdateThirstPacket decode(FriendlyByteBuf buffer)
	{
		return new UpdateThirstPacket(buffer.readNbt());
	}
	
	public static void encode(UpdateThirstPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeNbt(message.compound);
	}
	
	public static void handle(UpdateThirstPacket message, Supplier<NetworkEvent.Context> supplier)
	{
		final NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> syncThirst(message.compound)));
		
		supplier.get().setPacketHandled(true);
	}
	
	public static DistExecutor.SafeRunnable syncThirst(CompoundTag compound)
	{
		return new DistExecutor.SafeRunnable()
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void run()
			{
				LocalPlayer player = Minecraft.getInstance().player;

				if (player != null) {
					ThirstCapability thirst = player.getCapability(ThirstProvider.THIRST_CAPABILITY).orElse(new ThirstCapability());

					thirst.readNBT(compound);
				}
			}
		};
	}
}
