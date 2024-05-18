package sfiomn.legendarysurvivaloverhaul.api.config.json.thirst;

import com.google.gson.annotations.SerializedName;

public class JsonThirst
{
	@SerializedName("hydration")
	public int hydration;
	@SerializedName("saturation")
	public float saturation;
	@SerializedName("dirty")
	public float dirty;

	public JsonThirst(int hydration, float saturation, float dirty) {
		this.hydration = hydration;
		this.saturation = saturation;
		this.dirty = dirty;
	}
}
