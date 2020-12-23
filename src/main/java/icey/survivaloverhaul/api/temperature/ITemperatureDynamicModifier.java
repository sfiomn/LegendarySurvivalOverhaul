package icey.survivaloverhaul.api.temperature;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Temperature Modifier that runs after other modifiers.<br>
 * It can take the old temperature and replace it with a new one.<br>
 * This lets it make smart decisions, but should be used sparingly,<br>
 * as multiple dynamic modifiers conflict and may create unexpected results!
 */

// code shamelessly ripped from SimpleDifficulty
// https://github.com/Charles445/SimpleDifficulty/blob/v0.3.4/src/main/java/com/charles445/simpledifficulty/api/temperature/ITemperatureDynamicModifier.java
public interface ITemperatureDynamicModifier
{
	/**
	 * Temperature change that relies on the player<br>
	 * Takes the old temperature and replaces it with a new one
	 * @param player
	 * @param currentTemperature
	 * @return newTemperature
	 */
	public float applyDynamicPlayerInfluence(PlayerEntity player, float currentTemperature);

	/**
	 * Temperature change that relies on the world<br>
	 * Takes the old temperature and replaces it with a new one
	 * @param world
	 * @param pos
	 * @return temperature influence on the world
	 */
	public float applyDynamicWorldInfluence(World world, BlockPos pos, float currentTemperature);
	
	/**
	 * The name of your modifier. Must be unique!<br>
	 * To make it easier, you can add your mod ID to this.
	 * @return modifier name
	 */
	public String getName();
}
