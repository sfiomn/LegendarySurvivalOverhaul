package icey.survivaloverhaul.api.config.json;

import javax.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class JsonBiomeIdentity
{
	@SerializedName("temperature")
	public float temperature;
	
	@SerializedName("isDry")
	public boolean isDry;
	
	public JsonBiomeIdentity(float temperature)
	{
		this(temperature, false);
	}
	
	public JsonBiomeIdentity(float temperature, boolean isDry)
	{
		this.temperature = temperature;
		this.isDry = isDry;
	}
}