package sfiomn.legendarysurvivaloverhaul.common.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

public class CanteenItem extends DrinkItem {

    public CanteenItem(Properties properties){
        super(properties);
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
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity player) {
        if (player instanceof PlayerEntity && canDrink(stack) && !world.isClientSide && Config.Baked.thirstEnabled) {
            ThirstUtil.takeDrink((PlayerEntity) player, ThirstUtil.getHydrationEnumTag(stack));
            ThirstUtil.setCapacityTag(stack, ThirstUtil.getCapacityTag(stack) - 1);
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
