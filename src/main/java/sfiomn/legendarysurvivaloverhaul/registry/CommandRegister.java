package sfiomn.legendarysurvivaloverhaul.registry;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.commands.CommandSourceStack;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.commands.BodyDamageCommand;
import sfiomn.legendarysurvivaloverhaul.common.commands.CommandBase;
import sfiomn.legendarysurvivaloverhaul.common.commands.TemperatureCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommandRegister 
{
	public static final CommandBase TEMPERATURE = new TemperatureCommand();
	public static final CommandBase BODY_DAMAGE = new BodyDamageCommand();
	
	@SubscribeEvent
	public static void onCommandRegister(RegisterCommandsEvent event)
	{
		CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

		dispatcher.register(TEMPERATURE.getBuilder());
		dispatcher.register(BODY_DAMAGE.getBuilder());
	}
}
