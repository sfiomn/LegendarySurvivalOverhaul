package sfiomn.legendarysurvivaloverhaul.common.events;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.DamageDistributionEnum;
import sfiomn.legendarysurvivaloverhaul.api.config.json.bodydamage.JsonBodyPartsDamageSource;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonConsumableTemperature;
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonThirst;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.common.items.drink.DrinkItem;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;
import sfiomn.legendarysurvivaloverhaul.network.packets.MessageDrinkWater;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;
import sfiomn.legendarysurvivaloverhaul.util.PlayerModelUtil;

import java.util.*;

@Mod.EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModCommonEvents {

    @SubscribeEvent
    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event)
    {
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof Player) || entity.level().isClientSide)
            return;

        Player player = (Player) entity;
        ResourceLocation itemRegistryName = ForgeRegistries.ITEMS.getKey(event.getItem().getItem());

        if (shouldApplyTemperature((Player) entity)) {
            List<JsonConsumableTemperature> jsonConsumableTemperatures = null;
            if (itemRegistryName != null)
                jsonConsumableTemperatures = JsonConfig.consumableTemperature.get(itemRegistryName.toString());

            if (jsonConsumableTemperatures != null) {
                for (JsonConsumableTemperature jct : jsonConsumableTemperatures) {
                    if (jct.getEffect() != null) {
                        player.addEffect(new MobEffectInstance(jct.getEffect(), jct.duration, (Math.abs(jct.temperatureLevel) - 1), false, false, true));
                        player.removeEffect(jct.getOppositeEffect());
                    }
                }
            }
        }

        if (shouldApplyThirst((Player) entity) && !(event.getItem().getItem() instanceof DrinkItem)) {
            JsonThirst jsonConsumableThirst = null;
            if (itemRegistryName != null)
                jsonConsumableThirst = JsonConfig.consumableThirst.get(itemRegistryName.toString());

            if (jsonConsumableThirst != null && (jsonConsumableThirst.hydration != 0 || jsonConsumableThirst.saturation != 0)) {
                ThirstUtil.takeDrink(player, jsonConsumableThirst.hydration, jsonConsumableThirst.saturation, jsonConsumableThirst.dirty);
            } else if (event.getItem().getItem() == Items.POTION){
                Potion potion = PotionUtils.getPotion(event.getItem());
                if(potion == Potions.WATER || potion == Potions.AWKWARD || potion == Potions.MUNDANE || potion == Potions.THICK)
                {
                    ThirstUtil.takeDrink(player, HydrationEnum.NORMAL);
                }
                else if (potion != Potions.EMPTY)
                {
                    ThirstUtil.takeDrink(player, HydrationEnum.POTION);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player && shouldApplyThirst((Player) entity) && !entity.level().isClientSide) {
            ThirstUtil.addExhaustion((Player) entity, (float) Config.Baked.onJumpThirstExhaustion);
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (shouldApplyThirst(player) && !player.level().isClientSide && event.getState().getBlock().canHarvestBlock(event.getState(), event.getLevel(), event.getPos(), player) && event.getState().getDestroySpeed(event.getLevel(), event.getPos()) > 0.0f) {
            ThirstUtil.addExhaustion(player, (float) Config.Baked.onBlockBreakThirstExhaustion);
        }
    }

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        if (shouldApplyThirst(player) && !player.level().isClientSide) {
            Entity monster = event.getTarget();
            if(monster.isAttackable()) {
                ThirstUtil.addExhaustion(player, (float) Config.Baked.onAttackThirstExhaustion);
            }
        }
    }

    // Only Client side event
    @SubscribeEvent
    public static void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        if (shouldApplyThirst(event.getEntity())) {
            // Only run on main hand (otherwise it runs twice)
            if(event.getHand() == InteractionHand.MAIN_HAND)
            {
                HydrationEnum water = playerGetHydrationEnum(event.getEntity());

                if (water != null) {
                    playerDrinkEffect(event.getEntity());
                    MessageDrinkWater messageDrinkToServer = new MessageDrinkWater();
                    NetworkHandler.INSTANCE.sendToServer(messageDrinkToServer);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (shouldApplyThirst(event.getEntity())) {
            // Only run on main hand (otherwise it runs twice)
            if(event.getHand()==InteractionHand.MAIN_HAND && event.getEntity().getMainHandItem().isEmpty())
            {
                HydrationEnum water = playerGetHydrationEnum(event.getEntity());

                if (water != null) {
                    if (event.getLevel().isClientSide)
                        playerDrinkEffect(event.getEntity());
                    else {
                        ThirstUtil.takeDrink(event.getEntity(), water);
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityHurt(LivingHurtEvent event) {
        if (!event.getSource().is(DamageTypes.FALL) && event.getEntity().hasEffect(MobEffectRegistry.VULNERABILITY.get()))
            event.setAmount(event.getAmount() * (1 + 0.2f * Objects.requireNonNull(event.getEntity().getEffect(MobEffectRegistry.VULNERABILITY.get())).getAmplifier() + 1));

        else if (event.getSource().is(DamageTypes.FALL) && event.getEntity().hasEffect(MobEffectRegistry.HARD_FALLING.get())) {
            event.setAmount(event.getAmount() * (1 + 0.2f * Objects.requireNonNull(event.getEntity().getEffect(MobEffectRegistry.HARD_FALLING.get())).getAmplifier() + 1));
            event.getEntity().level().playSound(null, event.getEntity(), SoundRegistry.HARD_FALLING_HURT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityHurtDamage(LivingDamageEvent event) {
        Player player;
        if (event.getEntity() instanceof Player)
            player = (Player) event.getEntity();
        else return;

        if (player.level().isClientSide || !shouldApplyLocalizedBodyDamage(player))
            return;

        float bodyPartDamageValue = event.getAmount() * (float) Config.Baked.bodyDamageMultiplier;
        DamageSource source = event.getSource();

        JsonBodyPartsDamageSource damageSourceBodyParts = JsonConfig.damageSourceBodyParts.get(source.getMsgId());
        List<BodyPartEnum> hitBodyParts = new ArrayList<>();
        if (damageSourceBodyParts != null)
            hitBodyParts.addAll(damageSourceBodyParts.getBodyParts(player));

        if (hitBodyParts.isEmpty()) {
             if (source.is(DamageTypeTags.IS_PROJECTILE) && source.getDirectEntity() != null) {
                 hitBodyParts.addAll(PlayerModelUtil.getPreciseEntityImpact(source.getDirectEntity(), player));

             } else if (source.getDirectEntity() != null) {
                 List<BodyPartEnum> possibleHitParts = PlayerModelUtil.getEntityImpact(source.getDirectEntity(), player);
                 if (!possibleHitParts.isEmpty()) {
                     hitBodyParts.addAll(DamageDistributionEnum.ONE_OF.getBodyParts(player, possibleHitParts));
                 }
            }
        }

        if (hitBodyParts.isEmpty()) {
            hitBodyParts.addAll(DamageDistributionEnum.ONE_OF.getBodyParts(player, Arrays.asList(BodyPartEnum.values())));
        }

        BodyDamageUtil.balancedHurtBodyParts(player, hitBodyParts, bodyPartDamageValue);

        if (source.is(DamageTypeTags.IS_PROJECTILE) && hitBodyParts.contains(BodyPartEnum.HEAD) && Config.Baked.headCriticalShotMultiplier > 1) {
            event.setAmount(event.getAmount() * (float) Config.Baked.headCriticalShotMultiplier);
            player.level().playSound(null, player, SoundRegistry.HEADSHOT.get(), SoundSource.HOSTILE, 1.0F, 1.0F);
        }
    }

    @SubscribeEvent
    public static void onSleepFinished(SleepFinishedTimeEvent event) {
        for (Player player : event.getLevel().players()) {
            if (player.isSleepingLongEnough()) {
                if (Config.Baked.localizedBodyDamageEnabled && Config.Baked.bodyHealthRatioRecoveredFromSleep > 0) {
                    for (BodyPartEnum bodyPart : BodyPartEnum.values()) {
                        double healthRecovered = BodyDamageUtil.getMaxHealth(player, bodyPart) * Config.Baked.bodyHealthRatioRecoveredFromSleep;
                        BodyDamageUtil.healBodyPart(player, bodyPart, (float) healthRecovered);
                    }
                }

                if (Config.Baked.healthRatioRecoveredFromSleep > 0) {
                    double healthRecovered = player.getMaxHealth() * Config.Baked.healthRatioRecoveredFromSleep;
                    player.heal((float) healthRecovered);
                }
            }
        }
    }


    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.isEndConquered())
            return;

        Player player = event.getEntity();
        if (Config.Baked.temperatureResistanceOnDeathEnabled) {
            player.addEffect(new MobEffectInstance(MobEffectRegistry.HEAT_RESISTANCE.get(), Config.Baked.temperatureResistanceOnDeathTime));
            player.addEffect(new MobEffectInstance(MobEffectRegistry.COLD_RESISTANCE.get(), Config.Baked.temperatureResistanceOnDeathTime));
        }
    }

    private static boolean shouldApplyThirst(Player player)
    {
        return !player.isCreative() && !player.isSpectator() && Config.Baked.thirstEnabled;
    }

    private static boolean shouldApplyTemperature(Player player)
    {
        return !player.isCreative() && !player.isSpectator() && Config.Baked.temperatureEnabled;
    }

    private static boolean shouldApplyLocalizedBodyDamage(Player player)
    {
        return !player.isCreative() && !player.isSpectator() && Config.Baked.localizedBodyDamageEnabled;
    }

    private static void playerDrinkEffect(Player player)
    {
        //Play sound and swing arm
        player.swing(InteractionHand.MAIN_HAND);
        player.playSound(SoundEvents.GENERIC_DRINK, 1.0f, 1.0f);
    }

    private static HydrationEnum playerGetHydrationEnum(Player player) {
        HydrationEnum hydrationEnum = ThirstUtil.traceWater(player);
        if (hydrationEnum != null && player.getMainHandItem().isEmpty()) {
            ThirstCapability thirstCapability = CapabilityUtil.getThirstCapability(player);
            if (thirstCapability.isHydrationLevelAtMax()) {
                return null;
            }
            return hydrationEnum;
        }
        return null;
    }
}
