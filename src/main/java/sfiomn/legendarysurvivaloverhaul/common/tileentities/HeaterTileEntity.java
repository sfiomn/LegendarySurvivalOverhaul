package sfiomn.legendarysurvivaloverhaul.common.tileentities;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.common.containers.HeaterContainer;
import sfiomn.legendarysurvivaloverhaul.registry.TileEntityRegistry;

public class HeaterTileEntity extends AbstractThermalTileEntity {

    public HeaterTileEntity() {
        this(TileEntityRegistry.HEATER_TILE_ENTITY.get());
    }

    public HeaterTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn, ThermalTypeEnum.HEATING);
    }

    @Override
    public ITextComponent getDefaultName() {
        return new TranslationTextComponent("container." + LegendarySurvivalOverhaul.MOD_ID + ".heater");
    }

    @Override
    public Container createMenu(int id, PlayerInventory playerInventory) {
        return new HeaterContainer(id, playerInventory, this);
    }
}
