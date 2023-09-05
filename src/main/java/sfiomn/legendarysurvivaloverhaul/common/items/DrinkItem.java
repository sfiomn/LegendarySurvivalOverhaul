package sfiomn.legendarysurvivaloverhaul.common.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonThirst;
import sfiomn.legendarysurvivaloverhaul.api.thirst.IThirstCapability;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

public abstract class DrinkItem extends Item {

    public DrinkItem(Properties properties) {
        super(properties.stacksTo(16));
    }

    public void runSecondaryEffect(PlayerEntity player, ItemStack stack)
    {
        //Can be overridden to run a special task
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack)
    {
        return UseAction.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack)
    {
        return 32;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
    {
        ItemStack stack = player.getItemInHand(hand);

        if(!Config.Baked.thirstEnabled)
        {
            // Don't restrict drinking if thirst is disabled
            player.startUsingItem(hand);
            return ActionResult.success(stack);
        }

        IThirstCapability capability = CapabilityUtil.getThirstCapability(player);
        if(!capability.isHydrationLevelAtMax())
        {
            player.startUsingItem(hand);
            return ActionResult.success(stack);
        }

        return ActionResult.fail(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entity)
    {
        if(world.isClientSide || !(entity instanceof PlayerEntity))
            return stack;

        PlayerEntity player = (PlayerEntity)entity;


        JsonThirst jsonThirst = null;
        // Check if the JSON has overridden the drink's defaults, and if so, allow ThirstHandler to take over
        if (this.getRegistryName() != null)
            jsonThirst = JsonConfig.consumableThirst.get(this.getRegistryName().toString());

        if(jsonThirst != null)
            ThirstUtil.takeDrink(player, jsonThirst.hydration, jsonThirst.saturation, jsonThirst.dirty);

        runSecondaryEffect(player, stack);

        stack.shrink(1);

        ItemStack glassBottle = new ItemStack(Items.GLASS_BOTTLE);

        if(stack.isEmpty())
        {
            return glassBottle;
        }
        else
        {
            int slot = player.inventory.findSlotMatchingUnusedItem(glassBottle);
            if (slot == -1)
                slot = player.inventory.getFreeSlot();
            if (slot > -1)
                player.inventory.add(slot, glassBottle);
            else
                player.drop(glassBottle, false);

            return stack;
        }
    }

    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        return false;
    }
}
