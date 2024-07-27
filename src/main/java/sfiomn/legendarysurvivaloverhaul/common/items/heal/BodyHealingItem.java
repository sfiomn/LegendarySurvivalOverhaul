package sfiomn.legendarysurvivaloverhaul.common.items.heal;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.IBodyDamageCapability;
import sfiomn.legendarysurvivaloverhaul.api.config.json.bodydamage.JsonConsumableHeal;
import sfiomn.legendarysurvivaloverhaul.client.screens.ClientHooks;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import java.util.List;

public class BodyHealingItem extends Item {

    public BodyHealingItem(Properties properties) {
        super(properties);
    }

    public void runSecondaryEffect(Player player, ItemStack stack)
    {
        //Can be overridden to run a special task
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

        JsonConsumableHeal  jsonConsumableHeal = null;

        ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(this);
        if (registryName != null)
            jsonConsumableHeal = JsonConfig.consumableHeal.get(registryName.toString());

        if(jsonConsumableHeal != null) {
            IBodyDamageCapability capability = CapabilityUtil.getBodyDamageCapability(player);
            // 0 healing charge = heal all body parts => only allow healing if one wounded limb is not yet healing
            if (jsonConsumableHeal.healingCharges == 0) {
                for (BodyPartEnum bodyPart : BodyPartEnum.values()) {
                    if (capability.getBodyPartDamage(bodyPart) > 0 && capability.getRemainingHealingTicks(bodyPart) == 0) {
                        player.startUsingItem(hand);
                        return InteractionResultHolder.success(stack);
                    }
                }
            } else if (jsonConsumableHeal.healingCharges > 0 && capability.isWounded()) {
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

        JsonConsumableHeal  jsonConsumableHeal = null;

        ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(this);
        if (registryName != null)
            jsonConsumableHeal = JsonConfig.consumableHeal.get(registryName.toString());

        if (jsonConsumableHeal != null) {
            if (jsonConsumableHeal.healingCharges > 0) {
                if (world.isClientSide && Minecraft.getInstance().screen == null)
                    ClientHooks.openBodyHealthScreen(player, entity.getUsedItemHand(), false,
                            jsonConsumableHeal.healingCharges, jsonConsumableHeal.healingValue, jsonConsumableHeal.healingTime);
            } else if (jsonConsumableHeal.healingCharges == 0) {
                for (BodyPartEnum bodyPart : BodyPartEnum.values()) {
                    BodyDamageUtil.applyHealingTimeBodyPart(player, bodyPart, jsonConsumableHeal.healingValue, jsonConsumableHeal.healingTime);
                }
                runSecondaryEffect(player, stack);
                world.playSound(null, entity, SoundRegistry.HEAL_BODY_PART.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
                if (!player.isCreative())
                    stack.shrink(1);
            }
        }

        return stack;
    }

    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        return false;
    }

    public static void addSecondaryEffectTooltip(List<Component> tooltips, MobEffectInstance mobEffect) {
        MutableComponent mutableComponent = Component.translatable(mobEffect.getDescriptionId());

        if (mobEffect.getAmplifier() > 0) {
            mutableComponent = Component.translatable("potion.withAmplifier", mutableComponent, Component.translatable("potion.potency." + mobEffect.getAmplifier()));
        }

        if (mobEffect.getDuration() > 20) {
            mutableComponent = Component.translatable("potion.withDuration", mutableComponent, MobEffectUtil.formatDuration(mobEffect, 1.0f));
        }

        tooltips.add(mutableComponent.withStyle(Style.EMPTY.withColor(ChatFormatting.BLUE)));
    }
}
