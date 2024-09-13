package sfiomn.legendarysurvivaloverhaul.api.config.json.thirst;

import com.google.gson.annotations.SerializedName;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.Property;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.config.json.JsonPropertyValue;

import java.util.*;

public class JsonBlockFluidThirst
{
	@SerializedName("hydration")
	public int hydration;
	@SerializedName("saturation")
	public float saturation;
	@SerializedName("effects")
	public List<JsonEffectParameter> effects;
	@SerializedName("properties")
	public Map<String,String> properties;

	public JsonBlockFluidThirst(int hydration, float saturation, JsonEffectParameter[] effects, JsonPropertyValue... properties) {
		this.hydration = hydration;
		this.saturation = saturation;

		this.effects = new ArrayList<>();
        this.effects.addAll(Arrays.asList(effects));

		this.properties = new HashMap<>();
		for (JsonPropertyValue prop : properties)
		{
			this.properties.put(prop.name, prop.value);
		}
	}

	public JsonPropertyValue[] getPropertyArray() {
		List<JsonPropertyValue> jpvList = new ArrayList<>();
		for(Map.Entry<String, String> entry : this.properties.entrySet())
		{
			jpvList.add(new JsonPropertyValue(entry.getKey(), entry.getValue()));
		}

		return jpvList.toArray(new JsonPropertyValue[0]);
	}

	public boolean isDefault() {
		return this.properties.isEmpty();
	}

	public boolean matchesState(BlockState blockState) {
		for(Property<?> property : blockState.getProperties())
		{
			String name = property.getName();

			if(properties.containsKey(name))
			{
				String stateValue = blockState.getValue(property).toString();

				if(!properties.get(name).equalsIgnoreCase(stateValue))
				{
					return false;
				}
			} else {
				return false;
			}
		}

		return true;
	}

	public boolean matchesState(FluidState fluidState)
	{
		for(Property<?> property : fluidState.getProperties())
		{
			String name = property.getName();

			if(properties.containsKey(name))
			{
				String stateValue = fluidState.getValue(property).toString();

				if(!properties.get(name).equalsIgnoreCase(stateValue))
				{
					return false;
				}
			} else {
				return false;
			}
		}

		return true;
	}

	public boolean matchesProperties(JsonPropertyValue... props)
	{
		if(props.length != this.properties.keySet().size())
		{
			return false;
		}

		for(JsonPropertyValue prop : props)
		{
			if(!this.properties.containsKey(prop.name))
			{
				return false;
			}
			else if(!prop.value.equals(this.properties.get(prop.name)))
			{
				return false;
			}
		}

		return true;
	}
}
