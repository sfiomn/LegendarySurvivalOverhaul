package sfiomn.legendarysurvivaloverhaul.util.internal;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonConsumableThirst;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.api.thirst.IThirstCapability;
import sfiomn.legendarysurvivaloverhaul.api.thirst.IThirstUtil;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import java.util.List;
import java.util.Objects;

import static net.minecraft.world.level.block.LayeredCauldronBlock.LEVEL;

public class ThirstUtilInternal implements IThirstUtil {

    public static final String HYDRATION_ENUM_TAG = LegendarySurvivalOverhaul.MOD_ID + ":HydrationPurity";
    public static final String CAPACITY_TAG = LegendarySurvivalOverhaul.MOD_ID + ":HydrationCapacity";

    @Override
    public void setThirstEnumTag(ItemStack stack, HydrationEnum hydrationEnum)
    {
        if (!stack.hasTag())
        {
            stack.setTag(new CompoundTag());
        }

        final CompoundTag compound = stack.getTag();

        if (compound != null) {
            compound.putString(HYDRATION_ENUM_TAG, hydrationEnum.getName());
        }
    }

    @Override
    public HydrationEnum getHydrationEnumTag(ItemStack stack)
    {
        if (stack.hasTag())
        {
            final CompoundTag compound = stack.getTag();

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
            final CompoundTag compound = stack.getTag();
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
            stack.setTag(new CompoundTag());
        }

        final CompoundTag compound = stack.getTag();

        if (compound != null) {
            compound.putInt(CAPACITY_TAG, capacity);
        }
    }

    @Override
    public int getCapacityTag(ItemStack stack)
    {
        if (stack.hasTag())
        {
            final CompoundTag compound = stack.getTag();

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
            final CompoundTag compound = stack.getTag();
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
    public HydrationEnum traceWater(Player player)
    {
        HydrationEnum hydrationEnum = getHydrationEnumLookedAt(player, player.getAttributeValue(ForgeMod.BLOCK_REACH.get()) / 2);

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
    public void takeDrink(Player player, int hydration, float saturation, float effectChance, String effectName)
    {
        if(!Config.Baked.thirstEnabled)
            return;

        IThirstCapability capability = CapabilityUtil.getThirstCapability(player);

        capability.addHydrationLevel(hydration);

        capability.addSaturationLevel(saturation);

        // Check for effect chance
        if(effectChance != 0.0f && !effectName.isEmpty() && player.level().random.nextFloat() < effectChance)
        {
            MobEffect mobEffect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(effectName));
            if (mobEffect != null)
                player.addEffect(new MobEffectInstance(mobEffect,600, 0, false, true, true));
        }
    }

    @Override
    public void takeDrink(Player player, int hydration, float saturation)
    {
        // Clean water
        takeDrink(player, hydration, saturation, 0.0f, "");
    }

    @Override
    public void takeDrink(Player player, HydrationEnum type)
    {
        takeDrink(player, type.getHydration(), (float) type.getSaturation(), (float) type.getEffectChance(), type.getEffect());
    }

    @Override
    public void addExhaustion(Player player, float exhaustion) {
        ThirstCapability thirstCap = CapabilityUtil.getThirstCapability(player);
        thirstCap.addThirstExhaustion(exhaustion);
    }

    @Override
    public HydrationEnum getHydrationEnumLookedAt(Player player, double finalDistance) {

        // Check if player is looking up, if it's raining, if they can see sky, and if drinkFromRain is enabled
        if(player.getViewXRot(1.0f) < -60.0f && player.level().isRainingAt(player.blockPosition().above()) && Config.Baked.drinkFromRain)
        {
            //Drinking rain
            return HydrationEnum.RAIN;
        }

        HitResult positionLookedAt = player.pick(finalDistance, 0.0F, true);

        if (positionLookedAt.getType() == HitResult.Type.BLOCK) {
            BlockState blockState = player.level().getBlockState(((BlockHitResult) positionLookedAt).getBlockPos());
            Fluid fluidState = player.level().getFluidState(((BlockHitResult) positionLookedAt).getBlockPos()).getType();
            if (blockState.getBlock() == Blocks.WATER_CAULDRON) {
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
