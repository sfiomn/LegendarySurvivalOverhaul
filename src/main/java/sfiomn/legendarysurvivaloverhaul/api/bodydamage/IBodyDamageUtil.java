package sfiomn.legendarysurvivaloverhaul.api.bodydamage;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffect;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface IBodyDamageUtil
{
    public List<Pair<MobEffect, Integer>> getEffects(MalusBodyPartEnum bodyPart, float headHealthRatio);

    public void applyHealingTimeBodyPart(Player player, BodyPartEnum bodyPartEnum, float healingValue, int healingTime);

    public void healBodyPart(Player player, BodyPartEnum bodyPartEnum, float healingValue);

    public void hurtBodyPart(Player player, BodyPartEnum bodyPartEnum, float damageValue);

    public void balancedHurtBodyParts(Player player, List<BodyPartEnum> bodyParts, float damageValue);

    public void randomHurtBodyParts(Player player, List<BodyPartEnum> bodyParts, float damageValue);

    public float getHealthRatio(Player player, BodyPartEnum bodyPartEnum);

    public float getTotalRemainingHealing(Player player, BodyPartEnum bodyPartEnum);

    public float getMaxHealth(Player player, BodyPartEnum bodyPartEnum);
}
