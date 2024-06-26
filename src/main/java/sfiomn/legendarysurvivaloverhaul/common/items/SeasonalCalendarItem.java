package sfiomn.legendarysurvivaloverhaul.common.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsUtil.formatSeasonName;

public class SeasonalCalendarItem extends Item {
    public SeasonalCalendarItem(Item.Properties properties){
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide())
            player.displayClientMessage(formatSeasonName(player.blockPosition(), player.level()), true);
        return super.use(level, player, hand);
    }
}
