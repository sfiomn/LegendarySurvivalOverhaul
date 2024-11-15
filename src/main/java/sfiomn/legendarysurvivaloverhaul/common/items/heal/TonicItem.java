package sfiomn.legendarysurvivaloverhaul.common.items.heal;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import sfiomn.legendarysurvivaloverhaul.config.Config;

import javax.annotation.Nullable;
import java.util.List;

public class TonicItem extends BodyHealingItem {

    public TonicItem(Properties properties) {
        super(properties);
    }

    @Override
    public void runSecondaryEffect(PlayerEntity player, ItemStack stack) {
        player.addEffect(new EffectInstance(Effects.REGENERATION, 400, 1));
        super.runSecondaryEffect(player, stack);
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
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltips, ITooltipFlag isAdvanced) {
        super.appendHoverText(stack, world, tooltips, isAdvanced);
        addSecondaryEffectTooltip(tooltips, new EffectInstance(Effects.REGENERATION, 400, 1));
    }
}
