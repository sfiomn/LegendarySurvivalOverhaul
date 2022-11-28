package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.config.Config;

import java.util.List;

public class PlayerHuddlingModifier extends ModifierBase
{
	public PlayerHuddlingModifier()
	{
		super();
	}
	

	@Override
	public float getPlayerInfluence(PlayerEntity player)
	{
		if (Config.Baked.playerHuddlingRadius == 0 || Config.Baked.playerHuddlingModifier == 0.0d)
			return 0.0f;
		
		World world = player.getCommandSenderWorld();
		BlockPos pos = player.blockPosition();
		
		int huddleRadius = Config.Baked.playerHuddlingRadius;
		
		AxisAlignedBB bounds = new AxisAlignedBB(pos.offset(-huddleRadius, -huddleRadius, -huddleRadius), pos.offset(huddleRadius, huddleRadius, huddleRadius));
		
		List<Entity> entities = world.getEntities(player, bounds, null);
		
		int playerCount = 0;
		
		for (Entity entity : entities)
		{
			if (entity instanceof PlayerEntity)
			{
				playerCount++;
			}
		}
		
		return (float)( ((double) playerCount) * Config.Baked.playerHuddlingModifier);
	}
}
