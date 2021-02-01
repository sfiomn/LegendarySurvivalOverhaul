package icey.survivaloverhaul.common.command;

import java.util.UUID;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.common.capability.temperature.TemperatureCapability;
import icey.survivaloverhaul.registry.TemperatureModifierRegistry;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class TemperatureCommand extends CommandBase
{
	
	public TemperatureCommand()
	{
		this.builder = Commands.literal("temperature").executes(src -> new TemperatureCommand().execute(src.getSource()));
	}

	@Override
	public int execute(CommandSource source) throws CommandSyntaxException
	{
		try
		{
			float armorValue = TemperatureModifierRegistry.ModifierList.ARMOR.getPlayerInfluence(source.asPlayer());
			int playerTemp = TemperatureCapability.getTempCapability(source.asPlayer()).getTemperatureLevel();
		
			String reply = "Temp: "+  playerTemp +"\nArmor Influence: " + armorValue;
		
			source.asPlayer().sendMessage(new StringTextComponent((reply)), UUID.randomUUID());
		}
		catch(Exception e) 
		{
			Main.LOGGER.error(e.getMessage());
		}
		return Command.SINGLE_SUCCESS;
	}
}
