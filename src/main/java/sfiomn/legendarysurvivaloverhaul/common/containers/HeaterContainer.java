package sfiomn.legendarysurvivaloverhaul.common.containers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.common.blockentities.AbstractThermalBlockEntity;
import sfiomn.legendarysurvivaloverhaul.registry.BlockRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.ContainerRegistry;

public class HeaterContainer extends AbstractThermalContainer {

    public HeaterContainer(int windowId, Inventory inventory, BlockEntity be, ContainerData dataAccess) {
        super(windowId, inventory, (AbstractThermalBlockEntity) be, dataAccess, ContainerRegistry.HEATER_CONTAINER, ThermalTypeEnum.HEATING);
    }

    public HeaterContainer(final int windowId, final Inventory playerInv, final FriendlyByteBuf data)
    {
        this(windowId, playerInv, playerInv.player.level().getBlockEntity(data.readBlockPos()), new SimpleContainerData(2));
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(this.level, blockEntity.getBlockPos()), player, BlockRegistry.HEATER.get());
    }
}
