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
import sfiomn.legendarysurvivaloverhaul.common.capabilities.bodydamage.BodyDamageCapability;

import java.util.function.Supplier;

public class UpdateBodyDamagePacket
{
	private CompoundNBT compound;

	public UpdateBodyDamagePacket(INBT compound)
	{
		this.compound = (CompoundNBT) compound;
	}

	public UpdateBodyDamagePacket() {}
	
	public static UpdateBodyDamagePacket decode(PacketBuffer buffer)
	{
		return new UpdateBodyDamagePacket(buffer.readNbt());
	}
	
	public static void encode(UpdateBodyDamagePacket message, PacketBuffer buffer)
	{
		buffer.writeNbt(message.compound);
	}
	
	public static void handle(UpdateBodyDamagePacket message, Supplier<NetworkEvent.Context> supplier)
	{
		final NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> syncBodyDamage(message.compound)));
		
		supplier.get().setPacketHandled(true);
	}
	
	public static DistExecutor.SafeRunnable syncBodyDamage(CompoundNBT compound)
	{
		return new DistExecutor.SafeRunnable()
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void run()
			{
				ClientPlayerEntity player = Minecraft.getInstance().player;

                BodyDamageCapability bodyDamageCapability = player.getCapability(LegendarySurvivalOverhaul.BODY_DAMAGE_CAP).orElse(new BodyDamageCapability());
				
				bodyDamageCapability.readNBT(compound);
			}
		};
	}
}
