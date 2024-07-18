package sfiomn.legendarysurvivaloverhaul.common.items.heal;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class MedikitItem extends BodyHealingItem {

    public final ItemStack potionEquivalent = PotionUtils.setCustomEffects(new PotionItem(new Properties()).getDefaultInstance(), Collections.singletonList(new EffectInstance(Effects.REGENERATION, 400, 2)));

    public MedikitItem(Properties properties) {
        super(properties);
    }

    @Override
    public void runSecondaryEffect(PlayerEntity player, ItemStack stack) {
        player.addEffect(new EffectInstance(Effects.REGENERATION, 400, 2));
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
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltips, ITooltipFlag isAdvanced) {
        if(Config.Baked.localizedBodyDamageEnabled) {
            tooltips.add(new TranslationTextComponent("tooltip.legendarysurvivaloverhaul.body_heal_item.whole_body"));
            tooltips.add(new TranslationTextComponent("tooltip.legendarysurvivaloverhaul.body_heal_item.healing_value",
                    getHealingCapacity(), MathUtil.round(getHealingTicks() / 20.0f, 1)));
        }
        PotionUtils.addPotionTooltip(potionEquivalent, tooltips, 1.0f);
        super.appendHoverText(stack, world, tooltips, isAdvanced);
    }
}
