package icey.survivaloverhaul.config.json;

import com.google.gson.annotations.SerializedName;

/**
 * .json file used for overriding the default temperature of a biome.
 * This should be called after a dimension's temperature overrides.
 * @author Icey
 */
public class JsonBiomeTempOverride
{
	@SerializedName("override_temperature")
	public float overrideTemperature;
	
	public JsonBiomeTempOverride(float overrideTemperature)
	{
		this.overrideTemperature = overrideTemperature;
	}
}
