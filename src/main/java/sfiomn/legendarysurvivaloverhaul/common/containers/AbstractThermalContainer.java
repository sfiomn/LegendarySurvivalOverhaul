package sfiomn.legendarysurvivaloverhaul.common.containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.common.tileentity.AbstractThermalTileEntity;

public abstract class AbstractThermalContainer extends Container {

    private final AbstractThermalTileEntity tileEntity;
    private final ThermalTypeEnum thermalType;

    public AbstractThermalContainer(int windowId, PlayerInventory playerInventory, AbstractThermalTileEntity te, RegistryObject<ContainerType<AbstractThermalContainer>> registryObject, ThermalTypeEnum thermalType) {
        super(registryObject.get(), windowId);
        this.thermalType = thermalType;
        this.tileEntity = te;

        if (te != null) {
            addSlot(addThermalSlot(te, 0, 12, 32));
            addSlot(addThermalSlot(te, 1, 31, 32));
            addSlot(addThermalSlot(te, 2, 12, 51));
            addSlot(addThermalSlot(te, 3, 31, 51));
        }

        layoutPlayerInventorySlots(playerInventory, 8, 86);
    }

    private Slot addThermalSlot(AbstractThermalTileEntity te, int index, int posX, int posY) {
        return new Slot(te, index, posX, posY) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return te.isItemValid(stack.getItem());
            }
        };
    }

    public ThermalTypeEnum getThermalType() {
        return this.thermalType;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isPowered() {
        return this.tileEntity.isPowered();
    }

    @OnlyIn(Dist.CLIENT)
    public float getFuelTimeScale() {
        return this.tileEntity.getFuelTimeScale();
    }

    public static AbstractThermalTileEntity getTileEntity(PlayerInventory playerInv, PacketBuffer data)
    {
        BlockPos tePos = data.readBlockPos();
        TileEntity te = playerInv.player.level.getBlockEntity(tePos);

        if(te instanceof AbstractThermalTileEntity)
        {
            return (AbstractThermalTileEntity) te;
        } else {
            throw new IllegalStateException("Missing Thermal tile entity");
        }
    }

    @Override
    public boolean stillValid(PlayerEntity playerEntity) {
        return this.tileEntity.stillValid(playerEntity);
    }

    private int addSlotRange(PlayerInventory playerInventory, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new Slot(playerInventory, index, x, y));
            x += dx;
            index++;
        }

        return index;
    }

    private int addSlotBox(PlayerInventory playerInventory, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(playerInventory, index, x, y, horAmount, dx);
            y += dy;
        }

        return index;
    }

    private void layoutPlayerInventorySlots(PlayerInventory playerInventory, int leftCol, int topRow) {
        int lastIndex = addSlotRange(playerInventory, 0, leftCol, topRow + 58, 9, 18);
        addSlotBox(playerInventory, lastIndex, leftCol, topRow, 9, 18, 3, 18);

    }

    //  0 - 3 = TileInventory slots, which map to our TileEntity slot numbers 0 - 3)
    //  4 - 12 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  13 - 39 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            // Index is in Tile entity inventory
            if (index < AbstractThermalTileEntity.SLOT_COUNT) {
                // Move to Player inventory
                if (!this.moveItemStackTo(itemstack1, AbstractThermalTileEntity.SLOT_COUNT + 9, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < this.slots.size()) {
                // Item is a valid fuel for the tile entity
                if (tileEntity.isItemValid(itemstack1.getItem())) {
                    // Move to Tile entity inventory
                    if (!this.moveItemStackTo(itemstack1, 0, AbstractThermalTileEntity.SLOT_COUNT, false)) {
                        return ItemStack.EMPTY;
                    }
                // Index is in Player hot bar
                } else if (index < AbstractThermalTileEntity.SLOT_COUNT + 9) {
                    // Move to Player inventory
                    if (!this.moveItemStackTo(itemstack1, AbstractThermalTileEntity.SLOT_COUNT + 9, this.slots.size(), false)) {
                        return ItemStack.EMPTY;
                    }
                // Index is in Player inventory
                } else {
                    // Move to Player hot bar
                    if (!this.moveItemStackTo(itemstack1, AbstractThermalTileEntity.SLOT_COUNT, AbstractThermalTileEntity.SLOT_COUNT + 9, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else {
                return ItemStack.EMPTY;
            }

            // If stack size == 0 (the entire stack was moved) set slot contents to null
            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }
}
