package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.heartmods.HeartModifierCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.heartmods.HeartModifierProvider;

import java.util.function.Supplier;

public class UpdateHeartsPacket
{
	private CompoundTag compound;
	
	public UpdateHeartsPacket(Tag compound)
	{
		this.compound = (CompoundTag) compound;
	}
	
	public UpdateHeartsPacket() {}
	
	public static UpdateHeartsPacket decode(FriendlyByteBuf buffer)
	{
		return new UpdateHeartsPacket(buffer.readNbt());
	}
	
	public static void encode(UpdateHeartsPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeNbt(message.compound);
	}
	
	public static void handle(UpdateHeartsPacket message, Supplier<NetworkEvent.Context> supplier)
	{
		final NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> syncHearts(message.compound)));
		
		supplier.get().setPacketHandled(true);
	}
	
	public static DistExecutor.SafeRunnable syncHearts(CompoundTag compound)
	{
		return new DistExecutor.SafeRunnable()
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void run()
			{
				LocalPlayer player = Minecraft.getInstance().player;

				if (player != null) {
					HeartModifierCapability hearts = player.getCapability(HeartModifierProvider.HEART_MODIFIER_CAPABILITY).orElse(new HeartModifierCapability());

					hearts.readNBT(compound);
					hearts.updateMaxHealth(player.getCommandSenderWorld(), player);
				}
			}
		};
	}
}
