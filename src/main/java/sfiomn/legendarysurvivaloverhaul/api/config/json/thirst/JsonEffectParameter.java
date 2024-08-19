package sfiomn.legendarysurvivaloverhaul.api.config.json.thirst;

import com.google.gson.annotations.SerializedName;

public class JsonEffectParameter
{
	@SerializedName("chance")
	public float chance;
	@SerializedName("effect")
	public String name;
	@SerializedName("duration")
	public int duration;
	@SerializedName("amplifier")
	public int amplifier;

	public JsonEffectParameter(String name, float chance, int duration, int amplifier)
	{
		this.name = name;
        this.chance = chance < 0 ? 0: chance > 1 ? 1: chance;
		this.duration = Math.max(duration, 0);
		this.amplifier = Math.max(amplifier, 0);
	}
}
