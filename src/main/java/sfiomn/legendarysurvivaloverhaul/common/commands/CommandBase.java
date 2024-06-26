package sfiomn.legendarysurvivaloverhaul.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.CommandSourceStack;

//cmd execute template
public class CommandBase
{
	// may not be used in most cases but still might be useful in this scope
	CommandSourceStack source;
	LiteralArgumentBuilder<CommandSourceStack> builder;
	
	public CommandBase(LiteralArgumentBuilder<CommandSourceStack> builder)
	{
		this.builder = builder;
	}
		
	public LiteralArgumentBuilder<CommandSourceStack> getBuilder() { return builder;}
		
	public int get(CommandSourceStack source) throws CommandSyntaxException {return Command.SINGLE_SUCCESS;}
	public int get(CommandSourceStack source, String[] args) throws CommandSyntaxException {return Command.SINGLE_SUCCESS;}
}
