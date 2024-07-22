package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.bodydamage.BodyDamageCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.bodydamage.BodyDamageProvider;

import java.util.function.Supplier;

public class UpdateBodyDamagePacket
{
	private CompoundTag compound;

	public UpdateBodyDamagePacket(Tag compound)
	{
		this.compound = (CompoundTag) compound;
	}

	public UpdateBodyDamagePacket() {}
	
	public static UpdateBodyDamagePacket decode(FriendlyByteBuf buffer)
	{
		return new UpdateBodyDamagePacket(buffer.readNbt());
	}
	
	public static void encode(UpdateBodyDamagePacket message, FriendlyByteBuf buffer)
	{
		buffer.writeNbt(message.compound);
	}
	
	public static void handle(UpdateBodyDamagePacket message, Supplier<NetworkEvent.Context> supplier)
	{
		final NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> syncBodyDamage(message.compound)));
		
		supplier.get().setPacketHandled(true);
	}
	
	public static DistExecutor.SafeRunnable syncBodyDamage(CompoundTag compound)
	{
		return new DistExecutor.SafeRunnable()
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void run()
			{
				LocalPlayer player = Minecraft.getInstance().player;

				if (player != null) {
					BodyDamageCapability bodyDamageCapability = player.getCapability(BodyDamageProvider.BODY_DAMAGE_CAPABILITY).orElse(new BodyDamageCapability());

					bodyDamageCapability.readNBT(compound);
				}
			}
		};
	}
}
