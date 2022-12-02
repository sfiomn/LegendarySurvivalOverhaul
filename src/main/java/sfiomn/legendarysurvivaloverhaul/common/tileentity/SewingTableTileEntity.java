package sfiomn.legendarysurvivaloverhaul.common.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.containers.SewingTableContainer;
import sfiomn.legendarysurvivaloverhaul.registry.TileEntityRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SewingTableTileEntity extends LockableTileEntity implements ISidedInventory, IRecipeHolder, IRecipeHelperPopulator {
    protected NonNullList<ItemStack> items;
    public static final int SLOT_COUNT = 3;

    public SewingTableTileEntity() {
        this(TileEntityRegistry.SEWING_TABLE_TILE_ENTITY.get());
    }

    public SewingTableTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
        this.items = NonNullList.withSize(SewingTableTileEntity.SLOT_COUNT, ItemStack.EMPTY);
    }

    @Override
    public ITextComponent getDefaultName() {
        return new TranslationTextComponent("container." + LegendarySurvivalOverhaul.MOD_ID + ".sewing_table");
    }

    @Override
    public Container createMenu(int id, PlayerInventory playerInventory) {
        return new SewingTableContainer(id, playerInventory, IWorldPosCallable.create(playerInventory.player.level, playerInventory.player.blockPosition()));
    }

    public boolean isItemArmor(Item item) {
        return item.getItemCategory() == ItemGroup.TAB_COMBAT;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[]{0, 1, 2};
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack itemStack) {
        switch (slot) {
            case 0: return isItemArmor(itemStack.getItem());
            case 2: return false;
            default: return true;
        }
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
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        ItemStackHelper.saveAllItems(nbt, this.items);
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
        return nbt;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        handleUpdateTag(level.getBlockState(pkt.getPos()), pkt.getTag());
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT nbt) {
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.items);
    }

    @Override
    public void fillStackedContents(RecipeItemHelper p_194018_1_) {

    }

    @Override
    public void setRecipeUsed(@Nullable IRecipe<?> p_193056_1_) {

    }

    @Nullable
    @Override
    public IRecipe<?> getRecipeUsed() {
        return null;
    }

    @Override
    public void awardUsedRecipes(PlayerEntity p_201560_1_) {
        IRecipeHolder.super.awardUsedRecipes(p_201560_1_);
    }

    @Override
    public boolean setRecipeUsed(World p_201561_1_, ServerPlayerEntity p_201561_2_, IRecipe<?> p_201561_3_) {
        return IRecipeHolder.super.setRecipeUsed(p_201561_1_, p_201561_2_, p_201561_3_);
    }
}
