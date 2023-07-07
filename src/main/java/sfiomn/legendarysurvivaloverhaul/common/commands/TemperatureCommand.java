package sfiomn.legendarysurvivaloverhaul.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureCapability;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import java.util.UUID;

public class TemperatureCommand extends CommandBase
{
	//.executes(src -> new TemperatureCommand().execute(src.getSource())));
	public TemperatureCommand()
	{
		super(Commands.literal("temperature")
				.requires((p_198521_0_) -> p_198521_0_.hasPermission(2))
				.then(Commands.literal("set").then(Commands.argument("Temperature", IntegerArgumentType.integer(0,30)).executes(src ->  new TemperatureCommand().set(src.getSource(), IntegerArgumentType.getInteger(src, "Temperature")))))
				.then(Commands.literal("get").executes(src -> new TemperatureCommand().execute(src.getSource())))
				);
	}

	@Override
	public int execute(CommandSource source)
	{
		try
		{
			if (source.getEntity() instanceof PlayerEntity)
			{
				float targetTemperature = TemperatureUtil.getPlayerTargetTemperature(source.getPlayerOrException());
				TemperatureCapability cap = CapabilityUtil.getTempCapability(source.getPlayerOrException());
				float playerTemp = cap.getTemperatureLevel();
				float worldTemp =  TemperatureUtil.getWorldTemperature(source.getPlayerOrException().level, source.getPlayerOrException().blockPosition());

				String reply1 = "Temp: " +  playerTemp,
				reply2 = "Target Temp: " + targetTemperature,
				reply3 = "World Temp: " + worldTemp;

				LegendarySurvivalOverhaul.LOGGER.debug(reply1);
				LegendarySurvivalOverhaul.LOGGER.debug(reply2);
				LegendarySurvivalOverhaul.LOGGER.debug(reply3);

				source.getPlayerOrException().sendMessage(new StringTextComponent((reply1)), UUID.randomUUID());
				source.getPlayerOrException().sendMessage(new StringTextComponent((reply2)), UUID.randomUUID());
				source.getPlayerOrException().sendMessage(new StringTextComponent((reply3)), UUID.randomUUID());
			}
		}
		catch(Exception e) 
		{
			LegendarySurvivalOverhaul.LOGGER.error(e.getMessage());
		}
		return Command.SINGLE_SUCCESS;
	}
	private int set(CommandSource src, int temp) throws CommandSyntaxException  
	{
		CapabilityUtil.getTempCapability(src.getPlayerOrException()).setTemperatureLevel(temp);
		return Command.SINGLE_SUCCESS;
	}
}
