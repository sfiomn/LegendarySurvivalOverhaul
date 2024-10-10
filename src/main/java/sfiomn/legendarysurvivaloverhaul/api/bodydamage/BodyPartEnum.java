package sfiomn.legendarysurvivaloverhaul.api.bodydamage;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.*;

public enum BodyPartEnum {
    HEAD(),
    RIGHT_ARM(),
    LEFT_ARM(),
    CHEST(),
    RIGHT_LEG(),
    RIGHT_FOOT(),
    LEFT_LEG(),
    LEFT_FOOT();

    BodyPartEnum() {}

    public List<BodyPartEnum> getNeighbours() {
        switch (this) {
            case HEAD:
            case LEFT_ARM:
            case RIGHT_ARM:
                return singletonList(CHEST);
            case CHEST:
                return Arrays.asList(HEAD, LEFT_ARM, RIGHT_ARM, LEFT_LEG, RIGHT_LEG);
            case LEFT_LEG:
                return Arrays.asList(CHEST, RIGHT_LEG, LEFT_FOOT);
            case LEFT_FOOT:
                return Arrays.asList(LEFT_LEG, RIGHT_FOOT);
            case RIGHT_LEG:
                return Arrays.asList(CHEST, LEFT_LEG, RIGHT_FOOT);
            case RIGHT_FOOT:
                return Arrays.asList(RIGHT_LEG, LEFT_FOOT);
            default:
                return emptyList();
        }
    }
}
