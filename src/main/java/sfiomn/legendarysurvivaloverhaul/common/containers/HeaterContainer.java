package sfiomn.legendarysurvivaloverhaul.common.containers;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.common.tileentity.AbstractThermalTileEntity;
import sfiomn.legendarysurvivaloverhaul.registry.ContainerRegistry;

public class HeaterContainer extends AbstractThermalContainer {

    public HeaterContainer(int windowId, PlayerInventory playerInventory, AbstractThermalTileEntity te) {
        super(windowId, playerInventory, te, ContainerRegistry.HEATER_CONTAINER, ThermalTypeEnum.HEATING);
    }

    public HeaterContainer(final int windowId, final PlayerInventory playerInv, final PacketBuffer data)
    {
        this(windowId, playerInv, getTileEntity(playerInv, data));
    }
}
