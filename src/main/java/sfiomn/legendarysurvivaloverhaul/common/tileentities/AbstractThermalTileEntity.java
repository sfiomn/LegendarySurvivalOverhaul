package sfiomn.legendarysurvivaloverhaul.common.tileentities;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.api.config.json.temperature.JsonFuelItemIdentity;
import sfiomn.legendarysurvivaloverhaul.common.blocks.ThermalBlock;
import sfiomn.legendarysurvivaloverhaul.config.json.JsonConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractThermalTileEntity extends LockableTileEntity implements ITickableTileEntity, ISidedInventory {
    protected NonNullList<ItemStack> items;
    private int fuelTime;
    private int fuelDuration;
    private int tickCount;
    private final ThermalTypeEnum thermalType;
    public static final int SLOT_COUNT = 4;

    public AbstractThermalTileEntity(TileEntityType<?> tileEntityType, ThermalTypeEnum thermalType) {
        super(tileEntityType);
        this.items = NonNullList.withSize(AbstractThermalTileEntity.SLOT_COUNT, ItemStack.EMPTY);
        this.tickCount = 0;
        this.fuelTime = 0;
        this.thermalType = thermalType;
    }

    @Override
    public abstract ITextComponent getDefaultName();

    @Override
    public abstract Container createMenu(int id, PlayerInventory playerInventory);

    @Override
    public void tick() {
        if (this.level == null) {
            return;
        }

        if(!isPowered()) {
            // Remove LIT state if unpowered
            if (this.level.getBlockState(this.worldPosition).getValue(ThermalBlock.LIT) && !this.level.isClientSide) {
                this.level.setBlockAndUpdate(this.worldPosition, getBlockState().setValue(ThermalBlock.LIT, false));
                this.setChanged();
            }
            return;
        }


        // Put LIT state if powered & fuel already there
        if (isLit() && !this.level.getBlockState(this.worldPosition).getValue(ThermalBlock.LIT) && !this.level.isClientSide) {
            this.level.setBlockAndUpdate(this.worldPosition, getBlockState().setValue(ThermalBlock.LIT, this.isLit()));
            this.setChanged();
        }


        if (this.tickCount > 20) {
            this.tickCount = 0;

            boolean initiallyLit = isLit();
            boolean needRefresh = false;
            if (isLit()) {
                consumeFuel();
            }

            if (!isLit() && !this.level.isClientSide) {
                refillFuel();
                if (isLit()) {
                    this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), 2);
                    needRefresh = true;
                }
            }

            if (initiallyLit != isLit() && !this.level.isClientSide) {
                this.level.setBlockAndUpdate(this.worldPosition, getBlockState().setValue(ThermalBlock.LIT, this.isLit()));
                needRefresh = true;
            }

            if (needRefresh) {
                this.setChanged();
            }
        }

        this.tickCount++;
    }

    private void consumeFuel() {
        this.fuelTime--;
    }

    public void refillFuel() {
        for(int i = 0; i < getContainerSize(); i++) {
            if (getItem(i).getCount() > 0 && this.isItemValid(getItem(i).getItem())) {
                int fuelValue = getFuelValue(items.get(i));
                if (fuelValue > 0) {
                    this.fuelTime = fuelValue;
                    this.fuelDuration = fuelValue;
                    getItem(i).shrink(1);
                    return;
                }
            }
        }
    }

    public boolean isItemValid(Item item) {
        ResourceLocation registryNameItem = item.getRegistryName();
        if (registryNameItem != null) {
            JsonFuelItemIdentity fuelInfo = JsonConfig.fuelItems.get(registryNameItem.toString());
            return fuelInfo != null && fuelInfo.thermalType == thermalType && fuelInfo.fuelValue > 0;
        }
        return false;
    }

    public int getFuelValue(ItemStack item) {
        ResourceLocation registryNameItem = item.getItem().getRegistryName();
        if (registryNameItem != null) {
            JsonFuelItemIdentity fuelInfo = JsonConfig.fuelItems.get(registryNameItem.toString());
            if (fuelInfo != null) {
                return fuelInfo.fuelValue;
            }
        }
        return 0;
    }

    public boolean isLit() {
        if (this.level != null) {
            return this.fuelTime > 0;
        }
        return false;
    }

    public boolean isPowered() {
        if (this.level != null) {
            return this.level.hasNeighborSignal(this.worldPosition);
        }
        return false;
    }

    public float getFuelTimeScale() {
        if (this.fuelDuration != 0) {
            return (float) this.fuelTime / this.fuelDuration;
        } else {
            return 0.0f;
        }
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[]{0, 1, 2, 3};
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack itemStack) {
        return isItemValid(itemStack.getItem());
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, @Nullable Direction direction) {
        return this.canPlaceItem(slot, itemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        return slot < SLOT_COUNT;
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Nonnull
    @Override
    public ItemStack getItem(int slot) {
        return this.items.get(slot);
    }

    @Nonnull
    @Override
    public ItemStack removeItem(int slot, int amount) {
        return ItemStackHelper.removeItem(this.items, slot, amount);
    }

    @Nonnull
    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ItemStackHelper.takeItem(this.items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.items.set(slot, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return this.level != null
                && this.level.getBlockEntity(this.worldPosition) == this
                && player.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5) <= 64;
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.items);
        this.fuelTime = nbt.getInt("fuelTime");
        this.fuelDuration = nbt.getInt("fuelDuration");
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        ItemStackHelper.saveAllItems(nbt, this.items);
        nbt.putInt("fuelTime", this.fuelTime);
        nbt.putInt("fuelDuration", this.fuelDuration);
        return nbt;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbt = this.getUpdateTag();
        ItemStackHelper.saveAllItems(nbt, this.items);
        return new SUpdateTileEntityPacket(this.worldPosition, 1, nbt);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = super.getUpdateTag();
        nbt.putInt("fuelTime", fuelTime);
        nbt.putInt("fuelDuration", fuelDuration);
        return nbt;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        handleUpdateTag(level.getBlockState(pkt.getPos()), pkt.getTag());
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT nbt) {
        fuelTime = nbt.getInt("fuelTime");
        fuelDuration = nbt.getInt("fuelDuration");
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.items);
    }
}
