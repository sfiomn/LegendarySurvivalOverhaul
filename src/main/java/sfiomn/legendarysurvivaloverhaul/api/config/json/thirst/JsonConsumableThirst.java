package sfiomn.legendarysurvivaloverhaul.api.config.json.thirst;

import com.google.gson.annotations.SerializedName;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.config.json.JsonPropertyValue;

import java.util.*;

public class JsonConsumableThirst
{
	@SerializedName("hydration")
	public int hydration;
	@SerializedName("saturation")
	public float saturation;
	@SerializedName("effects")
	public List<JsonEffectParameter> effects;
	@SerializedName("nbt")
	public Map<String,String> nbt;

	public JsonConsumableThirst(int hydration, float saturation, JsonEffectParameter[] effects, JsonPropertyValue... nbt) {
		this.hydration = hydration;
		this.saturation = saturation;

		this.effects = new ArrayList<>();
        this.effects.addAll(Arrays.asList(effects));

		this.nbt = new HashMap<>();
		for (JsonPropertyValue prop : nbt)
		{
			this.nbt.put(prop.name, prop.value);
		}
	}

	public JsonPropertyValue[] getNbtArray() {
		List<JsonPropertyValue> jpvList = new ArrayList<>();
		for(Map.Entry<String, String> entry : this.nbt.entrySet())
		{
			jpvList.add(new JsonPropertyValue(entry.getKey(), entry.getValue()));
		}

		return jpvList.toArray(new JsonPropertyValue[0]);
	}

	public boolean matchesNbt(ItemStack itemStack) {
		if (itemStack.hasTag() == nbt.isEmpty())
			return false;

		CompoundNBT itemStackTag = itemStack.getTag();

		if (itemStackTag == null && nbt.isEmpty())
			return true;

		assert itemStackTag != null;

		for(Map.Entry<String, String> nbtEntry : this.nbt.entrySet()) {
			if (!itemStackTag.contains(nbtEntry.getKey()))
				return false;

			byte tagType = itemStackTag.getTagType(nbtEntry.getKey());
			//  String type
			if (tagType == 8) {
				if (!itemStackTag.getString(nbtEntry.getKey()).equals(nbtEntry.getValue()))
					return false;

				//  Numerical type
			} else if (tagType == 1 || tagType == 2 || tagType == 3 || tagType == 4 || tagType == 5 || tagType == 6) {
				if (itemStackTag.getDouble(nbtEntry.getKey()) != Double.parseDouble(nbtEntry.getValue()))
					return false;
			} else {
				LegendarySurvivalOverhaul.LOGGER.error("Error while matching nbt for {} : Tag type {} not taken into account.\n" +
						"It can either be a String (tag type 8) or a numeric (tag type in [1-6])", itemStack.getDescriptionId(), tagType);
			}
		}

		return true;
	}

	public boolean isDefault() {
		return this.nbt.isEmpty();
	}

	public boolean matchesNbt(JsonPropertyValue... nbt)
	{
		if(nbt.length != this.nbt.keySet().size())
		{
			return false;
		}

		for(JsonPropertyValue prop : nbt)
		{
			if(!this.nbt.containsKey(prop.name))
			{
				return false;
			}
			else if(!prop.value.equals(this.nbt.get(prop.name)))
			{
				return false;
			}
		}

		return true;
	}
}
