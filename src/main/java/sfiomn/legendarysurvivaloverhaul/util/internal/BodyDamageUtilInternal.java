package sfiomn.legendarysurvivaloverhaul.util.internal;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.*;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import java.util.*;
import java.util.stream.Collectors;

public class BodyDamageUtilInternal implements IBodyDamageUtil {
    private static final Map<MalusBodyPartEnum, Map<Float, Pair<MobEffect, Integer>>> bodyPartMalusEffects = new HashMap<>();

    public BodyDamageUtilInternal() {}

    public static void initMalusConfig() {
        for (MalusBodyPartEnum malus: MalusBodyPartEnum.values()) {
            Map<Float, Pair<MobEffect, Integer>> malusEffects = new HashMap<>();
            if (malus.effects.size() != malus.amplifiers.size() || malus.effects.size() != malus.thresholds.size()) {
                LegendarySurvivalOverhaul.LOGGER.debug("{} effects, amplifiers and thresholds elements number doesn't match. The last elements won't be used.", malus.name());
            }

            for (int i=0; i<malus.effects.size(); i++) {
                MobEffect malusEffect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(malus.effects.get(i)));
                int malusAmplifier;
                float malusThreshold;
                if (malusEffect == null) {
                    LegendarySurvivalOverhaul.LOGGER.debug("Unknown effect {} for {}", malus.effects.get(i), malus.name());
                    continue;
                }
                try {
                    malusAmplifier = Math.abs(malus.amplifiers.get(i));
                } catch (IndexOutOfBoundsException e) {
                    LegendarySurvivalOverhaul.LOGGER.debug("No amplifier defined for effect {} in {}", malus.effects.get(i), malus.name());
                    continue;
                }
                try {
                    malusThreshold = (float) Mth.clamp(malus.thresholds.get(i), 0.0f, 1.0f);
                } catch (IndexOutOfBoundsException e) {
                    LegendarySurvivalOverhaul.LOGGER.debug("No threshold defined for effect {} in {}", malus.thresholds.get(i), malus.name());
                    continue;
                }
                malusEffects.put(malusThreshold, Pair.of(malusEffect, malusAmplifier));
            }
            bodyPartMalusEffects.put(malus, malusEffects);
        }
    }

    public List<Pair<MobEffect, Integer>> getEffects(MalusBodyPartEnum bodyPart, float healthRatio) {
        List<Pair<MobEffect, Integer>> effects = new ArrayList<>();
        if(bodyPart == null)
            return effects;

        for (Map.Entry<Float, Pair<MobEffect, Integer>> effect: bodyPartMalusEffects.get(bodyPart).entrySet()) {
            if (healthRatio <= effect.getKey()) {
                effects.add(effect.getValue());
            }
        }
        return effects;
    }

    public void applyHealingTimeBodyPart(Player player, BodyPartEnum bodyPartEnum, float healingValue, int healingTime) {

        if(!Config.Baked.localizedBodyDamageEnabled || bodyPartEnum == null)
            return;

        float healingValuePerTick = healingValue / (float) healingTime;

        IBodyDamageCapability capability = CapabilityUtil.getBodyDamageCapability(player);

        capability.applyHealingTime(bodyPartEnum, healingTime, healingValuePerTick);
    }

    @Override
    public void healBodyPart(Player player, BodyPartEnum bodyPartEnum, float healingValue) {

        if(!Config.Baked.localizedBodyDamageEnabled)
            return;

        IBodyDamageCapability capability = CapabilityUtil.getBodyDamageCapability(player);

        capability.heal(bodyPartEnum, healingValue);
    }

    public void hurtBodyPart(Player player, BodyPartEnum bodyPartEnum, float damageValue) {

        if(!Config.Baked.localizedBodyDamageEnabled || bodyPartEnum == null)
            return;

        IBodyDamageCapability capability = CapabilityUtil.getBodyDamageCapability(player);

        float remainingDamage = Math.max(0, damageValue - (capability.getBodyPartMaxHealth(bodyPartEnum) - capability.getBodyPartDamage(bodyPartEnum)));

        capability.hurt(bodyPartEnum, damageValue - remainingDamage);

        if (remainingDamage > 0 && !bodyPartEnum.getNeighbours().isEmpty()) {
            List<BodyPartEnum> damageableBodyParts = bodyPartEnum.getNeighbours().stream().filter(bodyPart -> capability.getBodyPartHealthRatio(bodyPart) > 0).collect(Collectors.toList());
            if (!damageableBodyParts.isEmpty()){
                BodyPartEnum bodyPart = DamageDistributionEnum.ONE_OF.getBodyParts(player, damageableBodyParts).get(0);
                capability.hurt(bodyPart, remainingDamage);
            }
        }
    }

    public void balancedHurtBodyParts(Player player, List<BodyPartEnum> bodyParts, float damageValue) {

        if(!Config.Baked.localizedBodyDamageEnabled || bodyParts.isEmpty())
            return;

        Collections.shuffle(bodyParts);

        for (BodyPartEnum bodyPart : bodyParts) {
            hurtBodyPart(player, bodyPart, damageValue / bodyParts.size());
        }
    }

    public void randomHurtBodyParts(Player player, List<BodyPartEnum> bodyParts, float damageValue) {
        if(!Config.Baked.localizedBodyDamageEnabled || bodyParts.isEmpty())
            return;
        int bodyPartIndex = bodyParts.size() == 1 ? 0 : player.getRandom().nextInt(bodyParts.size() - 1);

        hurtBodyPart(player, bodyParts.get(bodyPartIndex), damageValue);
    }

    public float getHealthRatio(Player player, BodyPartEnum bodyPartEnum) {
        if(!Config.Baked.localizedBodyDamageEnabled || bodyPartEnum == null)
            return 0.0f;

        IBodyDamageCapability capability = CapabilityUtil.getBodyDamageCapability(player);

        return capability.getBodyPartHealthRatio(bodyPartEnum);
    }

    public float getTotalRemainingHealing(Player player, BodyPartEnum bodyPartEnum) {
        if(!Config.Baked.localizedBodyDamageEnabled || bodyPartEnum == null)
            return 0.0f;

        IBodyDamageCapability capability = CapabilityUtil.getBodyDamageCapability(player);

        return capability.getRemainingHealingTicks(bodyPartEnum) * capability.getHealingPerTicks(bodyPartEnum);
    }

    @Override
    public float getMaxHealth(Player player, BodyPartEnum bodyPartEnum) {
        if(!Config.Baked.localizedBodyDamageEnabled || bodyPartEnum == null)
            return 0.0f;

        IBodyDamageCapability capability = CapabilityUtil.getBodyDamageCapability(player);

        return capability.getBodyPartMaxHealth(bodyPartEnum);
    }
}
