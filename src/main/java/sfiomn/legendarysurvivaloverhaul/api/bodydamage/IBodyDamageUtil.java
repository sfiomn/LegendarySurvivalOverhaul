package sfiomn.legendarysurvivaloverhaul.api.bodydamage;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffect;
import sfiomn.legendarysurvivaloverhaul.common.items.heal.BodyHealingItem;

import java.util.List;

public interface IBodyDamageUtil
{
    public List<Pair<MobEffect, Integer>> getEffects(MalusBodyPartEnum bodyPart, float headHealthRatio);

    public void applyHealingItem(Player player, BodyPartEnum bodyPartEnum, BodyHealingItem item);

    public void healBodyPart(Player player, BodyPartEnum bodyPartEnum, float healingValue);

    public void hurtBodyPart(Player player, BodyPartEnum bodyPartEnum, float damageValue);

    public void balancedHurtBodyParts(Player player, List<BodyPartEnum> bodyParts, float damageValue);

    public void randomHurtBodyParts(Player player, List<BodyPartEnum> bodyParts, float damageValue);

    public float getHealthRatio(Player player, BodyPartEnum bodyPartEnum);

    public float getTotalRemainingHealing(Player player, BodyPartEnum bodyPartEnum);

    public float getMaxHealth(Player player, BodyPartEnum bodyPartEnum);
}
