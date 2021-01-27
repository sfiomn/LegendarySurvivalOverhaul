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
import icey.survivaloverhaul.common.capability.stamina.StaminaCapability;
import icey.survivaloverhaul.common.capability.temperature.TemperatureCapability;

public class UpdateStaminaPacket
{
	private CompoundNBT compound;
	
	public UpdateStaminaPacket(INBT compound)
	{
		this.compound = (CompoundNBT) compound;
	}
	
	public UpdateStaminaPacket() {}
	
	public static UpdateStaminaPacket decode(PacketBuffer buffer)
	{
		return new UpdateStaminaPacket(buffer.readCompoundTag());
	}
	
	public static void encode(UpdateStaminaPacket message, PacketBuffer buffer)
	{
		buffer.writeCompoundTag(message.compound);
	}
	
	public static void handle(UpdateStaminaPacket message, Supplier<NetworkEvent.Context> supplier)
	{
		final NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> syncStamina(message.compound)));
		
		supplier.get().setPacketHandled(true);
	}
	
	public static DistExecutor.SafeRunnable syncStamina(CompoundNBT compound)
	{
		return new DistExecutor.SafeRunnable()
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void run()
			{
				ClientPlayerEntity player = Minecraft.getInstance().player;
				
				StaminaCapability stamina = player.getCapability(Main.STAMINA_CAP).orElse(new StaminaCapability());
				
				stamina.load(compound);
			}
		};
	}
}
