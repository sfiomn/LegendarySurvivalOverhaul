package sfiomn.legendarysurvivaloverhaul.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.server.command.EnumArgument;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.bodydamage.BodyDamageCapability;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

public class BodyDamageCommand extends CommandBase
{
	//.executes(src -> new TemperatureCommand().execute(src.getSource())));
	public BodyDamageCommand() {
		super(Commands.literal("bodydamage")
				.requires((p_198521_0_) -> p_198521_0_.hasPermission(2))
				.then(Commands.literal("set")
					.then(Commands.argument("BodyPart", EnumArgument.enumArgument(BodyPartEnum.class))
						.then(Commands.argument("Health", FloatArgumentType.floatArg(0))
							.executes(src -> new BodyDamageCommand().set(src.getSource(), src.getArgument("BodyPart", BodyPartEnum.class), FloatArgumentType.getFloat(src, "Health"))))))
				.then(Commands.literal("get")
						.then(Commands.argument("BodyPart", EnumArgument.enumArgument(BodyPartEnum.class))
								.executes(src -> new BodyDamageCommand().get(src.getSource(), src.getArgument("BodyPart", BodyPartEnum.class)))))
				);
	}

	public int get(CommandSource source, BodyPartEnum bodyPart) {
		try {
			if (source.getEntity() instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) source.getEntity();
				BodyDamageCapability cap = CapabilityUtil.getBodyDamageCapability(player);
				float bodyPartMaxHealth = cap.getBodyPartMaxHealth(bodyPart);
				float bodyPartHealth = bodyPartMaxHealth - cap.getBodyPartDamage(bodyPart);

				String reply = "Body Limb " + bodyPart.name() + " Health : " +  bodyPartHealth + "/" + bodyPartMaxHealth;

				source.getPlayerOrException().sendMessage(new StringTextComponent(reply), source.getEntity().getUUID());
			}
		}
		catch(Exception e) 
		{
			LegendarySurvivalOverhaul.LOGGER.error(e.getMessage());
		}
		return Command.SINGLE_SUCCESS;
	}

	private int set(CommandSource src, BodyPartEnum bodyPart, float healthValue) throws CommandSyntaxException {
		BodyDamageCapability cap = CapabilityUtil.getBodyDamageCapability(src.getPlayerOrException());
		cap.setBodyPartDamage(bodyPart, cap.getBodyPartMaxHealth(bodyPart) -  healthValue);
		return Command.SINGLE_SUCCESS;
	}
}
