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
				PlayerEntity player = (PlayerEntity) source.getEntity();
				float targetTemperature = TemperatureUtil.getPlayerTargetTemperature(player);
				TemperatureCapability cap = CapabilityUtil.getTempCapability(player);
				float playerTemp = cap.getTemperatureLevel();
				float worldTemp =  TemperatureUtil.getWorldTemperature(player.level, player.blockPosition());

				String reply = "Temp: " +  playerTemp + "\nTarget Temp: " + targetTemperature + "\nWorld Temp: " + worldTemp;

				source.getPlayerOrException().sendMessage(new StringTextComponent(reply), source.getEntity().getUUID());
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
