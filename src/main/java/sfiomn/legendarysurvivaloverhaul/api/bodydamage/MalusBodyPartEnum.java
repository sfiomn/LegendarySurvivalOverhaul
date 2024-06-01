package sfiomn.legendarysurvivaloverhaul.api.bodydamage;

import com.google.common.collect.ImmutableList;
import sfiomn.legendarysurvivaloverhaul.config.Config;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public enum MalusBodyPartEnum {
    HEAD(Config.Baked.headPartEffects, Config.Baked.headPartEffectAmplifiers, Config.Baked.headPartEffectThresholds),
    ARMS(Config.Baked.armsPartEffects, Config.Baked.armsPartEffectAmplifiers, Config.Baked.armsPartEffectThresholds),
    BOTH_ARMS(Config.Baked.bothArmsPartEffects, Config.Baked.bothArmsPartEffectAmplifiers, Config.Baked.bothArmsPartEffectThresholds),
    CHEST(Config.Baked.chestPartEffects, Config.Baked.chestPartEffectAmplifiers, Config.Baked.chestPartEffectThresholds),
    LEGS(Config.Baked.legsPartEffects, Config.Baked.legsPartEffectAmplifiers, Config.Baked.legsPartEffectThresholds),
    BOTH_LEGS(Config.Baked.bothLegsPartEffects, Config.Baked.bothLegsPartEffectAmplifiers, Config.Baked.bothLegsPartEffectThresholds),
    FEET(Config.Baked.feetPartEffects, Config.Baked.feetPartEffectAmplifiers, Config.Baked.feetPartEffectThresholds),
    BOTH_FEET(Config.Baked.bothFeetPartEffects, Config.Baked.bothFeetPartEffectAmplifiers, Config.Baked.bothFeetPartEffectThresholds);

    public final List<String> effects;
    public final List<Integer> amplifiers;
    public final List<Float> thresholds;

    MalusBodyPartEnum(List<String> effects, List<Integer> amplifiers, List<Float> thresholds) {
        this.effects = effects;
        this.amplifiers = amplifiers;
        this.thresholds = thresholds;
    }
}
