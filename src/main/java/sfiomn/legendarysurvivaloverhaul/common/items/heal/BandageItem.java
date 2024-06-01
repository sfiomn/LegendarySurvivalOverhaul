package sfiomn.legendarysurvivaloverhaul.common.items.heal;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;

import javax.annotation.Nullable;
import java.util.List;

public class BandageItem extends BodyHealingItem {
    public BandageItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return Config.Baked.bandageUseTime;
    }

    @Override
    public float getHealingCapacity() {
        return (float) Config.Baked.bandageHealingValue;
    }

    @Override
    public int getHealingTicks() {
        return Config.Baked.bandageHealingTime;
    }

    @Override
    public int getHealingCharges() {
        return Config.Baked.bandageHealingCharges;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag isAdvanced) {
        tooltip.add(new TranslationTextComponent("tooltip.legendarysurvivaloverhaul.body_heal_item.body_part", getHealingCharges()));
        tooltip.add(new TranslationTextComponent("tooltip.legendarysurvivaloverhaul.body_heal_item.healing_value", getHealingCapacity(), MathUtil.round(getHealingTicks() / 20.0f, 1)));
        super.appendHoverText(stack, world, tooltip, isAdvanced);
    }
}
