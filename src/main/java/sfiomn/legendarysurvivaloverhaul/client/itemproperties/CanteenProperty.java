package sfiomn.legendarysurvivaloverhaul.client.itemproperties;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;


public class CanteenProperty implements ClampedItemPropertyFunction {

    @OnlyIn(Dist.CLIENT)
    @Override
    public float unclampedCall(@NotNull ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity entity, int i)
    {
        int capacity = ThirstUtil.getCapacityTag(itemStack);
        itemStack.setDamageValue(capacity);

        HydrationEnum hydrationEnum = ThirstUtil.getHydrationEnumTag(itemStack);
        if (hydrationEnum != null) {
            return hydrationEnum.ordinal();
        }

        return 0.0f;
    }
}
