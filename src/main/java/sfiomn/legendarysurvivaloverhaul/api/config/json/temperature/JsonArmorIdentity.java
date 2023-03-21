package sfiomn.legendarysurvivaloverhaul.api.config.json.temperature;

import sfiomn.legendarysurvivaloverhaul.api.config.json.JsonItemIdentity;

public class JsonArmorIdentity extends JsonTemperatureIdentity
{
	
	public JsonArmorIdentity(float temperature, String nbt)
	{
		super(temperature, nbt);
	}
	
	public JsonArmorIdentity(float temperature, JsonItemIdentity identity)
	{
		super(temperature, identity);
	}

}
