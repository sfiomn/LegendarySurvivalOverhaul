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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonMobEffect;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstBlock;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstConsumable;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.ThirstDataManager;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.api.thirst.IThirstCapability;
import sfiomn.legendarysurvivaloverhaul.api.thirst.IThirstUtil;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.common.integration.curios.CuriosUtil;
import sfiomn.legendarysurvivaloverhaul.common.integration.origins.OriginsUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import java.util.*;

public class ThirstUtilInternal implements IThirstUtil {

    public static final String HYDRATION_ENUM_TAG = LegendarySurvivalOverhaul.MOD_ID + ":HydrationPurity";
    public static final String CAPACITY_TAG = LegendarySurvivalOverhaul.MOD_ID + ":HydrationCapacity";

    @Override
    public void setHydrationEnumTag(ItemStack stack, HydrationEnum hydrationEnum)
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
    public void takeDrink(Player player, ItemStack itemStack) {
        if(!Config.Baked.thirstEnabled || !ThirstUtil.isThirstActive(player))
            return;

        JsonThirstConsumable jsonThirstConsumable = ThirstDataManager.getConsumable(itemStack);

        if (jsonThirstConsumable != null) {
            ThirstUtil.takeDrink(player, jsonThirstConsumable.hydration, jsonThirstConsumable.saturation, jsonThirstConsumable.effects);
        }
    }

    @Override
    public void takeDrink(Player player, int hydration, float saturation, List<JsonMobEffect> effects) {
        if(!Config.Baked.thirstEnabled || !ThirstUtil.isThirstActive(player))
            return;

        IThirstCapability capability = CapabilityUtil.getThirstCapability(player);

        if (!capability.isHydrationLevelAtMax()) {
            capability.addHydrationLevel(hydration);

            capability.addSaturationLevel(saturation);
        }

        // Check for effect chance
        for (JsonMobEffect effect: effects) {
            if (effect.chance >= 0.0f && effect.duration > 0 && !effect.name.isEmpty() && player.level().random.nextFloat() < effect.chance) {
                MobEffect mobEffect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(effect.name));
                if (mobEffect != null) {
                    int effectDuration = effect.duration;
                    if (Config.Baked.cumulativeThirstEffectDuration &&
                            mobEffect == MobEffectRegistry.THIRST.get() &&
                            player.getEffect(MobEffectRegistry.THIRST.get()) != null) {
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
    public JsonThirstBlock getFluidThirstLookedAt(Player player, double finalDistance) {
        ResourceLocation rain = new ResourceLocation("rain");

        // Check if player is looking up, if it's raining, if they can see sky, and if drinkFromRain is enabled
        if(player.getViewXRot(1.0f) < -60.0f && player.level().isRainingAt(player.blockPosition().above()) &&
                ThirstDataManager.getBlock(rain) != null) {
            //Drinking rain
            List<JsonThirstBlock> thirstPropertyList = ThirstDataManager.getBlock(rain);

            if (thirstPropertyList == null || thirstPropertyList.isEmpty()) {
                return null;
            }

            return thirstPropertyList.get(0);
        }

        HitResult positionLookedAt = player.pick(finalDistance, 0.0F, true);

        if (positionLookedAt.getType() == HitResult.Type.BLOCK) {

            FluidState fluidState = player.level().getFluidState(((BlockHitResult) positionLookedAt).getBlockPos());
            ResourceLocation fluidRegistryName = ForgeRegistries.FLUIDS.getKey(fluidState.getType());
            JsonThirstBlock defaultThirst = null;

            if (fluidRegistryName != null && !fluidState.isEmpty()) {

                if (LegendarySurvivalOverhaul.curiosLoaded) {
                    if (CuriosUtil.isCurioItemEquipped(player, ItemRegistry.NETHER_CHALICE.get()) &&
                            (fluidState.is(Fluids.FLOWING_LAVA) || fluidState.is(Fluids.LAVA)))
                        return new JsonThirstBlock(Config.Baked.hydrationLava, (float) Config.Baked.saturationLava, new ArrayList<>(), new HashMap<>());
                }

                if (LegendarySurvivalOverhaul.originsLoaded) {
                    if (OriginsUtil.canDrinkLava(player) &&
                            (fluidState.is(Fluids.FLOWING_LAVA) || fluidState.is(Fluids.LAVA)))
                        return new JsonThirstBlock(Config.Baked.hydrationLavaBlazeborn, (float) Config.Baked.saturationLavaBlazeborn, new ArrayList<>(), new HashMap<>());
                }

                List<JsonThirstBlock> jsonBlockFluidThirsts = ThirstDataManager.getBlock(fluidRegistryName);

                if (jsonBlockFluidThirsts == null)
                    return null;

                for (JsonThirstBlock thirstInfo : jsonBlockFluidThirsts) {
                    if (thirstInfo == null)
                        continue;

                    if (thirstInfo.isDefault())
                        defaultThirst = thirstInfo;

                    if (thirstInfo.matchesState(fluidState)) {
                        return thirstInfo;
                    }
                }
                return defaultThirst;

            }
        }
        return null;
    }

    @Override
    public JsonThirstBlock getBlockThirstLookedAt(Player player, double finalDistance) {

        HitResult positionLookedAt = player.pick(finalDistance, 0.0F, true);

        if (positionLookedAt.getType() == HitResult.Type.BLOCK) {

            JsonThirstBlock defaultThirst = null;

            BlockState blockState = player.level().getBlockState(((BlockHitResult) positionLookedAt).getBlockPos());
            ResourceLocation blockRegistryName = ForgeRegistries.BLOCKS.getKey(blockState.getBlock());

            if (blockRegistryName != null) {
                List<JsonThirstBlock> jsonBlockFluidThirsts = ThirstDataManager.getBlock(blockRegistryName);

                if (jsonBlockFluidThirsts == null)
                    return null;

                for (JsonThirstBlock thirstInfo : jsonBlockFluidThirsts) {
                    if (thirstInfo == null)
                        continue;

                    if (thirstInfo.isDefault())
                        defaultThirst = thirstInfo;

                    if (thirstInfo.matchesState(blockState)) {
                        return thirstInfo;
                    }
                }

                return defaultThirst;
            }
        }
        return null;
    }

    @Override
    public void deactivateThirst(Player player) {
        ThirstCapability cap = CapabilityUtil.getThirstCapability(player);
        cap.setTickTimer(-1);
        cap.setDirty();
    }

    @Override
    public void activateThirst(Player player) {
        ThirstCapability cap = CapabilityUtil.getThirstCapability(player);
        if (cap.getTickTimer() == -1) {
            cap.setTickTimer(0);
            cap.setDirty();
        }
    }

    @Override
    public boolean isThirstActive(Player player) {
        return CapabilityUtil.getThirstCapability(player).getTickTimer() != -1;
    }
}
