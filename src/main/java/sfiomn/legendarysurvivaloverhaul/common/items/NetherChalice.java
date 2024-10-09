package sfiomn.legendarysurvivaloverhaul.common.items;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import java.util.List;

public class NetherChalice extends Item {
    public NetherChalice(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemstack = player.getItemInHand(usedHand);

        if (CapabilityUtil.getThirstCapability(player).isHydrationLevelAtMax())
            return InteractionResultHolder.fail(itemstack);

        HitResult positionLookedAt = player.pick(player.getAttributeValue(ForgeMod.BLOCK_REACH.get()) / 2, 0.0F, true);
        FluidState fluidState = null;

        if (positionLookedAt.getType() == HitResult.Type.BLOCK) {
            fluidState = level.getFluidState(((BlockHitResult) positionLookedAt).getBlockPos());
        }

        if (fluidState != null && (fluidState.is(Fluids.LAVA) || fluidState.is(Fluids.FLOWING_LAVA))) {
            player.swing(InteractionHand.MAIN_HAND);
            player.playSound(SoundEvents.GENERIC_DRINK, 1.0f, 1.0f);
            ThirstUtil.takeDrink(player, Config.Baked.hydrationLava, (float) Config.Baked.saturationLava);
            return InteractionResultHolder.pass(itemstack);
        }

        return InteractionResultHolder.fail(itemstack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);

        tooltipComponents.add(Component.translatable("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".nether_chalice.description"));
    }
}
