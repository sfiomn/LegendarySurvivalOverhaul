package icey.survivaloverhaul.common.command;

import java.util.UUID;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import icey.survivaloverhaul.Main;
import icey.survivaloverhaul.common.capability.temperature.TemperatureCapability;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class TemperatureCMD
{
	public LiteralArgumentBuilder<CommandSource> builder;
	public TemperatureCMD()
	{
		this.builder = Commands.literal("temperature").executes(context -> execute(context.getSource()));
	}
	
	private int execute(CommandSource source) throws CommandSyntaxException
	{
		try
		{
			source.asPlayer().sendMessage(new StringTextComponent((""+ TemperatureCapability.getTempCapability(source.asPlayer()).getTemperatureLevel() +"")), UUID.randomUUID());
		}
		catch(Exception e) 
		{
			System.out.println(e.getMessage());
		}
		return Command.SINGLE_SUCCESS;
	}
	
}
