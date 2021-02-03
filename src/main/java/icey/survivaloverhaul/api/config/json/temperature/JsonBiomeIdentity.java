package icey.survivaloverhaul.api.config.json.temperature;

import com.google.gson.annotations.SerializedName;

public class JsonBiomeIdentity
{
	@SerializedName("temperature")
	public float temperature;
	
	@SerializedName("isDry")
	public boolean isDry;
	
	@SerializedName("scorched")
	public boolean scorched;
	
	public JsonBiomeIdentity(float temperature)
	{
		this(temperature, false, false);
	}
	
	public JsonBiomeIdentity(float temperature, boolean isDry)
	{
		this(temperature, isDry, false);
	}
	
	public JsonBiomeIdentity(float temperature, boolean isDry, boolean scorched)
	{
		this.temperature = temperature;
		this.isDry = isDry;
		this.scorched = scorched;
	}
}
