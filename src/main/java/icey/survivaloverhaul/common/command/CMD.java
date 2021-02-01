package icey.survivaloverhaul.common.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;

//cmd execute template
public class CMD
{
	CommandSource source;
	LiteralArgumentBuilder<CommandSource> builder;
		
	public LiteralArgumentBuilder<CommandSource> getBuilder() { return builder;}
		
	public int execute(CommandSource source) throws CommandSyntaxException {return Command.SINGLE_SUCCESS;}
	public int execute(CommandSource source,String[] args) throws CommandSyntaxException {return Command.SINGLE_SUCCESS;}
}
