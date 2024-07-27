package sfiomn.legendarysurvivaloverhaul.util.internal;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonConsumableThirst;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.api.thirst.IThirstCapability;
import sfiomn.legendarysurvivaloverhaul.api.thirst.IThirstUtil;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;
import sfiomn.legendarysurvivaloverhaul.registry.EffectRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import javax.annotation.Nullable;

import java.util.List;

import static net.minecraft.block.CauldronBlock.LEVEL;

public class ThirstUtilInternal implements IThirstUtil {

    public static final String HYDRATION_ENUM_TAG = "HydrationPurity";
    public static final String CAPACITY_TAG = "HydrationCapacity";

    @Override
    public void setThirstEnumTag(ItemStack stack, HydrationEnum hydrationEnum)
    {
        if (!stack.hasTag())
        {
            stack.setTag(new CompoundNBT());
        }

        final CompoundNBT compound = stack.getTag();

        if (compound != null) {
            compound.putString(HYDRATION_ENUM_TAG, hydrationEnum.getName());
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
                String hydrationEnumName = compound.getString(HYDRATION_ENUM_TAG);

                return HydrationEnum.getByName(hydrationEnumName);
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
    public void takeDrink(PlayerEntity player, int hydration, float saturation, float effectChance, String effectName)
    {
        if(!Config.Baked.thirstEnabled)
            return;

        IThirstCapability capability = CapabilityUtil.getThirstCapability(player);

        capability.addHydrationLevel(hydration);

        capability.addSaturationLevel(saturation);

        // Check for effect chance
        if(effectChance != 0.0f && !effectName.isEmpty() && player.level.random.nextFloat() < effectChance)
        {
            Effect mobEffect = ForgeRegistries.POTIONS.getValue(new ResourceLocation(effectName));
            if (mobEffect != null)
                player.addEffect(new EffectInstance(mobEffect,600, 0, false, true, true));
        }
    }

    @Override
    public void takeDrink(PlayerEntity player, int hydration, float saturation)
    {
        // Clean water
        takeDrink(player, hydration, saturation, 0.0f, "");
    }

    @Override
    public void takeDrink(PlayerEntity player, HydrationEnum type)
    {
        takeDrink(player, type.getHydration(), (float) type.getSaturation(), (float) type.getEffectChance(), type.getEffectName());
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

    public JsonConsumableThirst getThirstConfig(ResourceLocation itemRegistryName, ItemStack itemStack) {
        List<JsonConsumableThirst> jsonConsumableThirsts = null;
        JsonConsumableThirst defaultJct = null;
        if (itemRegistryName != null)
            jsonConsumableThirsts = JsonConfig.consumableThirst.get(itemRegistryName.toString());

        if (jsonConsumableThirsts != null)
            for (JsonConsumableThirst jct: jsonConsumableThirsts) {
                if (jct.matchesNbt(itemStack))
                    return jct;
                if (jct.isDefault())
                    defaultJct = jct;
            }

        return defaultJct;
    }
}
