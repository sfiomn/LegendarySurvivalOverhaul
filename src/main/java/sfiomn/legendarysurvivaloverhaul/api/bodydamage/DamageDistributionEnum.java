package sfiomn.legendarysurvivaloverhaul.api.bodydamage;

import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

import java.util.Collections;
import java.util.List;

public enum DamageDistributionEnum {
    NONE,
    ONE_OF(),
    ALL();

    DamageDistributionEnum() {}

    public List<BodyPartEnum> getBodyParts(Player player, List<BodyPartEnum> bodyParts) {
        if (this == DamageDistributionEnum.ONE_OF) {
            return Collections.singletonList(bodyParts.get(player.getRandom().nextInt(bodyParts.size())));
        }
        return bodyParts;
    }
}
