package icey.survivaloverhaul.network;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.common.capability.AttachCapabilities;
import icey.survivaloverhaul.common.capability.temperature.Temperature;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.DistExecutor;

public class ClientSidedMethods
{
	public static DistExecutor.SafeRunnable syncTemperature(CompoundNBT compound)
	{
		return new DistExecutor.SafeRunnable()
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void run()
			{
				ClientPlayerEntity player = Minecraft.getInstance().player;
				
				Temperature temperature = player.getCapability(Main.TEMPERATURE_CAP).orElse(new Temperature());
				
				temperature.load(compound);
			}
		};
	}
}
