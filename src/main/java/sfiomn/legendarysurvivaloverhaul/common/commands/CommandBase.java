package sfiomn.legendarysurvivaloverhaul.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;

//cmd execute template
public class CommandBase
{
	// may not be used in most cases but still might be useful in this scope
	CommandSource source;
	LiteralArgumentBuilder<CommandSource> builder;
	
	public CommandBase(LiteralArgumentBuilder<CommandSource> builder) 
	{
		this.builder = builder;
	}
		
	public LiteralArgumentBuilder<CommandSource> getBuilder() { return builder;}
		
	public int execute(CommandSource source) throws CommandSyntaxException {return Command.SINGLE_SUCCESS;}
	public int execute(CommandSource source,String[] args) throws CommandSyntaxException {return Command.SINGLE_SUCCESS;}
}
