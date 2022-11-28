package sfiomn.legendarysurvivaloverhaul.api.config.json.temperature;

import com.google.gson.annotations.SerializedName;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;

public class JsonFuelItemIdentity {
    @SerializedName("thermalType")
    public ThermalTypeEnum thermalType;

    @SerializedName("fuelValue")
    public int fuelValue;

    public JsonFuelItemIdentity(ThermalTypeEnum thermalType, int fuelValue)
    {
        this.thermalType = thermalType;
        this.fuelValue = fuelValue;
    }
}
