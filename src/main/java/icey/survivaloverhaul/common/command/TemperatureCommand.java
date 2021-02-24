package icey.survivaloverhaul.common.command;

import java.util.UUID;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.api.temperature.TemperatureUtil;
import icey.survivaloverhaul.common.capability.temperature.TemperatureCapability;
import icey.survivaloverhaul.util.CapabilityUtil;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class TemperatureCommand extends CommandBase
{
	//.executes(src -> new TemperatureCommand().execute(src.getSource())));
	public TemperatureCommand()
	{
		super(Commands.literal("temperature")
				.requires((p_198521_0_) -> {
					return p_198521_0_.hasPermissionLevel(2);
				})
				.then(Commands.literal("set").then(Commands.argument("Temperature", IntegerArgumentType.integer(0,30)).executes(src ->  new TemperatureCommand().set(src.getSource(), IntegerArgumentType.getInteger(src, "Temperature")))))
				.then(Commands.literal("get").executes(src -> new TemperatureCommand().execute(src.getSource())))
				);
	}

	@Override
	public int execute(CommandSource source) throws CommandSyntaxException
	{
		try
		{
			if (source.getEntity() instanceof PlayerEntity)
			{
				float targetTemperature = TemperatureUtil.getPlayerTargetTemperature(source.asPlayer());
				TemperatureCapability cap = CapabilityUtil.getTempCapability(source.asPlayer());
				int playerTemp = cap.getTemperatureLevel();
			
				String reply1 = "Temp: "+  playerTemp,
				reply2 = "Target Temp: " + targetTemperature;
				
				source.asPlayer().sendMessage(new StringTextComponent((reply1)), UUID.randomUUID());
				source.asPlayer().sendMessage(new StringTextComponent((reply2)), UUID.randomUUID());
			}
		}
		catch(Exception e) 
		{
			Main.LOGGER.error(e.getMessage());
		}
		return Command.SINGLE_SUCCESS;
	}
	private int set(CommandSource src, int temp) throws CommandSyntaxException  
	{
		CapabilityUtil.getTempCapability(src.asPlayer()).setTemperatureLevel(temp);
		return Command.SINGLE_SUCCESS;
	}
}
