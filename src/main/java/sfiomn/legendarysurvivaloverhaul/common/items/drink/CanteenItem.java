package sfiomn.legendarysurvivaloverhaul.common.items.drink;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonConsumableThirst;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

public class CanteenItem extends DrinkItem {

    public CanteenItem(Properties properties){
        super(properties.stacksTo(1));
    }

    public int getMaxCapacity() {
        return Config.Baked.canteenCapacity;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return canDrink(stack) ? 40 : 0;
    }

    public boolean canDrink(ItemStack stack){
        return ThirstUtil.getCapacityTag(stack) > 0 && ThirstUtil.getHydrationEnumTag(stack) != null;
    }

    public boolean canFill(ItemStack stack) {
        // Prevent filling if canteen contains other than normal water
        return Config.Baked.allowOverridePurifiedWater ?
                ThirstUtil.getCapacityTag(stack) < getMaxCapacity() :
                ThirstUtil.getCapacityTag(stack) < getMaxCapacity() && ThirstUtil.getHydrationEnumTag(stack) == HydrationEnum.NORMAL;
    }

    public void fill(ItemStack stack) {
        ThirstUtil.setCapacityTag(stack, ThirstUtil.getCapacityTag(stack) + 1);
        ThirstUtil.setHydrationEnumTag(stack, HydrationEnum.NORMAL);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        HydrationEnum hydrationEnum = ThirstUtil.traceWater(player);
        ItemStack itemstack = player.getItemInHand(hand);
        if (canFill(itemstack) && hydrationEnum == HydrationEnum.NORMAL) {
            world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            this.fill(itemstack);
            return ActionResult.success(itemstack);
        }
        if (canDrink(itemstack) && !CapabilityUtil.getThirstCapability(player).isHydrationLevelAtMax()) {
            player.startUsingItem(hand);
            return ActionResult.success(itemstack);
        }
        return ActionResult.fail(itemstack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entity) {
        if (entity instanceof PlayerEntity && canDrink(stack) && !world.isClientSide) {
            PlayerEntity player = (PlayerEntity) entity;
            ThirstUtil.setCapacityTag(stack, ThirstUtil.getCapacityTag(stack) - 1);

            JsonConsumableThirst jsonConsumableThirst = null;
            // Check if the JSON has overridden the drink's defaults, and if so, allow ThirstHandler to take over
            ResourceLocation registryName = stack.getItem().getRegistryName();
            if (registryName != null)
                jsonConsumableThirst = ThirstUtil.getThirstJsonConfig(registryName, stack);

            if (jsonConsumableThirst != null)
                ThirstUtil.takeDrink(player, jsonConsumableThirst.hydration, jsonConsumableThirst.saturation, jsonConsumableThirst.effects);
            else {
                HydrationEnum hydrationEnum = ThirstUtil.getHydrationEnumTag(stack);
                if (hydrationEnum != null)
                    ThirstUtil.takeDrink(player, hydrationEnum);
            }

            runSecondaryEffect(player, stack);
        }
        return stack;
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        if(ThirstUtil.getCapacityTag(stack) == 0)
            return "item." + LegendarySurvivalOverhaul.MOD_ID + ".canteen_empty";

        if (ThirstUtil.getHydrationEnumTag(stack) == HydrationEnum.PURIFIED)
            return "item." + LegendarySurvivalOverhaul.MOD_ID + ".canteen_purified";
        else
            return "item." + LegendarySurvivalOverhaul.MOD_ID + ".canteen";
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack)
    {
        return ThirstUtil.getCapacityTag(stack) > 0;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
        double max = getMaxCapacity();
        if(max == 0.0d)
            return 1.0d;


        return (max - (double)ThirstUtil.getCapacityTag(stack)) / max;
    }
}
