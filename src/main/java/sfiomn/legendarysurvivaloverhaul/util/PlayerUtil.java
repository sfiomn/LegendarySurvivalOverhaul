package sfiomn.legendarysurvivaloverhaul.util;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;

import java.util.Objects;

public class PlayerUtil {
    public static float getDamageAfterMagicAbsorb(PlayerEntity player, DamageSource source, float damageValue) {
        if (source.isBypassMagic()) {
            return damageValue;
        } else {
            int k;
            if (player.hasEffect(Effects.DAMAGE_RESISTANCE) && source != DamageSource.OUT_OF_WORLD) {
                k = (Objects.requireNonNull(player.getEffect(Effects.DAMAGE_RESISTANCE)).getAmplifier() + 1) * 5;
                int j = 25 - k;
                float f = damageValue * (float) j;
                damageValue = Math.max(f / 25.0F, 0.0F);
            }

            if (damageValue <= 0.0F) {
                return 0.0F;
            } else {
                k = EnchantmentHelper.getDamageProtection(player.getArmorSlots(), source);
                if (k > 0) {
                    damageValue = CombatRules.getDamageAfterMagicAbsorb(damageValue, (float) k);
                }

                return damageValue;
            }
        }
    }
}
