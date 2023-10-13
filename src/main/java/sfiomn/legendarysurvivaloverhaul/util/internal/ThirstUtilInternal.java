package sfiomn.legendarysurvivaloverhaul.util.internal;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.ForgeMod;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.api.thirst.IThirstCapability;
import sfiomn.legendarysurvivaloverhaul.api.thirst.IThirstUtil;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.EffectRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import javax.annotation.Nullable;

import static net.minecraft.block.CauldronBlock.LEVEL;

public class ThirstUtilInternal implements IThirstUtil {

    private final String HYDRATION_ENUM_TAG = "HydrationEnum";
    private final String CAPACITY_TAG = "HydrationCapacity";

    @Override
    public void setThirstEnumTag(ItemStack stack, HydrationEnum hydrationEnum)
    {
        if (!stack.hasTag())
        {
            stack.setTag(new CompoundNBT());
        }

        final CompoundNBT compound = stack.getTag();

        if (compound != null) {
            compound.putInt(HYDRATION_ENUM_TAG, hydrationEnum.ordinal());
        }
    }

    @Override
    public HydrationEnum getHydrationEnumTag(ItemStack stack)
    {
        if (stack.hasTag())
        {
            final CompoundNBT compound = stack.getTag();

            if (compound != null && compound.contains(HYDRATION_ENUM_TAG))
            {
                int hydrationEnumOrdinal = compound.getInt(HYDRATION_ENUM_TAG);

                return HydrationEnum.values()[hydrationEnumOrdinal];
            }
        }
        return null;
    }

    @Override
    public void removeHydrationEnumTag(ItemStack stack)
    {
        if(stack.hasTag())
        {
            final CompoundNBT compound = stack.getTag();
            if (compound != null && compound.contains(HYDRATION_ENUM_TAG))
            {
                compound.remove(HYDRATION_ENUM_TAG);
            }
        }
    }

    @Override
    public void setCapacityTag(ItemStack stack, int capacity)
    {
        if (!stack.hasTag())
        {
            stack.setTag(new CompoundNBT());
        }

        final CompoundNBT compound = stack.getTag();

        if (compound != null) {
            compound.putInt(CAPACITY_TAG, capacity);
        }
    }

    @Override
    public int getCapacityTag(ItemStack stack)
    {
        if (stack.hasTag())
        {
            final CompoundNBT compound = stack.getTag();

            if (compound != null && compound.contains(CAPACITY_TAG))
            {
                return compound.getInt(CAPACITY_TAG);
            }
        }
        return 0;
    }

    @Override
    public void removeCapacityTag(ItemStack stack)
    {
        if(stack.hasTag())
        {
            final CompoundNBT compound = stack.getTag();
            if (compound != null && compound.contains(CAPACITY_TAG))
            {
                compound.remove(CAPACITY_TAG);
            }
        }
    }

    // API
    // Returns a HydrationEnum based on what is being looked at
    @Nullable
    @Override
    public HydrationEnum traceWater(PlayerEntity player)
    {
        HydrationEnum hydrationEnum = getHydrationEnumLookedAt(player, player.getAttributeValue(ForgeMod.REACH_DISTANCE.get()) / 2);

        if(hydrationEnum == HydrationEnum.RAIN && !Config.Baked.drinkFromRain)
        {
            return null;
        }
        else if(hydrationEnum == HydrationEnum.NORMAL && !Config.Baked.drinkFromWater)
        {
            return null;
        }

        return hydrationEnum;
    }

    @Override
    public void takeDrink(PlayerEntity player, int hydration, float saturation, float dirtyChance)
    {
        if(!Config.Baked.thirstEnabled)
            return;

        IThirstCapability capability = CapabilityUtil.getThirstCapability(player);

        capability.addHydrationLevel(hydration);

        capability.addSaturationLevel(saturation);

        // Check for dirtiness
        if(dirtyChance != 0.0f && player.level.random.nextFloat() < dirtyChance)
        {
            player.addEffect(new EffectInstance(EffectRegistry.THIRST.get(),600, 0, false, true));
        }
    }

    @Override
    public void takeDrink(PlayerEntity player, int hydration, float saturation)
    {
        // Clean water
        takeDrink(player, hydration, saturation, 0.0f);
    }

    @Override
    public void takeDrink(PlayerEntity player, HydrationEnum type)
    {
        takeDrink(player, type.getHydration(), type.getSaturation(), type.getDirtiness());
    }

    @Override
    public void addExhaustion(PlayerEntity player, float exhaustion) {
        ThirstCapability thirstCap = CapabilityUtil.getThirstCapability(player);
        thirstCap.addThirstExhaustion(exhaustion);
    }

    @Override
    public HydrationEnum getHydrationEnumLookedAt(PlayerEntity player, double finalDistance) {

        // Check if player is looking up, if it's raining, if they can see sky, and if drinkFromRain is enabled
        if(player.getViewXRot(1.0f) < -60.0f && player.level.isRainingAt(new BlockPos(player.position()).above()) && Config.Baked.drinkFromRain)
        {
            //Drinking rain
            return HydrationEnum.RAIN;
        }

        RayTraceResult positionLookedAt = player.pick(finalDistance, 0.0F, true);

        if (positionLookedAt.getType() == RayTraceResult.Type.BLOCK) {
            BlockState blockState = player.level.getBlockState(((BlockRayTraceResult) positionLookedAt).getBlockPos());
            Fluid fluidState = player.level.getFluidState(((BlockRayTraceResult) positionLookedAt).getBlockPos()).getType();
            if (blockState.getBlock() == Blocks.CAULDRON) {
                int level = blockState.getValue(LEVEL);
                if(level > 0) {
                    return HydrationEnum.NORMAL;
                }
            } else {
                if (fluidState == Fluids.WATER || fluidState == Fluids.FLOWING_WATER) {
                    return HydrationEnum.NORMAL;
                }
            }
        }
        return null;
    }
}
