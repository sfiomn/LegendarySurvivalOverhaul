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

public class PlasterItem extends BodyHealingItem {
    public PlasterItem(Properties properties) {
        super(properties);
    }

    @Override
    public void runSecondaryEffect(Player player, ItemStack stack) {
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 400, 0));
        super.runSecondaryEffect(player, stack);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return Config.Baked.plasterUseTime;
    }

    @Override
    public float getHealingCapacity() {
        return (float) Config.Baked.plasterHealingValue;
    }

    @Override
    public int getHealingTicks() {
        return Config.Baked.plasterHealingTime;
    }

    @Override
    public int getHealingCharges() {
        return Config.Baked.plasterHealingCharges;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltips, TooltipFlag isAdvanced) {
        if (Config.Baked.localizedBodyDamageEnabled) {
            tooltips.add(Component.translatable("tooltip.legendarysurvivaloverhaul.body_heal_item.body_part", getHealingCharges()));
            tooltips.add(Component.translatable("tooltip.legendarysurvivaloverhaul.body_heal_item.healing_value", getHealingCapacity(), MathUtil.round(getHealingTicks() / 20.0f, 1)));
        }
        addSecondaryEffectTooltip(tooltips, new MobEffectInstance(MobEffects.REGENERATION, 400, 0));
        super.appendHoverText(stack, level, tooltips, isAdvanced);
    }
}
