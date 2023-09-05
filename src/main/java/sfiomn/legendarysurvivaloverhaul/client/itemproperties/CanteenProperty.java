package sfiomn.legendarysurvivaloverhaul.client.itemproperties;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;


public class CanteenProperty implements IItemPropertyGetter {

    @OnlyIn(Dist.CLIENT)
    @Override
    public float call(ItemStack stack, ClientWorld clientWorld, LivingEntity entity)
    {
        int capacity = ThirstUtil.getCapacityTag(stack);
        stack.setDamageValue(capacity);

        HydrationEnum hydrationEnum = ThirstUtil.getHydrationEnumTag(stack);
        if (hydrationEnum != null) {
            return hydrationEnum.ordinal();
        }

        return 0.0f;
    }
}
