package sfiomn.legendarysurvivaloverhaul.common.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

public class NetherChalice extends Item {
    public NetherChalice(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand usedHand) {
        ItemStack itemstack = player.getItemInHand(usedHand);

        if (CapabilityUtil.getThirstCapability(player).isHydrationLevelAtMax())
            return ActionResult.fail(itemstack);

        RayTraceResult positionLookedAt = player.pick(player.getAttributeValue(ForgeMod.REACH_DISTANCE.get()) / 2, 0.0F, true);
        FluidState fluidState = null;

        if (positionLookedAt.getType() == RayTraceResult.Type.BLOCK) {
            fluidState = world.getFluidState(((BlockRayTraceResult) positionLookedAt).getBlockPos());
        }

        if (fluidState != null && (fluidState.getType() == Fluids.LAVA || fluidState.getType() == Fluids.FLOWING_LAVA)) {
            player.swing(Hand.MAIN_HAND);
            player.playSound(SoundEvents.GENERIC_DRINK, 1.0f, 1.0f);
            ThirstUtil.takeDrink(player, Config.Baked.hydrationLava, (float) Config.Baked.saturationLava);
            return ActionResult.pass(itemstack);
        }

        return ActionResult.fail(itemstack);
    }
}
