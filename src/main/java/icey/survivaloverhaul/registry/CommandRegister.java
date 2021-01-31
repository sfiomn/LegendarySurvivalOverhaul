package icey.survivaloverhaul.registry;

import java.lang.reflect.Field;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.common.command.TemperatureCMD;
import net.minecraft.command.CommandSource;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MOD_ID)
public class CommandRegister 
{
	public static final class ModCommands
	{
		public static final LiteralArgumentBuilder<CommandSource> cmd = new TemperatureCMD().builder;
	}
	
	@SubscribeEvent
	public static void onCommandRegister(RegisterCommandsEvent event)
	{
		CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
		dispatcher.register(ModCommands.cmd);
	}
}
