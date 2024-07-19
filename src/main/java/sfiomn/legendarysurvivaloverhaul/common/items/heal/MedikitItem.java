package sfiomn.legendarysurvivaloverhaul.common.items.heal;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class MedikitItem extends BodyHealingItem {
    public MedikitItem(Properties properties) {
        super(properties);
    }

    @Override
    public void runSecondaryEffect(Player player, ItemStack stack) {
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 400, 2));
        super.runSecondaryEffect(player, stack);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return Config.Baked.medikitUseTime;
    }

    @Override
    public float getHealingCapacity() {
        return (float) Config.Baked.medikitHealingValue;
    }

    @Override
    public int getHealingTicks() {
        return Config.Baked.medikitHealingTime;
    }

    @Override
    public int getHealingCharges() {
        return 0;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltips, TooltipFlag isAdvanced) {
        if (Config.Baked.localizedBodyDamageEnabled) {
            tooltips.add(Component.translatable("tooltip.legendarysurvivaloverhaul.body_heal_item.whole_body"));
            tooltips.add(Component.translatable("tooltip.legendarysurvivaloverhaul.body_heal_item.healing_value", getHealingCapacity(), MathUtil.round(getHealingTicks() / 20.0f, 1)));
        }
        ItemStack potionEquivalent = PotionUtils.setCustomEffects(new ItemStack(ItemRegistry.MEDIKIT.get()), Collections.singletonList(new MobEffectInstance(MobEffects.REGENERATION, 400, 2)));
        PotionUtils.addPotionTooltip(potionEquivalent, tooltips, 1.0f);
        super.appendHoverText(stack, level, tooltips, isAdvanced);
    }
}
