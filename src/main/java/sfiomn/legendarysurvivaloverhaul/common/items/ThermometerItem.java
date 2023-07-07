package sfiomn.legendarysurvivaloverhaul.common.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureItemCapability;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ThermometerItem extends Item {
    public ThermometerItem(Properties properties){
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (world.isClientSide()) {
            TemperatureItemCapability tempCap = CapabilityUtil.getTempItemCapability(player.getMainHandItem());

            if (tempCap.shouldUpdate()) {
                tempCap.updateWorldTemperature(world, player);
            }
            player.displayClientMessage(new StringTextComponent(tempCap.getWorldTemperatureLevel() + "\u00B0C"), (true));
        }
        return super.use(world, player, hand);
    }

    @Override
    public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable CompoundNBT nbt) {
        return new TemperatureItemCapability.TemperatureItemProvider();
    }
}
