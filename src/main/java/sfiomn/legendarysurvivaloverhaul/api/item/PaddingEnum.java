package sfiomn.legendarysurvivaloverhaul.api.item;

import java.util.Objects;

public enum PaddingEnum {
    THERMAL_1("thermal1", "thermal", 2),
    THERMAL_2("thermal2", "thermal", 4),
    THERMAL_3("thermal3", "thermal", 6),
    COOLING_1("cooling1", "cooling", 2),
    COOLING_2("cooling2", "cooling", 4),
    COOLING_3("cooling3", "cooling", 6),
    HEATING_1("heating1", "heating", 2),
    HEATING_2("heating2", "heating", 4),
    HEATING_3("heating3", "heating", 6),
    REMOVAL("removal", "removal", 0);

    private final String paddingId;
    private final String paddingType;
    private final float modifier;
    PaddingEnum(String paddingId, String paddingType, float modifier) {
        this.paddingId = paddingId;
        this.paddingType = paddingType;
        this.modifier = modifier;
    }

    public String id() {
        return this.paddingId;
    }

    public String type() {
        return this.paddingType;
    }

    public float modifier() {
        return this.modifier;
    }

    public static PaddingEnum getFromId(String id) {
        for (PaddingEnum padding : PaddingEnum.values()) {
            if (Objects.equals(padding.paddingId, id)) {
                return padding;
            }
        }
        return null;
    }
}
