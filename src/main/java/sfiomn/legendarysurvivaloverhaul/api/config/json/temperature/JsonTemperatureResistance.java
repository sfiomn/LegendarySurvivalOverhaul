package sfiomn.legendarysurvivaloverhaul.api.config.json.temperature;

import com.google.gson.annotations.SerializedName;

public class JsonTemperatureResistance
{
	@SerializedName("temperature")
	public float temperature;

	@SerializedName("heat_resistance")
	public float heatResistance;

	@SerializedName("cold_resistance")
	public float coldResistance;

	@SerializedName("thermal_resistance")
	public float thermalResistance;

	public JsonTemperatureResistance()
	{
		this(0, 0, 0, 0);
	}

	public JsonTemperatureResistance(float temperature)
	{
		this(temperature, 0, 0, 0);
	}

	public JsonTemperatureResistance(float temperature, float heatResistance, float coldResistance, float thermalResistance)
	{
		this.temperature = temperature;
		this.heatResistance = heatResistance;
		this.coldResistance = coldResistance;
		this.thermalResistance = thermalResistance;
	}

	public void add(JsonTemperatureResistance config) {
		this.temperature += config.temperature;
		this.heatResistance += config.heatResistance;
		this.coldResistance += config.coldResistance;
		this.thermalResistance += config.thermalResistance;
	}
}
