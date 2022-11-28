package sfiomn.legendarysurvivaloverhaul.api.config.json.temperature;

import sfiomn.legendarysurvivaloverhaul.api.config.json.JsonItemIdentity;

public class JsonArmorIdentity extends JsonTemperatureIdentity
{
	public float insulation;
	
	public JsonArmorIdentity(float temperature, String nbt)
	{
		this(temperature, 0.0f, nbt);
	}
	
	public JsonArmorIdentity(float temperature, float insulation, String nbt)
	{
		super(temperature, nbt);
		this.insulation = insulation;
	}
	
	public JsonArmorIdentity(float temperature, JsonItemIdentity identity)
	{
		this(temperature, 1.0f, identity);
	}
	
	public JsonArmorIdentity(float temperature, float insulation, JsonItemIdentity identity)
	{
		super(temperature, identity);
		this.insulation = insulation;
	}

}
