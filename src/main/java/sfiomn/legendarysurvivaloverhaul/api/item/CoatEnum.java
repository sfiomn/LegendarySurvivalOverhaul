package sfiomn.legendarysurvivaloverhaul.api.item;

import sfiomn.legendarysurvivaloverhaul.config.Config;

import java.util.Objects;

public enum CoatEnum {
    THERMAL_1("thermal1", "thermal", Config.Baked.thermalCoat1Modifier),
    THERMAL_2("thermal2", "thermal", Config.Baked.thermalCoat2Modifier),
    THERMAL_3("thermal3", "thermal", Config.Baked.thermalCoat3Modifier),
    COOLING_1("cooling1", "cooling", Config.Baked.coolingCoat1Modifier),
    COOLING_2("cooling2", "cooling", Config.Baked.coolingCoat2Modifier),
    COOLING_3("cooling3", "cooling", Config.Baked.coolingCoat3Modifier),
    HEATING_1("heating1", "heating", Config.Baked.heatingCoat1Modifier),
    HEATING_2("heating2", "heating", Config.Baked.heatingCoat2Modifier),
    HEATING_3("heating3", "heating", Config.Baked.heatingCoat3Modifier);

    private final String coatId;
    private final String coatType;
    private final double modifier;
    CoatEnum(String coatId, String coatType, double modifier) {
        this.coatId = coatId;
        this.coatType = coatType;
        this.modifier = modifier;
    }

    public String id() {
        return this.coatId;
    }

    public String type() {
        return this.coatType;
    }

    public double modifier() {
        return this.modifier;
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
