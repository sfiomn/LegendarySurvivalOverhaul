package icey.survivaloverhaul.api.config.json.temperature;

import icey.survivaloverhaul.api.config.json.JsonItemIdentity;

public class JsonArmorIdentity extends JsonTemperatureIdentity
{
	
	public float insulation;
	
	public JsonArmorIdentity(float temperature, JsonItemIdentity identity)
	{
		this(temperature, identity, 1.0f);
	}
	
	public JsonArmorIdentity(float temperature, JsonItemIdentity identity, float insulation)
	{
		super(temperature, identity);
		
		this.insulation = insulation;
	}

}
