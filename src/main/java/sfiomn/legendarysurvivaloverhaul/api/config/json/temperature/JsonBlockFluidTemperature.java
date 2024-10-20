package sfiomn.legendarysurvivaloverhaul.api.config.json.temperature;

import com.google.gson.annotations.SerializedName;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import sfiomn.legendarysurvivaloverhaul.api.config.json.JsonPropertyValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Code taken and adapted from Charles445's SimpleDifficulty mod
 * @see <a href="https://github.com/Charles445/SimpleDifficulty/tree/master/src/main/java/com/charles445/simpledifficulty/api/config/json">Github Link</a>
 * @author Charles445
 * @author Icey
 */

public class JsonBlockFluidTemperature
{
	@SerializedName("properties")
	public Map<String,String> properties;
	
	@SerializedName("temperature")
	public float temperature;
	
	public JsonBlockFluidTemperature(float temperature, JsonPropertyValue... props)
	{
		this.temperature = temperature;
		this.properties = new HashMap<>();
		
		for (JsonPropertyValue prop : props)
		{
			properties.put(prop.name, prop.value);
		}
	}
	
	public JsonPropertyValue[] getPropertyArray()
	{
		List<JsonPropertyValue> jpvList = new ArrayList<>();
		for(Map.Entry<String, String> entry : properties.entrySet())
		{
			jpvList.add(new JsonPropertyValue(entry.getKey(), entry.getValue()));
		}
		
		return jpvList.toArray(new JsonPropertyValue[0]);
	}
	
	public boolean matchesState(BlockState blockState)
	{
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
			}
		}

		return true;
	}
	
	public boolean matchesProperties(JsonPropertyValue... props)
	{
		if(props.length != properties.keySet().size())
		{
			return false;
		}
		
		for(JsonPropertyValue prop : props)
		{
			if(!properties.containsKey(prop.name))
			{
				return false;
			}
			else
			{
				if(!prop.value.equalsIgnoreCase(properties.get(prop.name)))
				{
					return false;
				}
			}
		}
		
		return true;
	}
}
