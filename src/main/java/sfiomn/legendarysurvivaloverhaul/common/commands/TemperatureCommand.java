package sfiomn.legendarysurvivaloverhaul.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureCapability;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;

public class TemperatureCommand extends CommandBase
{
	//.executes(src -> new TemperatureCommand().execute(src.getSource())));
	public TemperatureCommand()
	{
		super(Commands.literal("temperature")
				.requires((p_198521_0_) -> p_198521_0_.hasPermission(2))
				.then(Commands.literal("set").then(Commands.argument("Temperature", IntegerArgumentType.integer(TemperatureEnum.FROSTBITE.getLowerBound(),TemperatureEnum.HEAT_STROKE.getUpperBound())).executes(src ->  new TemperatureCommand().set(src.getSource(), IntegerArgumentType.getInteger(src, "Temperature")))))
				.then(Commands.literal("get").executes(src -> new TemperatureCommand().get(src.getSource())))
				);
	}

	@Override
	public int get(CommandSourceStack source)
	{
		try
		{
			if (source.getEntity() instanceof Player)
			{
				Player player = (Player) source.getEntity();
				float targetTemperature = TemperatureUtil.getPlayerTargetTemperature(player);
				TemperatureCapability cap = CapabilityUtil.getTempCapability(player);
				float playerTemp = MathUtil.round(cap.getTemperatureLevel(), 2);
				float worldTemp =  TemperatureUtil.getWorldTemperature(player.level(), player.blockPosition());

				String reply = "Temp: " +  playerTemp + "\nTarget Temp: " + targetTemperature + "\nWorld Temp: " + worldTemp;

				source.sendSuccess(() -> Component.literal(reply), false);
			}
		}
		catch(Exception e) 
		{
			LegendarySurvivalOverhaul.LOGGER.error(e.getMessage());
		}
		return Command.SINGLE_SUCCESS;
	}
	private int set(CommandSourceStack src, int temp) throws CommandSyntaxException
	{
		CapabilityUtil.getTempCapability(src.getPlayerOrException()).setTemperatureLevel(temp);
		return Command.SINGLE_SUCCESS;
	}
}
