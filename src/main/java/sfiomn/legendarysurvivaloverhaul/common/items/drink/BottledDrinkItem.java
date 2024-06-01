package sfiomn.legendarysurvivaloverhaul.common.items.drink;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public abstract class BottledDrinkItem extends DrinkItem {

    public BottledDrinkItem(Properties properties) {
        super(properties.stacksTo(16));
    }

    @Override
    public int getUseDuration(ItemStack stack)
    {
        return 32;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entity)
    {
        if(world.isClientSide || !(entity instanceof PlayerEntity))
            return stack;

        PlayerEntity player = (PlayerEntity)entity;

        stack = super.finishUsingItem(stack, world, player);

        if (Config.Baked.glassBottleLootAfterDrink) {
            ItemStack glassBottle = new ItemStack(Items.GLASS_BOTTLE);

            if (stack.isEmpty()) {
                return glassBottle;
            } else {
                int slot = player.inventory.findSlotMatchingUnusedItem(glassBottle);
                if (slot == -1)
                    slot = player.inventory.getFreeSlot();
                if (slot > -1)
                    player.inventory.add(slot, glassBottle);
                else
                    player.drop(glassBottle, false);
            }
        }
        return stack;
    }

    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        return false;
    }
}
