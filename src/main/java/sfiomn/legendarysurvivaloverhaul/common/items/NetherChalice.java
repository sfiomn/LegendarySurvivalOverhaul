package sfiomn.legendarysurvivaloverhaul.common.items;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public class NetherChalice extends Item {
    public NetherChalice(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null)
            return InteractionResult.FAIL;

        Fluid fluidState = context.getLevel().getFluidState(context.getClickedPos()).getType();

        if (fluidState == Fluids.LAVA || fluidState == Fluids.FLOWING_LAVA) {
            ThirstUtil.takeDrink(player, Config.Baked.hydrationLava, (float) Config.Baked.saturationLava);
            return InteractionResult.PASS;
        }

        return InteractionResult.FAIL;
    }
}
