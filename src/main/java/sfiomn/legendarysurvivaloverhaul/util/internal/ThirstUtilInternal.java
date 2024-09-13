package sfiomn.legendarysurvivaloverhaul.util.internal;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonBlockFluidThirst;
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonConsumableThirst;
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonEffectParameter;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.api.thirst.IThirstCapability;
import sfiomn.legendarysurvivaloverhaul.api.thirst.IThirstUtil;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.common.integration.curios.CuriosUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;
import sfiomn.legendarysurvivaloverhaul.registry.EffectRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ThirstUtilInternal implements IThirstUtil {

    public static final String HYDRATION_ENUM_TAG = "HydrationPurity";
    public static final String CAPACITY_TAG = "HydrationCapacity";

    @Override
    public void setThirstEnumTag(ItemStack stack, HydrationEnum hydrationEnum) {
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
    public HydrationEnum getHydrationEnumTag(ItemStack stack) {
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
    public void removeHydrationEnumTag(ItemStack stack) {
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
    public void setCapacityTag(ItemStack stack, int capacity) {
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
    public int getCapacityTag(ItemStack stack) {
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
    public void removeCapacityTag(ItemStack stack) {
        if(stack.hasTag())
        {
            final CompoundNBT compound = stack.getTag();
            if (compound != null && compound.contains(CAPACITY_TAG))
            {
                compound.remove(CAPACITY_TAG);
            }
        }
    }

    @Override
    public void takeDrink(PlayerEntity player, int hydration, float saturation, List<JsonEffectParameter> effects) {
        if(!Config.Baked.thirstEnabled)
            return;

        IThirstCapability capability = CapabilityUtil.getThirstCapability(player);

        capability.addHydrationLevel(hydration);

        capability.addSaturationLevel(saturation);

        // Check for effect chance
        for (JsonEffectParameter effect: effects) {
            if(effect.chance >= 0.0f && effect.duration > 0 && !effect.name.isEmpty() && player.level.random.nextFloat() < effect.chance) {
                Effect mobEffect = ForgeRegistries.POTIONS.getValue(new ResourceLocation(effect.name));
                if (mobEffect != null) {
                    int effectDuration = effect.duration;
                    if (mobEffect == EffectRegistry.THIRST.get() && player.getEffect(EffectRegistry.THIRST.get()) != null) {
                        effectDuration += Objects.requireNonNull(player.getEffect(EffectRegistry.THIRST.get())).getDuration();
                    }
                    player.addEffect(new EffectInstance(mobEffect, effectDuration, effect.amplifier, false, true, true));
                }
            }
        }
    }

    @Override
    public void takeDrink(PlayerEntity player, int hydration, float saturation) {
        // Clean water
        takeDrink(player, hydration, saturation, Collections.emptyList());
    }

    @Override
    public void addExhaustion(PlayerEntity player, float exhaustion) {
        ThirstCapability thirstCap = CapabilityUtil.getThirstCapability(player);
        thirstCap.addThirstExhaustion(exhaustion);
    }

    @Override
    public JsonBlockFluidThirst getJsonBlockFluidThirstLookedAt(PlayerEntity player, double finalDistance) {

        // Check if player is looking up, if it's raining, if they can see sky, and if drinkFromRain is enabled
        if(player.getViewXRot(1.0f) < -60.0f && player.level.isRainingAt(player.blockPosition().above()) &&
                JsonConfig.blockFluidThirst.containsKey("minecraft:rain")) {
            //Drinking rain
            List<JsonBlockFluidThirst> thirstPropertyList = JsonConfig.blockFluidThirst.get("minecraft:rain");

            if (thirstPropertyList == null || thirstPropertyList.isEmpty()) {
                return null;
            }

            return JsonConfig.blockFluidThirst.get("minecraft:rain").get(0);
        }

        RayTraceResult positionLookedAt = player.pick(finalDistance, 0.0F, true);

        if (positionLookedAt.getType() == RayTraceResult.Type.BLOCK) {

            FluidState fluidState = player.level.getFluidState(((BlockRayTraceResult) positionLookedAt).getBlockPos());
            ResourceLocation fluidRegistryName = ForgeRegistries.FLUIDS.getKey(fluidState.getType());
            JsonBlockFluidThirst defaultJsonBlockFluidThirst = null;

            if (fluidRegistryName != null && !fluidState.isEmpty()) {

                if (LegendarySurvivalOverhaul.curiosLoaded) {
                    if (CuriosUtil.isCurioItemEquipped(player, ItemRegistry.NETHER_CHALICE.get()) && (fluidState.getType() == Fluids.FLOWING_LAVA || fluidState.getType() == Fluids.LAVA))
                        return new JsonBlockFluidThirst(Config.Baked.hydrationLava, (float) Config.Baked.saturationLava, new JsonEffectParameter[]{});
                }

                List<JsonBlockFluidThirst> jsonBlockFluidThirsts = JsonConfig.blockFluidThirst.get(fluidRegistryName.toString());

                if (jsonBlockFluidThirsts == null)
                    return null;

                for (JsonBlockFluidThirst thirstInfo : jsonBlockFluidThirsts) {
                    if (thirstInfo == null)
                        continue;

                    if (thirstInfo.isDefault())
                        defaultJsonBlockFluidThirst = thirstInfo;

                    if (thirstInfo.matchesState(fluidState)) {
                        return thirstInfo;
                    }
                }
                return defaultJsonBlockFluidThirst;

            } else {
                BlockState blockState = player.level.getBlockState(((BlockRayTraceResult) positionLookedAt).getBlockPos());
                ResourceLocation blockRegistryName = ForgeRegistries.BLOCKS.getKey(blockState.getBlock());

                if (blockRegistryName != null) {
                    List<JsonBlockFluidThirst> jsonBlockFluidThirsts = JsonConfig.blockFluidThirst.get(blockRegistryName.toString());

                    if (jsonBlockFluidThirsts == null)
                        return null;

                    for (JsonBlockFluidThirst thirstInfo : jsonBlockFluidThirsts) {
                        if (thirstInfo == null)
                            continue;

                        if (thirstInfo.isDefault())
                            defaultJsonBlockFluidThirst = thirstInfo;

                        if (thirstInfo.matchesState(blockState)) {
                            return thirstInfo;
                        }
                    }

                    return defaultJsonBlockFluidThirst;
                }
            }
        }
        return null;
    }

    @Override
    public JsonConsumableThirst getThirstJsonConfig(ResourceLocation itemRegistryName, ItemStack itemStack) {
        List<JsonConsumableThirst> jsonConsumableThirsts = null;
        JsonConsumableThirst defaultJct = null;
        if (itemRegistryName != null)
            jsonConsumableThirsts = JsonConfig.consumableThirst.get(itemRegistryName.toString());

        if (jsonConsumableThirsts != null) {
            for (JsonConsumableThirst jct : jsonConsumableThirsts) {
                if (jct.matchesNbt(itemStack))
                    return jct;
                if (jct.isDefault())
                    defaultJct = jct;
            }
        }

        return defaultJct;
    }

    @Override
    public void deactivateThirst(PlayerEntity player) {
        CapabilityUtil.getThirstCapability(player).setThirstTickTimer(-1);
        CapabilityUtil.getThirstCapability(player).setDirty();
    }

    @Override
    public void activateThirst(PlayerEntity player) {
        ThirstCapability cap = CapabilityUtil.getThirstCapability(player);
        if (cap.getThirstTickTimer() == -1) {
            cap.setThirstTickTimer(0);
            cap.setDirty();
        }
    }

    @Override
    public boolean isThirstActive(PlayerEntity player) {
        return CapabilityUtil.getThirstCapability(player).getThirstTickTimer() != -1;
    }
}
