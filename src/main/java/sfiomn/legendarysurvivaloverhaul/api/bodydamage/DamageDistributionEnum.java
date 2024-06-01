package sfiomn.legendarysurvivaloverhaul.api.bodydamage;

import net.minecraft.entity.player.PlayerEntity;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

import java.util.Collections;
import java.util.List;

public enum DamageDistributionEnum {
    ONE_OF(),
    ALL();

    DamageDistributionEnum() {}

    public List<BodyPartEnum> getBodyParts(PlayerEntity player, List<BodyPartEnum> bodyParts) {
        if (this == DamageDistributionEnum.ONE_OF) {
            return Collections.singletonList(bodyParts.get(player.getRandom().nextInt(bodyParts.size())));
        }
        return bodyParts;
    }
}
