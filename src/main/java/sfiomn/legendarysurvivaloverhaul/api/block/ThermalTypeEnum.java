package sfiomn.legendarysurvivaloverhaul.api.block;

public enum ThermalTypeEnum {
    COOLING("cooling", -1.0f),
    HEATING("heating", 1.0f),
    BROKEN("broken", 0f);

    private final String name;
    private final float temperature;

    ThermalTypeEnum(String name, float temperature) {
        this.name = name;
        this.temperature = temperature;
    }

    public String getName() {
        return name;
    }

    public float getTemperatureMultiplier() {
        return temperature;
    }
}
