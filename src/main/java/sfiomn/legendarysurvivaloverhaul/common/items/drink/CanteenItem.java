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
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
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
        HydrationEnum hydrationEnum = ThirstUtil.traceWater(player);
        ItemStack itemstack = player.getItemInHand(hand);
        if (canFill(itemstack) && hydrationEnum == HydrationEnum.NORMAL) {
            level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
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
                jsonConsumableThirst = ThirstUtil.getThirstConfig(registryName, stack);

            if (jsonConsumableThirst != null)
                ThirstUtil.takeDrink(player, jsonConsumableThirst.hydration, jsonConsumableThirst.saturation, jsonConsumableThirst.effectChance, jsonConsumableThirst.effect);
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
