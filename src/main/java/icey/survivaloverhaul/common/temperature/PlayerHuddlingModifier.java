package icey.survivaloverhaul.common.temperature;

import java.util.List;

import icey.survivaloverhaul.api.temperature.ModifierBase;
import icey.survivaloverhaul.config.Config;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
		
		World world = player.getEntityWorld();
		BlockPos pos = player.getPosition();
		
		int huddleRadius = Config.Baked.playerHuddlingRadius;
		
		AxisAlignedBB bounds = new AxisAlignedBB(pos.add(-huddleRadius, -huddleRadius, -huddleRadius), pos.add(huddleRadius, huddleRadius, huddleRadius));
		
		List<Entity> entities = world.getEntitiesInAABBexcluding(player, bounds, null);
		
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
