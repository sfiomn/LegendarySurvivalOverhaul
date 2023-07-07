package sfiomn.legendarysurvivaloverhaul.common.containers;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.common.tileentities.AbstractThermalTileEntity;
import sfiomn.legendarysurvivaloverhaul.registry.ContainerRegistry;

public class CoolerContainer extends AbstractThermalContainer {

    public CoolerContainer(int windowId, PlayerInventory playerInventory, AbstractThermalTileEntity te) {
        super(windowId, playerInventory, te, ContainerRegistry.COOLER_CONTAINER, ThermalTypeEnum.COOLING);
    }

    public CoolerContainer(final int windowId, final PlayerInventory playerInv, final PacketBuffer data)
    {
        this(windowId, playerInv, getTileEntity(playerInv, data));
    }
}
