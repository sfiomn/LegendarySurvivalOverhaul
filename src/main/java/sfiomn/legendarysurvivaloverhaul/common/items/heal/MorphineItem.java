package sfiomn.legendarysurvivaloverhaul.common.items.heal;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;

import javax.annotation.Nullable;
import java.util.List;

import static sfiomn.legendarysurvivaloverhaul.registry.EffectRegistry.PAINKILLER;

public class MorphineItem extends Item {

    public MorphineItem(Properties p_i48487_1_) {
        super(p_i48487_1_);
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack)
    {
        return UseAction.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack)
    {
        return 30;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
    {
        ItemStack stack = player.getItemInHand(hand);

        player.startUsingItem(hand);
        return ActionResult.success(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entity)
    {
        if(world.isClientSide || !(entity instanceof PlayerEntity))
            return stack;

        PlayerEntity player = (PlayerEntity) entity;
        player.addEffect(new EffectInstance(PAINKILLER.get(), 3600, 0, false, false, true));

        return stack;
    }

    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag isAdvanced) {
        tooltip.add(new TranslationTextComponent("tooltip.legendarysurvivaloverhaul.body_heal_item.morphine"));
        super.appendHoverText(stack, world, tooltip, isAdvanced);
    }
}
