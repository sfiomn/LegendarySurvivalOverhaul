package sfiomn.legendarysurvivaloverhaul.common.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.common.containers.CoolerContainer;
import sfiomn.legendarysurvivaloverhaul.registry.BlockEntityRegistry;

public class CoolerBlockEntity extends AbstractThermalBlockEntity {

    public CoolerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.COOLER_BLOCK_ENTITY.get(), ThermalTypeEnum.COOLING, pos, state);
    }

    @Override
    public @NotNull Component getDefaultName() {
        return Component.translatable("container." + LegendarySurvivalOverhaul.MOD_ID + ".cooler");
    }

    @Override
    public @NotNull AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory) {
        return new CoolerContainer(id, inventory, this, this.dataAccess);
    }
}
