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
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import sfiomn.legendarysurvivaloverhaul.api.thirst.IThirstCapability;
import sfiomn.legendarysurvivaloverhaul.api.thirst.IThirstUtil;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstEnum;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.EffectRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import javax.annotation.Nullable;

import static net.minecraft.block.CauldronBlock.LEVEL;

public class ThirstUtilInternal implements IThirstUtil {

    private final String THIRST_ENUM_TAG = "ThirstEnum";
    private final String CAPACITY_TAG = "ThirstCapacity";

    @Override
    public void setThirstEnumTag(ItemStack stack, ThirstEnum thirstEnum)
    {
        if (!stack.hasTag())
        {
            stack.setTag(new CompoundNBT());
        }

        final CompoundNBT compound = stack.getTag();

        if (compound != null) {
            compound.putInt(THIRST_ENUM_TAG, thirstEnum.ordinal());
        }
    }

    @Override
    public ThirstEnum getThirstEnumTag(ItemStack stack)
    {
        if (stack.hasTag())
        {
            final CompoundNBT compound = stack.getTag();

            if (compound != null && compound.contains(THIRST_ENUM_TAG))
            {
                int thirstEnumOrdinal = compound.getInt(THIRST_ENUM_TAG);

                return ThirstEnum.values()[thirstEnumOrdinal];
            }
        }
        return null;
    }

    @Override
    public void removeThirstEnumTag(ItemStack stack)
    {
        if(stack.hasTag())
        {
            final CompoundNBT compound = stack.getTag();
            if (compound != null && compound.contains(THIRST_ENUM_TAG))
            {
                compound.remove(THIRST_ENUM_TAG);
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
    // Returns a ThirstEnum based on what is being looked at
    @Nullable
    @Override
    public ThirstEnum traceWater(PlayerEntity player)
    {
        ThirstEnum thirstEnum = getThirstEnumLookedAt(player, player.getAttributeValue(ForgeMod.REACH_DISTANCE.get()) / 2);

        if(thirstEnum == ThirstEnum.RAIN && !Config.Baked.drinkFromRain)
        {
            return null;
        }
        else if(thirstEnum == ThirstEnum.NORMAL && !Config.Baked.drinkFromWater)
        {
            return null;
        }

        return thirstEnum;
    }

    @Override
    public void takeDrink(PlayerEntity player, int thirst, float saturation, float dirtyChance)
    {
        if(!Config.Baked.thirstEnabled)
            return;

        IThirstCapability capability = CapabilityUtil.getThirstCapability(player);

        capability.addThirstLevel(thirst);

        capability.addThirstSaturation(saturation);

        // Check for dirtiness
        if(dirtyChance != 0.0f && player.level.random.nextFloat() < dirtyChance)
        {
            player.addEffect(new EffectInstance(EffectRegistry.THIRSTY.get(),1200, 0, false, true));
        }
    }

    @Override
    public void takeDrink(PlayerEntity player, int thirst, float saturation)
    {
        // Clean water
        takeDrink(player, thirst, saturation, 0.0f);
    }

    @Override
    public void takeDrink(PlayerEntity player, ThirstEnum type)
    {
        takeDrink(player, type.getThirst(), type.getSaturation(), type.getDirtiness());
    }

    @Override
    public void addExhaustion(PlayerEntity player, float exhaustion) {
        ThirstCapability thirstCap = CapabilityUtil.getThirstCapability(player);
        thirstCap.addThirstExhaustion(exhaustion);
    }

    @Override
    public ItemStack createPurifiedWaterBucket()
    {
        return FluidUtil.getFilledBucket(new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME));
    }

    @Override
    public ThirstEnum getThirstEnumLookedAt(PlayerEntity player, double finalDistance) {

        //Check if player is looking up, if it's raining, if they can see sky, and if THIRST_DRINK_RAIN is enabled
        //This essentially means rain can't be a trace result for drinking or for a canteen
        if(player.getViewXRot(1.0f) < -60.0f && player.level.isRainingAt(new BlockPos(player.position())) && Config.Baked.drinkFromRain)
        {
            //Drinking rain
            return ThirstEnum.RAIN;
        }

        RayTraceResult positionLookedAt = player.pick(finalDistance, 0.0F, true);

        if (positionLookedAt.getType() == RayTraceResult.Type.BLOCK) {
            BlockState blockState = player.level.getBlockState(((BlockRayTraceResult) positionLookedAt).getBlockPos());
            Fluid fluidState = player.level.getFluidState(((BlockRayTraceResult) positionLookedAt).getBlockPos()).getType();
            if (blockState.getBlock() == Blocks.CAULDRON) {
                int level = blockState.getValue(LEVEL);
                if(level > 0) {
                    return ThirstEnum.NORMAL;
                }
            } else {
                if (fluidState == Fluids.WATER || fluidState == Fluids.FLOWING_WATER) {
                    return ThirstEnum.NORMAL;
                }
            }
        }
        return null;
    }
}
