package sfiomn.legendarysurvivaloverhaul.common.events;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.DamageSources;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.DamageDistributionEnum;
import sfiomn.legendarysurvivaloverhaul.api.config.json.bodydamage.JsonBodyPartsDamageSource;
import sfiomn.legendarysurvivaloverhaul.api.config.json.bodydamage.JsonConsumableHeal;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonConsumableTemperature;
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonConsumableThirst;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.client.screens.ClientHooks;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.common.items.drink.DrinkItem;
import sfiomn.legendarysurvivaloverhaul.common.items.heal.BodyHealingItem;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;
import sfiomn.legendarysurvivaloverhaul.network.packets.MessageDrinkWater;
import sfiomn.legendarysurvivaloverhaul.registry.EffectRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;
import sfiomn.legendarysurvivaloverhaul.util.PlayerModelUtil;

import java.util.*;

@Mod.EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModCommonEvents {

    @SubscribeEvent
    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event)
    {
        Entity entity = event.getEntityLiving();
        if (!(entity instanceof PlayerEntity) || entity.level.isClientSide)
            return;

        PlayerEntity player = (PlayerEntity) entity;
        ResourceLocation itemRegistryName = event.getItem().getItem().getRegistryName();

        if (Config.Baked.temperatureEnabled) {
            List<JsonConsumableTemperature> jsonConsumableTemperatures = null;
            if (itemRegistryName != null)
                jsonConsumableTemperatures = JsonConfig.consumableTemperature.get(itemRegistryName.toString());

            if (jsonConsumableTemperatures != null) {
                for (JsonConsumableTemperature jct : jsonConsumableTemperatures) {
                    if (jct.getEffect() != null) {
                        player.addEffect(new EffectInstance(jct.getEffect(), jct.duration, (Math.abs(jct.temperatureLevel) - 1), false, false, true));
                        player.removeEffect(jct.getOppositeEffect());
                    }
                }
            }
        }

        if (Config.Baked.thirstEnabled && ThirstUtil.isThirstActive(player) && !entity.level.isClientSide && !(event.getItem().getItem() instanceof DrinkItem)) {
            JsonConsumableThirst jsonConsumableThirst = ThirstUtil.getThirstJsonConfig(itemRegistryName, event.getItem());

            if (jsonConsumableThirst != null) {
                ThirstUtil.takeDrink(player, jsonConsumableThirst.hydration, jsonConsumableThirst.saturation, jsonConsumableThirst.effects);
            } else {
                HydrationEnum hydrationEnum = ThirstUtil.getHydrationEnumTag(event.getItem());
                if (hydrationEnum != null) {
                    ThirstUtil.takeDrink(player, hydrationEnum);
                }
            }
        }

        if (Config.Baked.localizedBodyDamageEnabled && !(event.getItem().getItem() instanceof BodyHealingItem)) {
            JsonConsumableHeal jsonConsumableHeal = null;
            if (itemRegistryName != null)
                jsonConsumableHeal = JsonConfig.consumableHeal.get(itemRegistryName.toString());

            if (jsonConsumableHeal != null) {
                if (jsonConsumableHeal.healingCharges > 0) {
                    if (player.level.isClientSide && Minecraft.getInstance().screen == null)
                        ClientHooks.openBodyHealthScreen(player, ((PlayerEntity) entity).getUsedItemHand(), true,
                                jsonConsumableHeal.healingCharges, jsonConsumableHeal.healingValue, jsonConsumableHeal.healingTime);
                } else if (jsonConsumableHeal.healingCharges == 0) {
                    for (BodyPartEnum bodyPart : BodyPartEnum.values()) {
                        BodyDamageUtil.applyHealingTimeBodyPart(player, bodyPart, jsonConsumableHeal.healingValue, jsonConsumableHeal.healingTime);
                    }
                    player.level.playSound(null, entity, SoundRegistry.HEAL_BODY_PART.get(), SoundCategory.PLAYERS, 1.0f, 1.0f);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event) {
        Entity entity = event.getEntityLiving();
        if (entity instanceof PlayerEntity && shouldApplyThirst((PlayerEntity) entity) && !entity.level.isClientSide) {
            ThirstUtil.addExhaustion((PlayerEntity) entity, (float) Config.Baked.onJumpThirstExhaustion);
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        PlayerEntity player = event.getPlayer();
        if (shouldApplyThirst(player) && !player.level.isClientSide && event.getState().getBlock().canHarvestBlock(event.getState(), event.getWorld(), event.getPos(), player) && event.getState().getDestroySpeed(event.getWorld(), event.getPos()) > 0.0f) {
            ThirstUtil.addExhaustion(player, (float) Config.Baked.onBlockBreakThirstExhaustion);
        }
    }

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        PlayerEntity player = event.getPlayer();
        if (shouldApplyThirst(player) && !player.level.isClientSide) {
            Entity monster = event.getTarget();
            if(monster.isAttackable()) {
                ThirstUtil.addExhaustion(player, (float) Config.Baked.onAttackThirstExhaustion);
            }
        }
    }

    // Only Client side event
    @SubscribeEvent
    public static void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        if (shouldApplyThirst(event.getPlayer())) {
            // Only run on main hand (otherwise it runs twice)
            if(event.getHand() == Hand.MAIN_HAND)
            {
                HydrationEnum water = playerGetHydrationEnum(event.getPlayer());

                if (water != null) {
                    playerDrinkEffect(event.getPlayer());
                    MessageDrinkWater messageDrinkToServer = new MessageDrinkWater();
                    NetworkHandler.INSTANCE.sendToServer(messageDrinkToServer);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (shouldApplyThirst(event.getPlayer())) {
            // Only run on main hand (otherwise it runs twice)
            if(event.getHand()==Hand.MAIN_HAND && event.getPlayer().getMainHandItem().isEmpty())
            {
                HydrationEnum water = playerGetHydrationEnum(event.getPlayer());

                if (water != null) {
                    if (event.getWorld().isClientSide)
                        playerDrinkEffect(event.getPlayer());
                    else {
                        ThirstUtil.takeDrink(event.getPlayer(), water);
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityHurt(LivingHurtEvent event) {
        if (event.getSource() != (DamageSource.FALL) &&
            event.getSource() != (DamageSource.DROWN) &&
            event.getSource() != (DamageSources.DEHYDRATION) &&
            event.getSource() != (DamageSources.HYPOTHERMIA) &&
            event.getSource() != (DamageSources.HYPERTHERMIA) && event.getEntityLiving().hasEffect(EffectRegistry.VULNERABILITY.get()))
            event.setAmount(event.getAmount() * (1 + 0.2f * Objects.requireNonNull(event.getEntityLiving().getEffect(EffectRegistry.VULNERABILITY.get())).getAmplifier() + 1));

        else if (event.getSource() == DamageSource.FALL && event.getEntityLiving().hasEffect(EffectRegistry.HARD_FALLING.get())) {
            event.setAmount(event.getAmount() * (1 + 0.2f * Objects.requireNonNull(event.getEntityLiving().getEffect(EffectRegistry.HARD_FALLING.get())).getAmplifier() + 1));
            event.getEntityLiving().level.playSound(null, event.getEntityLiving(), SoundRegistry.HARD_FALLING_HURT.get(), SoundCategory.PLAYERS, 1.0F, 1.0F);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityHurtDamage(LivingDamageEvent event) {
        PlayerEntity player;
        if (event.getEntityLiving() instanceof PlayerEntity)
            player = (PlayerEntity) event.getEntityLiving();
        else return;

        if (player.level.isClientSide || !shouldApplyLocalizedBodyDamage(player))
            return;

        float bodyPartDamageValue = event.getAmount() * (float) Config.Baked.bodyDamageMultiplier;
        DamageSource source = event.getSource();

        JsonBodyPartsDamageSource damageSourceBodyParts = JsonConfig.damageSourceBodyParts.get(source.msgId);
        List<BodyPartEnum> hitBodyParts = new ArrayList<>();
        if (damageSourceBodyParts != null) {
            if (damageSourceBodyParts.damageDistribution == DamageDistributionEnum.NONE)
                return;

            hitBodyParts.addAll(damageSourceBodyParts.getBodyParts(player));
        }


        if (hitBodyParts.isEmpty()) {
             if (source.isProjectile()) {
                hitBodyParts.addAll(PlayerModelUtil.getPreciseEntityImpact(source.getDirectEntity(), player));

            } else if (source.isExplosion()) {
                 hitBodyParts.addAll(DamageDistributionEnum.ALL.getBodyParts(player, Arrays.asList(BodyPartEnum.values())));

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

        if (!hitBodyParts.isEmpty())
            BodyDamageUtil.balancedHurtBodyParts(player, hitBodyParts, bodyPartDamageValue);

        if (source.isProjectile() && hitBodyParts.contains(BodyPartEnum.HEAD) && Config.Baked.headCriticalShotMultiplier > 1) {
            event.setAmount(event.getAmount() * (float) Config.Baked.headCriticalShotMultiplier);
            player.level.playSound(null, player, SoundRegistry.HEADSHOT.get(), SoundCategory.HOSTILE, 1.0F, 1.0F);
        }
    }

    @SubscribeEvent
    public static void onSleepFinished(SleepFinishedTimeEvent event) {
        for (PlayerEntity player : event.getWorld().players()) {
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

        PlayerEntity player = event.getPlayer();
        if (Config.Baked.temperatureResistanceOnDeathEnabled) {
            player.addEffect(new EffectInstance(EffectRegistry.HEAT_RESISTANCE.get(), Config.Baked.temperatureResistanceOnDeathTime, 0, false, false, true));
            player.addEffect(new EffectInstance(EffectRegistry.COLD_RESISTANCE.get(), Config.Baked.temperatureResistanceOnDeathTime, 0, false, false, true));
        }
    }

    private static boolean shouldApplyThirst(PlayerEntity player)
    {
        return !player.isCreative() && !player.isSpectator() && Config.Baked.thirstEnabled && ThirstUtil.isThirstActive(player);
    }

    private static boolean shouldApplyLocalizedBodyDamage(PlayerEntity player)
    {
        return !player.isCreative() && !player.isSpectator() && Config.Baked.localizedBodyDamageEnabled;
    }

    private static void playerDrinkEffect(PlayerEntity player)
    {
        //Play sound and swing arm
        player.swing(Hand.MAIN_HAND);
        player.playSound(SoundEvents.GENERIC_DRINK, 1.0f, 1.0f);
    }

    private static HydrationEnum playerGetHydrationEnum(PlayerEntity player) {
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
