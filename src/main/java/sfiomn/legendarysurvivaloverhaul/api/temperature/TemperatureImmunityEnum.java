package sfiomn.legendarysurvivaloverhaul.api.temperature;

public enum TemperatureImmunityEnum {
    HIGH_ALTITUDE(1, "high_altitude"),
    ON_FIRE(2, "on_fire");

    public int id;
    public String immunityName;

    TemperatureImmunityEnum(int id, String immunityName) {
        this.id = id;
        this.immunityName = immunityName;
    }
}
