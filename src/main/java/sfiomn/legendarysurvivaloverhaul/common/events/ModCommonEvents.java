package sfiomn.legendarysurvivaloverhaul.common.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonConsumableTemperature;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonThirst;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstEnum;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;
import sfiomn.legendarysurvivaloverhaul.network.packets.MessageDrinkWater;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import java.util.List;

@Mod.EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModCommonEvents {

    @SubscribeEvent
    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event)
    {
        Entity entity = event.getEntityLiving();
        if (entity instanceof PlayerEntity && shouldApplyTemperature((PlayerEntity) entity) && !entity.level.isClientSide) {
            PlayerEntity player = (PlayerEntity) entity;

            ResourceLocation itemRegistryName = event.getItem().getItem().getRegistryName();
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
        if (entity instanceof PlayerEntity && shouldApplyThirst((PlayerEntity) entity) && !entity.level.isClientSide) {
            PlayerEntity player = (PlayerEntity) entity;

            ResourceLocation itemRegistryName = event.getItem().getItem().getRegistryName();
            JsonThirst jsonConsumableThirst = null;
            if (itemRegistryName != null)
                jsonConsumableThirst = JsonConfig.consumableThirst.get(itemRegistryName.toString());

            if (jsonConsumableThirst != null && (jsonConsumableThirst.thirst != 0 || jsonConsumableThirst.saturation != 0)) {
                ThirstUtil.takeDrink(player, jsonConsumableThirst.thirst, jsonConsumableThirst.saturation, jsonConsumableThirst.dirty);
            } else if (event.getItem().getItem() == Items.POTION){
                Potion potion = PotionUtils.getPotion(event.getItem());
                if(potion == Potions.WATER || potion == Potions.AWKWARD || potion == Potions.MUNDANE || potion == Potions.THICK)
                {
                    ThirstUtil.takeDrink(player, ThirstEnum.NORMAL);
                }
                else if (potion != Potions.EMPTY)
                {
                    ThirstUtil.takeDrink(player, ThirstEnum.POTION);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event) {
        Entity entity = event.getEntityLiving();
        if (entity instanceof PlayerEntity && shouldApplyThirst((PlayerEntity) entity) && !entity.level.isClientSide) {
            ThirstUtil.addExhaustion((PlayerEntity) entity, Config.Baked.onJumpThirstExhaustion);
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        PlayerEntity player = event.getPlayer();
        if (shouldApplyThirst(player) && !player.level.isClientSide && event.getState().getBlock().canHarvestBlock(event.getState(), event.getWorld(), event.getPos(), player) && event.getState().getDestroySpeed(event.getWorld(), event.getPos()) > 0.0f) {
            ThirstUtil.addExhaustion(player, Config.Baked.onBlockBreakThirstExhaustion);
        }
    }

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        PlayerEntity player = event.getPlayer();
        if (shouldApplyThirst(player) && !player.level.isClientSide) {
            Entity monster = event.getTarget();
            if(monster.isAttackable()) {
                ThirstUtil.addExhaustion(player, Config.Baked.onAttackThirstExhaustion);
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
                ThirstEnum water = playerDrink(event.getPlayer());

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
                ThirstEnum water = playerDrink(event.getPlayer());

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

    private static boolean shouldApplyThirst(PlayerEntity player)
    {
        return !player.isCreative() && !player.isSpectator() && Config.Baked.thirstEnabled;
    }

    private static boolean shouldApplyTemperature(PlayerEntity player)
    {
        return !player.isCreative() && !player.isSpectator() && Config.Baked.temperatureEnabled;
    }

    private static void playerDrinkEffect(PlayerEntity player)
    {
        //Play sound and swing arm
        player.swing(Hand.MAIN_HAND);
        player.playSound(SoundEvents.GENERIC_DRINK, 1.0f, 1.0f);
    }

    private static ThirstEnum playerDrink(PlayerEntity player) {
        ThirstEnum thirstEnum = ThirstUtil.traceWater(player);
        if (thirstEnum != null && player.getMainHandItem().isEmpty()) {
            ThirstCapability thirstCapability = CapabilityUtil.getThirstCapability(player);
            if (thirstCapability.isThirstLevelAtMax()) {
                return null;
            }
            return thirstEnum;
        }
        return null;
    }
}
