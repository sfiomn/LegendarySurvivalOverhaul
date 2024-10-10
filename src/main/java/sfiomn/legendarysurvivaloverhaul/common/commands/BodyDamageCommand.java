package sfiomn.legendarysurvivaloverhaul.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraftforge.server.command.EnumArgument;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.bodydamage.BodyDamageCapability;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

public class BodyDamageCommand extends CommandBase
{
	public BodyDamageCommand() {
		super(Commands.literal("bodydamage")
				.requires((p_198521_0_) -> p_198521_0_.hasPermission(2))
				.then(Commands.argument("target", EntityArgument.entity())
				.then(Commands.literal("set")
					.then(Commands.argument("BodyPart", EnumArgument.enumArgument(BodyPartEnum.class))
						.then(Commands.argument("Health", FloatArgumentType.floatArg(0))
							.executes(src -> new BodyDamageCommand().set(src.getSource(), EntityArgument.getEntity(src, "target"), src.getArgument("BodyPart", BodyPartEnum.class), FloatArgumentType.getFloat(src, "Health")))))
					.then(Commands.literal("ALL")
							.then(Commands.argument("Health", FloatArgumentType.floatArg(0))
							.executes(src -> new BodyDamageCommand().setAll(src.getSource(), EntityArgument.getEntity(src, "target"), FloatArgumentType.getFloat(src, "Health"))))))
				.then(Commands.literal("heal")
						.then(Commands.argument("BodyPart", EnumArgument.enumArgument(BodyPartEnum.class))
								.then(Commands.argument("Health", FloatArgumentType.floatArg(0))
										.executes(src -> new BodyDamageCommand().heal(src.getSource(), EntityArgument.getEntity(src, "target"), src.getArgument("BodyPart", BodyPartEnum.class), FloatArgumentType.getFloat(src, "Health")))))
						.then(Commands.literal("ALL")
								.then(Commands.argument("Health", FloatArgumentType.floatArg(0))
										.executes(src -> new BodyDamageCommand().healAll(src.getSource(), EntityArgument.getEntity(src, "target"), FloatArgumentType.getFloat(src, "Health"))))))
				.then(Commands.literal("get")
						.then(Commands.argument("BodyPart", EnumArgument.enumArgument(BodyPartEnum.class))
								.executes(src -> new BodyDamageCommand().get(src.getSource(), EntityArgument.getEntity(src, "target"), src.getArgument("BodyPart", BodyPartEnum.class))))
						.then(Commands.literal("ALL")
								.executes(src -> new BodyDamageCommand().getAll(src.getSource(), EntityArgument.getEntity(src, "target")))))
				));
	}

	public int get(CommandSourceStack source, Entity entity, BodyPartEnum bodyPart) {
		try {
			if (entity instanceof Player player && source.getEntity() instanceof Player) {
				BodyDamageCapability cap = CapabilityUtil.getBodyDamageCapability(player);
				float bodyPartMaxHealth = cap.getBodyPartMaxHealth(bodyPart);
				float bodyPartHealth = bodyPartMaxHealth - cap.getBodyPartDamage(bodyPart);

				String reply = "Body Limb " + bodyPart.name() + " Health : " +  bodyPartHealth + "/" + bodyPartMaxHealth;

				source.sendSuccess(() -> Component.literal(reply), false);
			}
		}
		catch(Exception e) 
		{
			LegendarySurvivalOverhaul.LOGGER.error(e.getMessage());
		}
		return Command.SINGLE_SUCCESS;
	}

	public int getAll(CommandSourceStack source, Entity entity) {
		try {
			if (entity instanceof Player player && source.getEntity() instanceof Player) {
				BodyDamageCapability cap = CapabilityUtil.getBodyDamageCapability(player);
				StringBuilder reply = new StringBuilder();
				for (BodyPartEnum bodyPart: BodyPartEnum.values()) {
					float bodyPartMaxHealth = cap.getBodyPartMaxHealth(bodyPart);
					float bodyPartHealth = bodyPartMaxHealth - cap.getBodyPartDamage(bodyPart);

					reply.append("Body Limb ").append(bodyPart.name()).append(" Health : ").append(bodyPartHealth).append("/").append(bodyPartMaxHealth).append("\n");
				}

				source.sendSuccess(() -> Component.literal(reply.toString()), false);
			}
		}
		catch(Exception e)
		{
			LegendarySurvivalOverhaul.LOGGER.error(e.getMessage());
		}
		return Command.SINGLE_SUCCESS;
	}

	private int set(CommandSourceStack src, Entity entity, BodyPartEnum bodyPart, float healthValue) throws CommandSyntaxException {
		if (entity instanceof Player player) {
			BodyDamageCapability cap = CapabilityUtil.getBodyDamageCapability(player);
			cap.setBodyPartDamage(bodyPart, cap.getBodyPartMaxHealth(bodyPart) -  healthValue);
		}
		return Command.SINGLE_SUCCESS;
	}

	private int setAll(CommandSourceStack src, Entity entity, float healthValue) throws CommandSyntaxException {
		if (entity instanceof Player player) {
			BodyDamageCapability cap = CapabilityUtil.getBodyDamageCapability(player);
			for (BodyPartEnum bodyPart: BodyPartEnum.values())
				cap.setBodyPartDamage(bodyPart, cap.getBodyPartMaxHealth(bodyPart) -  healthValue);
		}
		return Command.SINGLE_SUCCESS;
	}

	private int heal(CommandSourceStack src, Entity entity, BodyPartEnum bodyPart, float healthValue) throws CommandSyntaxException {
		if (entity instanceof Player player) {
			BodyDamageCapability cap = CapabilityUtil.getBodyDamageCapability(player);
			cap.setBodyPartDamage(bodyPart, cap.getBodyPartDamage(bodyPart) -  healthValue);
		}
		return Command.SINGLE_SUCCESS;
	}

	private int healAll(CommandSourceStack src, Entity entity, float healthValue) throws CommandSyntaxException {
		if (entity instanceof Player player) {
			BodyDamageCapability cap = CapabilityUtil.getBodyDamageCapability(player);
			for (BodyPartEnum bodyPart: BodyPartEnum.values())
				cap.setBodyPartDamage(bodyPart, cap.getBodyPartDamage(bodyPart) -  healthValue);
		}
		return Command.SINGLE_SUCCESS;
	}
}
