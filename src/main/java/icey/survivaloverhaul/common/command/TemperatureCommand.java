package icey.survivaloverhaul.common.command;

import java.util.UUID;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.api.temperature.TemperatureUtil;
import icey.survivaloverhaul.common.capability.temperature.TemperatureCapability;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class TemperatureCommand extends CommandBase
{
	
	public TemperatureCommand()
	{
		super(Commands.literal("temperature").executes(src -> new TemperatureCommand().execute(src.getSource())));
	}

	@Override
	public int execute(CommandSource source) throws CommandSyntaxException
	{
		try
		{
			if (source.getEntity() instanceof PlayerEntity)
			{
				float targetTemperature = TemperatureUtil.getPlayerTargetTemperature(source.asPlayer());
				TemperatureCapability cap = TemperatureCapability.getTempCapability(source.asPlayer());
				int playerTemp = cap.getTemperatureLevel();
			
				String reply = "Temp: "+  playerTemp +"\nTarget Temp: " + targetTemperature;
			
				source.asPlayer().sendMessage(new StringTextComponent((reply)), UUID.randomUUID());
			}
		}
		catch(Exception e) 
		{
			Main.LOGGER.error(e.getMessage());
		}
		return Command.SINGLE_SUCCESS;
	}
}
