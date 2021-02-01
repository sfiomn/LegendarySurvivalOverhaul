package icey.survivaloverhaul.registry;

import com.mojang.brigadier.CommandDispatcher;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.common.command.CommandBase;
import icey.survivaloverhaul.common.command.TemperatureCommand;
import net.minecraft.command.CommandSource;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MOD_ID)
public class CommandRegister 
{
	public static final class ModCommands
	{
		public static final CommandBase TEMPERATURE = new TemperatureCommand();
	}
	
	@SubscribeEvent
	public static void onCommandRegister(RegisterCommandsEvent event)
	{
		CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();

		dispatcher.register(ModCommands.TEMPERATURE.getBuilder());
	}
}
