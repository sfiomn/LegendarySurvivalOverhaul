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
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;

import java.util.function.Supplier;

public class UpdateThirstPacket
{
	private CompoundNBT compound;

	public UpdateThirstPacket(INBT compound)
	{
		this.compound = (CompoundNBT) compound;
	}

	public UpdateThirstPacket() {}
	
	public static UpdateThirstPacket decode(PacketBuffer buffer)
	{
		return new UpdateThirstPacket(buffer.readNbt());
	}
	
	public static void encode(UpdateThirstPacket message, PacketBuffer buffer)
	{
		buffer.writeNbt(message.compound);
	}
	
	public static void handle(UpdateThirstPacket message, Supplier<NetworkEvent.Context> supplier)
	{
		final NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> syncThirst(message.compound)));
		
		supplier.get().setPacketHandled(true);
	}
	
	public static DistExecutor.SafeRunnable syncThirst(CompoundNBT compound)
	{
		return new DistExecutor.SafeRunnable()
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void run()
			{
				ClientPlayerEntity player = Minecraft.getInstance().player;
				
				ThirstCapability thirst = player.getCapability(LegendarySurvivalOverhaul.THIRST_CAP).orElse(new ThirstCapability());
				
				thirst.readNBT(compound);
			}
		};
	}
}
