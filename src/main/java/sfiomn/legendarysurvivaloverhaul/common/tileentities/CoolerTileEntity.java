package sfiomn.legendarysurvivaloverhaul.common.tileentities;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.common.containers.CoolerContainer;
import sfiomn.legendarysurvivaloverhaul.registry.TileEntityRegistry;

public class CoolerTileEntity extends AbstractThermalTileEntity {

    public CoolerTileEntity() {
        this(TileEntityRegistry.COOLER_TILE_ENTITY.get());
    }

    public CoolerTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn, ThermalTypeEnum.COOLING);
    }

    @Override
    public ITextComponent getDefaultName() {
        return new TranslationTextComponent("container." + LegendarySurvivalOverhaul.MOD_ID + ".cooler");
    }

    @Override
    public Container createMenu(int id, PlayerInventory playerInventory) {
        return new CoolerContainer(id, playerInventory, this);
    }
}
