package icey.survivaloverhaul.api.config.json;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

/**
 * Code taken and adapted from Charles445's SimpleDifficulty mod
 * @see <a href="https://github.com/Charles445/SimpleDifficulty/tree/master/src/main/java/com/charles445/simpledifficulty/api/config/json">Github Link</a>
 * @author Charles445
 * @author Icey
 */

public class JsonTemperatureIdentity
{
	public JsonItemIdentity identity;
	
	public float temperature;
	
	public JsonTemperatureIdentity(float temperature, String nbt)
	{
		this(temperature, new JsonItemIdentity(nbt));
	}
	
	public JsonTemperatureIdentity(float temperature, JsonItemIdentity identity)
	{
		this.temperature = temperature;
		this.identity = identity;
	}
	
	// Identity matching
	
	public boolean matches(ItemStack stack)
	{
		return identity.matches(stack);
	}
	
	public boolean matches(JsonItemIdentity sentIdentity)
	{
		return identity.matches(sentIdentity);
	}
	
	public boolean matches(CompoundNBT compound)
	{
		return identity.matches(compound);
	}
}
