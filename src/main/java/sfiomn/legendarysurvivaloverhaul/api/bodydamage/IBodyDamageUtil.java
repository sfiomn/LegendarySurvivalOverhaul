package sfiomn.legendarysurvivaloverhaul.api.bodydamage;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import sfiomn.legendarysurvivaloverhaul.common.items.heal.BodyHealingItem;

import java.util.List;

public interface IBodyDamageUtil
{
    public List<Pair<Effect, Integer>> getEffects(MalusBodyPartEnum bodyPart, float headHealthRatio);

    public void applyHealingItem(PlayerEntity player, BodyPartEnum bodyPartEnum, BodyHealingItem item);

    public void healBodyPart(PlayerEntity player, BodyPartEnum bodyPartEnum, float healingValue);

    public void hurtBodyPart(PlayerEntity player, BodyPartEnum bodyPartEnum, float damageValue);

    public void balancedHurtBodyParts(PlayerEntity player, List<BodyPartEnum> bodyParts, float damageValue);

    public void randomHurtBodyParts(PlayerEntity player, List<BodyPartEnum> bodyParts, float damageValue);

    public float getHealthRatio(PlayerEntity player, BodyPartEnum bodyPartEnum);

    public float getTotalRemainingHealing(PlayerEntity player, BodyPartEnum bodyPartEnum);

    public float getMaxHealth(PlayerEntity player, BodyPartEnum bodyPartEnum);
}
