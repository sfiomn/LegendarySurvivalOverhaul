package sfiomn.legendarysurvivaloverhaul.registry;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.commands.CommandSourceStack;
import sfiomn.legendarysurvivaloverhaul.common.commands.BodyDamageCommand;
import sfiomn.legendarysurvivaloverhaul.common.commands.CommandBase;
import sfiomn.legendarysurvivaloverhaul.common.commands.TemperatureCommand;
import net.minecraftforge.event.RegisterCommandsEvent;

public class CommandRegistry {

	public static final CommandBase TEMPERATURE = new TemperatureCommand();
	public static final CommandBase BODY_DAMAGE = new BodyDamageCommand();

	public static void registerCommandsEvent(RegisterCommandsEvent event)
	{
		CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

		dispatcher.register(TEMPERATURE.getBuilder());
		dispatcher.register(BODY_DAMAGE.getBuilder());
	}
}
