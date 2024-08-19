package sfiomn.legendarysurvivaloverhaul.common.items.heal;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import sfiomn.legendarysurvivaloverhaul.config.Config;

import javax.annotation.Nullable;
import java.util.List;

import static sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry.PAINKILLER;

public class MorphineItem extends Item {

    public MorphineItem(Item.Properties p_i48487_1_) {
        super(p_i48487_1_);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack)
    {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack)
    {
        return 30;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);

        player.startUsingItem(hand);
        return InteractionResultHolder.success(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity)
    {
        if(level.isClientSide || !(entity instanceof Player player))
            return stack;

        player.addEffect(new MobEffectInstance(PAINKILLER.get(), 1800, 0, false, false, true));

        if (!player.isCreative())
            stack.shrink(1);

        return stack;
    }

    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        if (Config.Baked.localizedBodyDamageEnabled)
            tooltip.add(Component.translatable("tooltip.legendarysurvivaloverhaul.body_heal_item.morphine"));
        super.appendHoverText(stack, level, tooltip, isAdvanced);
    }
}
