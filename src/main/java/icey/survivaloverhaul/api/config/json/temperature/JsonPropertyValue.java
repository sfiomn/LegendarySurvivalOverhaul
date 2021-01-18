package icey.survivaloverhaul.api.config.json.temperature;

/**
 * Code taken and adapted from Charles445's SimpleDifficulty mod
 * @see <a href="https://github.com/Charles445/SimpleDifficulty/tree/master/src/main/java/com/charles445/simpledifficulty/api/config/json">Github Link</a>
 * @author Charles445
 * @author Icey
 */

public class JsonPropertyValue
{
	public String property;
	public String value;
	
	public JsonPropertyValue(String property, String value)
	{
		this.property = property;
		this.value = value;
	}
}
