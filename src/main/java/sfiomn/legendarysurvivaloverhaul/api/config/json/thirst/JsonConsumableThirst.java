package sfiomn.legendarysurvivaloverhaul.api.config.json.thirst;

import com.google.gson.annotations.SerializedName;

public class JsonConsumableThirst
{
	@SerializedName("hydration")
	public int hydration;
	@SerializedName("saturation")
	public float saturation;
	@SerializedName("effectChance")
	public float effectChance;
	@SerializedName("effect")
	public String effect;

	public JsonConsumableThirst(int hydration, float saturation, float effectChance, String effect) {
		this.hydration = hydration;
		this.saturation = saturation;
		this.effectChance = effectChance;
		this.effect = effect;
	}
}
