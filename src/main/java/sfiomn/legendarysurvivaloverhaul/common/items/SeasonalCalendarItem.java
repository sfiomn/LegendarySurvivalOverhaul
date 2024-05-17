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

import static sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsUtil.formatSeasonName;

public class SeasonalCalendarItem extends Item {
    public SeasonalCalendarItem(Properties properties){
        super(properties.stacksTo(1));
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (world.isClientSide())
            player.displayClientMessage(formatSeasonName(player.blockPosition(), player.level), true);
        return super.use(world, player, hand);
    }

    @Override
    public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable CompoundNBT nbt) {
        return new TemperatureItemCapability.TemperatureItemProvider();
    }
}
