package sfiomn.legendarysurvivaloverhaul.common.items.drink;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonBlockFluidThirst;
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonConsumableThirst;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

public class CanteenItem extends DrinkItem {

    public CanteenItem(Item.Properties properties){
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
        ThirstUtil.setCapacityTag(stack,  Math.min(getMaxCapacity(), ThirstUtil.getCapacityTag(stack) + 1));
        ThirstUtil.setHydrationEnumTag(stack, HydrationEnum.NORMAL);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        HitResult positionLookedAt = player.pick(player.getAttributeValue(ForgeMod.BLOCK_REACH.get()) / 2, 0.0F, true);
        FluidState fluidState = null;
        BlockState blockState = null;
        if (positionLookedAt.getType() == HitResult.Type.BLOCK) {
            fluidState = level.getFluidState(((BlockHitResult) positionLookedAt).getBlockPos());
            blockState = level.getBlockState(((BlockHitResult) positionLookedAt).getBlockPos());
        }

        ItemStack itemstack = player.getItemInHand(hand);
        boolean isWater = false;
        if (fluidState != null && (fluidState.is(Fluids.FLOWING_WATER) || fluidState.is(Fluids.WATER)))
            isWater = true;
        else if (blockState != null && blockState.is(Blocks.WATER_CAULDRON))
            isWater = true;

        if (canFill(itemstack) && isWater) {
            player.swing(InteractionHand.MAIN_HAND);
            player.playSound(SoundEvents.BOTTLE_FILL, 1.0f, 1.0f);
            this.fill(itemstack);
            return InteractionResultHolder.success(itemstack);
        }
        if (canDrink(itemstack) && !CapabilityUtil.getThirstCapability(player).isHydrationLevelAtMax()) {
            player.startUsingItem(hand);
            return InteractionResultHolder.success(itemstack);
        }
        return InteractionResultHolder.fail(itemstack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, net.minecraft.world.level.Level world, LivingEntity entity) {
        if (entity instanceof Player player && canDrink(stack) && !world.isClientSide) {
            ThirstUtil.setCapacityTag(stack, ThirstUtil.getCapacityTag(stack) - 1);

            JsonConsumableThirst jsonConsumableThirst = null;
            // Check if the JSON has overridden the drink's defaults, and if so, allow ThirstHandler to take over
            ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(this);
            if (registryName != null)
                jsonConsumableThirst = ThirstUtil.getConsumableThirstJsonConfig(registryName, stack);

            if (jsonConsumableThirst != null)
                ThirstUtil.takeDrink(player, jsonConsumableThirst.hydration, jsonConsumableThirst.saturation, jsonConsumableThirst.effects);

            runSecondaryEffect(player, stack);
        }
        return stack;
    }

    @Override
    public @NotNull String getDescriptionId(ItemStack stack) {
        if(ThirstUtil.getCapacityTag(stack) == 0)
            return "item." + LegendarySurvivalOverhaul.MOD_ID + ".canteen_empty";

        if (ThirstUtil.getHydrationEnumTag(stack) == HydrationEnum.PURIFIED)
            return "item." + LegendarySurvivalOverhaul.MOD_ID + ".canteen_purified";
        else
            return "item." + LegendarySurvivalOverhaul.MOD_ID + ".canteen";
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return ThirstUtil.getCapacityTag(stack) > 0;
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack)
    {
        float max = getMaxCapacity();
        if(max == 0.0f)
            return 0;

        return Math.round(ThirstUtil.getCapacityTag(stack) / max * 13);
    }

    @Override
    public int getBarColor(@NotNull ItemStack stack) {
        float f = Math.max(0.0F, ThirstUtil.getCapacityTag(stack) / (float)this.getMaxCapacity());
        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }
}
