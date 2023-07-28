package sfiomn.legendarysurvivaloverhaul.api.config.json.temperature;

import com.google.gson.annotations.SerializedName;

public class JsonThirst
{
	@SerializedName("thirst")
	public int thirst;
	@SerializedName("saturation")
	public float saturation;
	@SerializedName("dirty")
	public float dirty;

	public JsonThirst(int thirst, float saturation, float dirty) {
		this.thirst = thirst;
		this.saturation = saturation;
		this.dirty = dirty;
	}
}
