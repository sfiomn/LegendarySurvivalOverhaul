package sfiomn.legendarysurvivaloverhaul.common.items.heal;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import sfiomn.legendarysurvivaloverhaul.config.Config;

import javax.annotation.Nullable;
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
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltips, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltips, isAdvanced);
        addSecondaryEffectTooltip(tooltips, new MobEffectInstance(MobEffects.REGENERATION, 400, 0));
    }
}
