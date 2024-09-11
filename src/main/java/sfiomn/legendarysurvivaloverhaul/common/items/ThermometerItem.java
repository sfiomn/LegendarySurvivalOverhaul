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
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;
import sfiomn.legendarysurvivaloverhaul.util.WorldUtil;

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

            float temperature = tempCap.getWorldTemperatureLevel();
            StringTextComponent temperatureComponent;
            if (Config.Baked.renderTemperatureInFahrenheit) {
                temperatureComponent = new StringTextComponent(WorldUtil.toFahrenheit(temperature) + "\u00B0F");
            } else {
                temperatureComponent = new StringTextComponent(temperature + "\u00B0C");
            }
            player.displayClientMessage(temperatureComponent, (true));
        }
        return super.use(world, player, hand);
    }

    @Override
    public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable CompoundNBT nbt) {
        return new TemperatureItemCapability.TemperatureItemProvider();
    }
}
