package sfiomn.legendarysurvivaloverhaul.api.config.json.temperature;

import com.google.gson.annotations.SerializedName;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;

public class JsonFuelItem {
    @SerializedName("thermalType")
    public ThermalTypeEnum thermalType;

    @SerializedName("fuelValue")
    public int fuelValue;

    public JsonFuelItem(ThermalTypeEnum thermalType, int fuelValue)
    {
        this.thermalType = thermalType;
        this.fuelValue = fuelValue;
    }
}
