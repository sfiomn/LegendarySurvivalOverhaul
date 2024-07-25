package sfiomn.legendarysurvivaloverhaul.api.config.json.bodydamage;

import com.google.gson.annotations.SerializedName;

public class JsonConsumableHeal {
    @SerializedName("healingCharges")
    public int healingCharges;
    @SerializedName("healingValue")
    public float healingValue;
    @SerializedName("healingTime")
    public int healingTime;

    public JsonConsumableHeal(int healingCharges, float healingValue, int healingTime) {
        this.healingCharges = healingCharges;
        this.healingValue = healingValue;
        this.healingTime = healingTime;
    }
}
