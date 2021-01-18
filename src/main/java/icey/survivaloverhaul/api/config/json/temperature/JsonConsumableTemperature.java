package icey.survivaloverhaul.api.config.json.temperature;

import icey.survivaloverhaul.api.config.json.JsonItemIdentity;

/**
 * Code taken and adapted from Charles445's SimpleDifficulty mod
 * @see <a href="https://github.com/Charles445/SimpleDifficulty/tree/master/src/main/java/com/charles445/simpledifficulty/api/config/json">Github Link</a>
 * @author Charles445
 * @author Icey
 */

public class JsonConsumableTemperature
{
	public JsonItemIdentity identity;
	
	public String group;
	public float temperature;
	public int duration;
	
	public JsonConsumableTemperature(String group, float temperature, int duration, String nbt)
	{
		this(group, temperature, duration, new JsonItemIdentity(nbt));
	}
	
	public JsonConsumableTemperature(String group, float temperature, int duration, JsonItemIdentity identity)
	{
		this.temperature = temperature;
		this.duration = duration;
		this.group = group.toLowerCase();
		this.identity = identity;
	}
}
