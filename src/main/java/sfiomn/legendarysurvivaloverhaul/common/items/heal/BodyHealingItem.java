package sfiomn.legendarysurvivaloverhaul.common.items.heal;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.IBodyDamageCapability;
import sfiomn.legendarysurvivaloverhaul.client.screens.ClientHooks;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

public class BodyHealingItem extends Item {

    public BodyHealingItem(Properties properties) {
        super(properties);
    }

    public void runSecondaryEffect(Player player, ItemStack stack)
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
    public UseAnim getUseAnimation(ItemStack stack)
    {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack)
    {
        return 20;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);

        if(!Config.Baked.localizedBodyDamageEnabled)
        {
            // Don't restrict use item if localized Body Damage is disabled
            player.startUsingItem(hand);
            return InteractionResultHolder.success(stack);
        }

        IBodyDamageCapability capability = CapabilityUtil.getBodyDamageCapability(player);
        for (BodyPartEnum bodyPart: BodyPartEnum.values()) {
            // 0 healing charge = heal all body parts => only allow healing if one wounded is not yet healing
            if (capability.getBodyPartDamage(bodyPart) > 0 &&
                    (getHealingCharges() != 0 || capability.getRemainingHealingTicks(bodyPart) == 0)) {
                player.startUsingItem(hand);
                return InteractionResultHolder.success(stack);
            }
        }

        return InteractionResultHolder.fail(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity)
    {
        if(!(entity instanceof Player player))
            return stack;

        if (!Config.Baked.localizedBodyDamageEnabled) {
            runSecondaryEffect(player, stack);
            stack.shrink(1);
            return stack;
        }

        if (world.isClientSide && Minecraft.getInstance().screen == null && getHealingCharges() > 0) {
            ClientHooks.openBodyHealthScreen(player, entity.getUsedItemHand());
        } else if (getHealingCharges() == 0) {
            for (BodyPartEnum bodyPart: BodyPartEnum.values()) {
                BodyDamageUtil.applyHealingItem(player, bodyPart, (BodyHealingItem) stack.getItem());
            }
            runSecondaryEffect(player, stack);
            world.playSound(null, entity, SoundRegistry.HEAL_BODY_PART.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
            if (!((Player) entity).isCreative())
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
