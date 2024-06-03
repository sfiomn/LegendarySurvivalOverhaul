package sfiomn.legendarysurvivaloverhaul.common.items.heal;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.IBodyDamageCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;

import javax.annotation.Nullable;
import java.util.List;

public class TonicItem extends BodyHealingItem {
    public TonicItem(Properties properties) {
        super(properties);
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return Config.Baked.tonicUseTime;
    }

    @Override
    public float getHealingCapacity() {
        return (float) Config.Baked.tonicHealingValue;
    }

    @Override
    public int getHealingTicks() {
        return Config.Baked.tonicHealingTime;
    }

    @Override
    public int getHealingCharges() {
        return 0;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag isAdvanced) {
        tooltip.add(new TranslationTextComponent("tooltip.legendarysurvivaloverhaul.body_heal_item.whole_body"));
        tooltip.add(new TranslationTextComponent("tooltip.legendarysurvivaloverhaul.body_heal_item.healing_value", getHealingCapacity(), MathUtil.round(getHealingTicks() / 20.0f, 1)));
        super.appendHoverText(stack, world, tooltip, isAdvanced);
    }
}
