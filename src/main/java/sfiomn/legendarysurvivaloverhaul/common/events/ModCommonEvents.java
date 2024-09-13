package sfiomn.legendarysurvivaloverhaul.common.events;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.*;
import net.minecraft.world.GameRules;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.IWorldInfo;
import net.minecraft.world.storage.ServerWorldInfo;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.SleepFinishedTimeEvent;
import net.minecraftforge.event.world.WorldEvent;
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
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonBlockFluidThirst;
import sfiomn.legendarysurvivaloverhaul.api.config.json.thirst.JsonConsumableThirst;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.client.integration.sereneseasons.RenderSeasonCards;
import sfiomn.legendarysurvivaloverhaul.client.screens.ClientHooks;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.common.integration.curios.CuriosUtil;
import sfiomn.legendarysurvivaloverhaul.common.items.drink.DrinkItem;
import sfiomn.legendarysurvivaloverhaul.common.items.heal.BodyHealingItem;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;
import sfiomn.legendarysurvivaloverhaul.network.packets.MessageDrinkBlockFluid;
import sfiomn.legendarysurvivaloverhaul.registry.EffectRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;
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

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (shouldApplyThirst(event.getPlayer())) {
            // Only run on main hand (otherwise it runs twice)
            if(event.getHand()== Hand.MAIN_HAND && event.getPlayer().getMainHandItem().isEmpty())
            {
                JsonBlockFluidThirst jsonBlockFluidThirst = ThirstUtil.getJsonBlockFluidThirstLookedAt(event.getPlayer(), event.getPlayer().getAttributeValue(ForgeMod.REACH_DISTANCE.get()) / 2);
                if (jsonBlockFluidThirst != null && (jsonBlockFluidThirst.hydration != 0 || jsonBlockFluidThirst.saturation != 0)) {
                    ThirstCapability thirstCapability = CapabilityUtil.getThirstCapability(event.getPlayer());
                    if (!thirstCapability.isHydrationLevelAtMax()) {
                        if (event.getWorld().isClientSide)
                            playerDrinkEffect(event.getPlayer());
                        else {
                            ThirstUtil.takeDrink(event.getPlayer(), jsonBlockFluidThirst.hydration, jsonBlockFluidThirst.saturation, jsonBlockFluidThirst.effects);
                        }
                    }
                }
            }
        }
    }

    // Only Client side event
    @SubscribeEvent
    public static void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        if (shouldApplyThirst(event.getPlayer())) {
            // Only run on main hand (otherwise it runs twice)
            if(event.getHand() == Hand.MAIN_HAND && event.getPlayer().getMainHandItem().isEmpty())
            {
                JsonBlockFluidThirst jsonBlockFluidThirst = ThirstUtil.getJsonBlockFluidThirstLookedAt(event.getPlayer(), event.getPlayer().getAttributeValue(ForgeMod.REACH_DISTANCE.get()) / 2);

                if (jsonBlockFluidThirst != null && (jsonBlockFluidThirst.hydration != 0 || jsonBlockFluidThirst.saturation != 0)) {
                    ThirstCapability thirstCapability = CapabilityUtil.getThirstCapability(event.getPlayer());
                    if (!thirstCapability.isHydrationLevelAtMax()) {
                        playerDrinkEffect(event.getPlayer());
                        MessageDrinkBlockFluid messageDrinkToServer = new MessageDrinkBlockFluid();
                        NetworkHandler.INSTANCE.sendToServer(messageDrinkToServer);
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

    @SubscribeEvent
    public static void onLevelLoad(WorldEvent.Load event) {
        if (!event.getWorld().isClientSide()) {
            IWorldInfo levelData = event.getWorld().getLevelData();
            if (levelData instanceof ServerWorldInfo && event.getWorld() instanceof ServerWorld)
                levelData.getGameRules().getRule(GameRules.RULE_NATURAL_REGENERATION).set(Config.Baked.naturalRegenerationEnabled, ((ServerWorld) event.getWorld()).getServer());
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

    public static void playerDrinkEffect(PlayerEntity player)
    {
        //Play sound and swing arm
        player.swing(Hand.MAIN_HAND);
        player.playSound(SoundEvents.GENERIC_DRINK, 1.0f, 1.0f);
    }
}
