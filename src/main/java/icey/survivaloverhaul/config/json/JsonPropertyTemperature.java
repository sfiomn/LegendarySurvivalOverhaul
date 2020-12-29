package icey.survivaloverhaul.config.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;

public class JsonPropertyTemperature
{
	@SerializedName("properties")
	public Map<String, String> properties;
	
	@SerializedName("temperature")
	public float temperature;
	
	public JsonPropertyTemperature(float temperature, JsonPropertyValue...props)
	{
		this.temperature = temperature;
		this.properties = new HashMap<String, String>();
		
		for(JsonPropertyValue prop : props)
		{
			properties.put(prop.property, prop.value);
		}
	}
	
	public JsonPropertyValue[] getAsPropertyArray()
	{
		List<JsonPropertyValue> jpvList = new ArrayList<JsonPropertyValue>();
		for(Map.Entry<String, String> entry : properties.entrySet())
		{
			jpvList.add(new JsonPropertyValue(entry.getKey(), entry.getValue()));
		}
		// Cast to an array to avoid a ClassCastException
		return jpvList.toArray(new JsonPropertyValue[0]);
	}
	
	public boolean matchesState(StateContainer<Block, BlockState> stateContainer)
	{
		for(Property<?> prop : stateContainer.getProperties())
		{
			String propName = prop.getName();
			
			if(properties.containsKey(propName))
			{
				String stateValue = stateContainer.getProperty(propName).toString();
				
				if(!properties.get(propName).equals(stateValue))
				{
					return false;
				}
			}
		}
		
		return true;
	}
	
	public boolean matchesDescribedPRoperties(JsonPropertyValue... props)
	{
		if(props.length != properties.keySet().size())
		{
			return false;
		}
		
		for(JsonPropertyValue prop : props)
		{
			if(!properties.containsKey(prop.property))
			{
				return false;
			}
			else
			{
				if(!prop.value.equals(properties.get(prop)))
				{
					return false;
				}
			}
		}
		
		return true;
	}
}
