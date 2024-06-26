package sfiomn.legendarysurvivaloverhaul.common.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureItemCapability;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ThermometerItem extends Item {
    public ThermometerItem(Item.Properties properties){
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) {
            TemperatureItemCapability tempCap = CapabilityUtil.getTempItemCapability(player.getMainHandItem());

            if (tempCap.shouldUpdate(level.getGameTime())) {
                tempCap.updateWorldTemperature(level, player, level.getGameTime());
            }
            player.displayClientMessage(Component.literal(tempCap.getWorldTemperatureLevel() + "\u00B0C"), (true));
        }
        return super.use(level, player, hand);
    }

    @Override
    public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable CompoundTag nbt) {
        return new TemperatureItemCapability.TemperatureItemProvider();
    }
}
