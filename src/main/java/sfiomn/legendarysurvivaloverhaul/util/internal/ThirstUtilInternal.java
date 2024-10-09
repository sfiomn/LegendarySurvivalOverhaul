package sfiomn.legendarysurvivaloverhaul.util.internal;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
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
import sfiomn.legendarysurvivaloverhaul.common.integration.origins.OriginsUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    @Override
    public void takeDrink(Player player, int hydration, float saturation, List<JsonEffectParameter> effects)
    {
        if(!Config.Baked.thirstEnabled)
            return;

        IThirstCapability capability = CapabilityUtil.getThirstCapability(player);

        capability.addHydrationLevel(hydration);

        capability.addSaturationLevel(saturation);

        // Check for effect chance
        for (JsonEffectParameter effect: effects) {
            if (effect.chance >= 0.0f && effect.duration > 0 && !effect.name.isEmpty() && player.level().random.nextFloat() < effect.chance) {
                MobEffect mobEffect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(effect.name));
                if (mobEffect != null) {
                    int effectDuration = effect.duration;
                    if (mobEffect == MobEffectRegistry.THIRST.get() && player.getEffect(MobEffectRegistry.THIRST.get()) != null) {
                        effectDuration += Objects.requireNonNull(player.getEffect(MobEffectRegistry.THIRST.get())).getDuration();
                    }
                    player.addEffect(new MobEffectInstance(mobEffect, effectDuration, effect.amplifier, false, true, true));
                }
            }
        }
    }

    @Override
    public void takeDrink(Player player, int hydration, float saturation)
    {
        // Clean water
        takeDrink(player, hydration, saturation, Collections.emptyList());
    }

    @Override
    public void addExhaustion(Player player, float exhaustion) {
        ThirstCapability thirstCap = CapabilityUtil.getThirstCapability(player);
        thirstCap.addThirstExhaustion(exhaustion);
    }

    @Override
    public JsonBlockFluidThirst getJsonBlockFluidThirstLookedAt(Player player, double finalDistance) {

        // Check if player is looking up, if it's raining, if they can see sky, and if drinkFromRain is enabled
        if(player.getViewXRot(1.0f) < -60.0f && player.level().isRainingAt(player.blockPosition().above()) &&
                JsonConfig.blockFluidThirst.containsKey("minecraft:rain")) {
            //Drinking rain
            List<JsonBlockFluidThirst> thirstPropertyList = JsonConfig.blockFluidThirst.get("minecraft:rain");

            if (thirstPropertyList == null || thirstPropertyList.isEmpty()) {
                return null;
            }

            return JsonConfig.blockFluidThirst.get("minecraft:rain").get(0);
        }

        HitResult positionLookedAt = player.pick(finalDistance, 0.0F, true);

        if (positionLookedAt.getType() == HitResult.Type.BLOCK) {

            FluidState fluidState = player.level().getFluidState(((BlockHitResult) positionLookedAt).getBlockPos());
            ResourceLocation fluidRegistryName = ForgeRegistries.FLUIDS.getKey(fluidState.getType());
            JsonBlockFluidThirst defaultJsonBlockFluidThirst = null;

            if (fluidRegistryName != null && !fluidState.isEmpty()) {

                if (LegendarySurvivalOverhaul.curiosLoaded) {
                    if (CuriosUtil.isCurioItemEquipped(player, ItemRegistry.NETHER_CHALICE.get()) &&
                            (fluidState.is(Fluids.FLOWING_LAVA) || fluidState.is(Fluids.LAVA)))
                        return new JsonBlockFluidThirst(Config.Baked.hydrationLava, (float) Config.Baked.saturationLava, new JsonEffectParameter[]{});
                }

                if (LegendarySurvivalOverhaul.originsLoaded) {
                    if (OriginsUtil.isOrigin(player, OriginsUtil.BLAZEBORN) &&
                            (fluidState.is(Fluids.FLOWING_LAVA) || fluidState.is(Fluids.LAVA)))
                        return new JsonBlockFluidThirst(Config.Baked.hydrationLavaBlazeborn, (float) Config.Baked.saturationLavaBlazeborn, new JsonEffectParameter[]{});
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
                BlockState blockState = player.level().getBlockState(((BlockHitResult) positionLookedAt).getBlockPos());
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
    public JsonConsumableThirst getConsumableThirstJsonConfig(ResourceLocation itemRegistryName, ItemStack itemStack) {
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

    @Override
    public void deactivateThirst(Player player) {
        ThirstCapability cap = CapabilityUtil.getThirstCapability(player);
        cap.setThirstTickTimer(-1);
        cap.setDirty();
    }

    @Override
    public void activateThirst(Player player) {
        ThirstCapability cap = CapabilityUtil.getThirstCapability(player);
        if (cap.getThirstTickTimer() == -1) {
            cap.setThirstTickTimer(0);
            cap.setDirty();
        }
    }

    @Override
    public boolean isThirstActive(Player player) {
        return CapabilityUtil.getThirstCapability(player).getThirstTickTimer() != -1;
    }
}
