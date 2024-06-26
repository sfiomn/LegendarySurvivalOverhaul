package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
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
	public float getPlayerInfluence(Player player)
	{
		if (Config.Baked.playerHuddlingRadius == 0 || Config.Baked.playerHuddlingModifier == 0.0d)
			return 0.0f;
		
		Level world = player.getCommandSenderWorld();
		BlockPos pos = player.blockPosition();
		
		int huddleRadius = Config.Baked.playerHuddlingRadius;
		
		AABB bounds = new AABB(pos.offset(-huddleRadius, -huddleRadius, -huddleRadius), pos.offset(huddleRadius, huddleRadius, huddleRadius));

		List<? extends Player> players = world.getNearbyPlayers(TargetingConditions.DEFAULT, player, bounds);
		
		int playerCount = 0;
		
		for (Player p : players)
		{
			if (!p.isCreative() && !p.isSpectator())
			{
				playerCount++;
			}
		}
		
		return (float)( ((double) playerCount) * Config.Baked.playerHuddlingModifier);
	}
}
