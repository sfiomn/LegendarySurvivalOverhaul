package sfiomn.legendarysurvivaloverhaul.api.item;

import sfiomn.legendarysurvivaloverhaul.config.Config;

import java.util.Objects;

public enum CoatEnum {
    THERMAL_1("thermal1", "thermal"),
    THERMAL_2("thermal2", "thermal"),
    THERMAL_3("thermal3", "thermal"),
    COOLING_1("cooling1", "cooling"),
    COOLING_2("cooling2", "cooling"),
    COOLING_3("cooling3", "cooling"),
    HEATING_1("heating1", "heating"),
    HEATING_2("heating2", "heating"),
    HEATING_3("heating3", "heating");

    private final String coatId;
    private final String coatType;
    CoatEnum(String coatId, String coatType) {
        this.coatId = coatId;
        this.coatType = coatType;
    }

    public String id() {
        return this.coatId;
    }

    public String type() {
        return this.coatType;
    }

    public double modifier() {
        return switch (this) {
            case THERMAL_1 -> Config.Baked.thermalCoat1Modifier;
            case THERMAL_2 -> Config.Baked.thermalCoat2Modifier;
            case THERMAL_3 -> Config.Baked.thermalCoat3Modifier;
            case COOLING_1 -> Config.Baked.coolingCoat1Modifier;
            case COOLING_2 -> Config.Baked.coolingCoat2Modifier;
            case COOLING_3 -> Config.Baked.coolingCoat3Modifier;
            case HEATING_1 -> Config.Baked.heatingCoat1Modifier;
            case HEATING_2 -> Config.Baked.heatingCoat2Modifier;
            case HEATING_3 -> Config.Baked.heatingCoat3Modifier;
        };
    }

    public static CoatEnum getFromId(String id) {
        for (CoatEnum coat : CoatEnum.values()) {
            if (Objects.equals(coat.coatId, id)) {
                return coat;
            }
        }
        return null;
    }
}
