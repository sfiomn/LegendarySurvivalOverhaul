package sfiomn.legendarysurvivaloverhaul.common.items.heal;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectUtils;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
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

        JsonConsumableHeal jsonConsumableHeal = null;

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
                        return ActionResult.success(stack);
                    }
                }
            } else if (jsonConsumableHeal.healingCharges > 0 && capability.isWounded()) {
                player.startUsingItem(hand);
                return ActionResult.success(stack);
            }
        }

        return ActionResult.fail(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entity)
    {
        if(!(entity instanceof PlayerEntity))
            return stack;

        PlayerEntity player = (PlayerEntity) entity;

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
                world.playSound(null, entity, SoundRegistry.HEAL_BODY_PART.get(), SoundCategory.PLAYERS, 1.0f, 1.0f);
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

    public static void addSecondaryEffectTooltip(List<ITextComponent> tooltips, EffectInstance effectInstance) {
        IFormattableTextComponent iformattabletextcomponent = new TranslationTextComponent(effectInstance.getDescriptionId());

        if (effectInstance.getAmplifier() > 1) {
            iformattabletextcomponent = new TranslationTextComponent("potion.withAmplifier", iformattabletextcomponent, new TranslationTextComponent("potion.potency." + effectInstance.getAmplifier()));
        }

        if (effectInstance.getDuration() > 20) {
            iformattabletextcomponent = new TranslationTextComponent("potion.withDuration", iformattabletextcomponent, EffectUtils.formatDuration(effectInstance, 1.0f));
        }

        tooltips.add(iformattabletextcomponent.withStyle(Style.EMPTY.withColor(TextFormatting.BLUE)));
    }
}
