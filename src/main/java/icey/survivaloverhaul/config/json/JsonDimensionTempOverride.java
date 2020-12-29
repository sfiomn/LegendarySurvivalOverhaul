package icey.survivaloverhaul.config.json;

import com.google.gson.annotations.SerializedName;

/**
 * .json file used for overriding overall dimension temperature.
 * This should be called before biome overrides.
 * @author Icey
 */
public class JsonDimensionTempOverride
{
	@SerializedName("override_temperature")
	public float overrideTemperature;
	
	public JsonDimensionTempOverride(float overrideTemperature)
	{
		this.overrideTemperature = overrideTemperature;
	}

}
