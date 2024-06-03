package sfiomn.legendarysurvivaloverhaul.common.items.heal;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.IBodyDamageCapability;
import sfiomn.legendarysurvivaloverhaul.client.screens.ClientHooks;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;

import javax.annotation.Nullable;
import java.util.List;

public class BodyHealingItem extends Item {

    public BodyHealingItem(Properties properties) {
        super(properties);
    }

    public void runSecondaryEffect(PlayerEntity player, ItemStack stack)
    {
        //Can be overridden to run a special task
    }

    public float getHealingCapacity()
    {
        return 2.0f;
    }

    public int getHealingTicks()
    {
        return 40;
    }

    public int getHealingCharges()
    {
        return 1;
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack)
    {
        return UseAction.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack)
    {
        return 20;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
    {
        ItemStack stack = player.getItemInHand(hand);

        if(!Config.Baked.localizedBodyDamageEnabled)
        {
            // Don't restrict use item if localized Body Damage is disabled
            player.startUsingItem(hand);
            return ActionResult.success(stack);
        }

        IBodyDamageCapability capability = CapabilityUtil.getBodyDamageCapability(player);
        for (BodyPartEnum bodyPart: BodyPartEnum.values()) {
            // 0 healing charge = heal all body parts => only allow healing if one wounded is not yet healing
            if (capability.getBodyPartDamage(bodyPart) > 0 &&
                    (getHealingCharges() != 0 || capability.getRemainingHealingTicks(bodyPart) == 0)) {
                player.startUsingItem(hand);
                return ActionResult.success(stack);
            }
        }

        return ActionResult.fail(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entity)
    {
        if(!(entity instanceof PlayerEntity) || !Config.Baked.localizedBodyDamageEnabled)
            return stack;

        if (world.isClientSide && Minecraft.getInstance().screen == null && getHealingCharges() > 0) {
            ClientHooks.openBodyHealthScreen((PlayerEntity) entity, entity.getUsedItemHand());
        } else if (getHealingCharges() == 0) {
            for (BodyPartEnum bodyPart: BodyPartEnum.values()) {
                BodyDamageUtil.applyHealingItem((PlayerEntity) entity, bodyPart, (BodyHealingItem) stack.getItem());
            }
            world.playSound(null, entity, SoundRegistry.HEAL_BODY_PART.get(), SoundCategory.PLAYERS, 1.0f, 1.0f);
            if (!((PlayerEntity) entity).isCreative())
                stack.shrink(1);
        }

        return stack;
    }

    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        return false;
    }
}
