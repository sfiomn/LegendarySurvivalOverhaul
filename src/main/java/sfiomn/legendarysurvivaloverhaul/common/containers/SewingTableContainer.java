package sfiomn.legendarysurvivaloverhaul.common.containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.world.World;
import sfiomn.legendarysurvivaloverhaul.api.item.PaddingEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.common.items.PaddingItem;
import sfiomn.legendarysurvivaloverhaul.data.recipes.SewingRecipe;
import sfiomn.legendarysurvivaloverhaul.registry.BlockRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.ContainerRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.RecipeRegistry;

import javax.annotation.Nullable;
import java.util.List;

public class SewingTableContainer extends Container {
    private final World world;
    private final IWorldPosCallable access;
    private final int SLOT_COUNT;
    @Nullable
    private SewingRecipe selectedRecipe;
    private final List<SewingRecipe> recipes;
    protected final CraftResultInventory resultSlots = new CraftResultInventory();
    protected final IInventory inputSlots = new Inventory(2) {
        public void setChanged() {
            super.setChanged();
            SewingTableContainer.this.slotsChanged(this);
        }
    };

    //  Constructor specified in registry
    public SewingTableContainer(int windowId, PlayerInventory playerInventory, PacketBuffer data) {
        this(windowId, playerInventory, IWorldPosCallable.NULL);
    }

    //  Constructor specified in Sewing Table block
    public SewingTableContainer(int windowId, PlayerInventory playerInventory, IWorldPosCallable pos) {
        super(ContainerRegistry.SEWING_TABLE_CONTAINER.get(), windowId);
        this.world = playerInventory.player.level;
        this.access = pos;
        this.recipes = this.world.getRecipeManager().getAllRecipesFor(RecipeRegistry.SEWING_RECIPE);

        addSlot(new Slot(inputSlots, 0, 18, 39));
        addSlot(new Slot(inputSlots, 1, 65, 39));

        addSlot(new Slot(resultSlots, 2, 134, 39) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            public ItemStack onTake(PlayerEntity player, ItemStack itemStack) {
                return SewingTableContainer.this.onTake(player, itemStack);
            }
        });

        this.SLOT_COUNT = this.inputSlots.getContainerSize() + this.resultSlots.getContainerSize();
        layoutPlayerInventorySlots(playerInventory, 8, 86);
    }

    public void slotsChanged(IInventory inventory) {
        super.slotsChanged(inventory);
        if (inventory == this.inputSlots) {
            this.createResult();
        }
    }

    public void createResult() {
        List<SewingRecipe> list = this.world.getRecipeManager().getRecipesFor(RecipeRegistry.SEWING_RECIPE, this.inputSlots, this.world);

        //  Proceed to the found recipe
        if (!list.isEmpty()) {
            this.selectedRecipe = list.get(0);
            ItemStack itemstack = this.selectedRecipe.assemble(this.inputSlots);
            this.resultSlots.setRecipeUsed(this.selectedRecipe);
            this.resultSlots.setItem(0, itemstack);

        //  Check a padding is possible
        } else {
            if (isItemArmor(this.inputSlots.getItem(0)) &&
                    isItemPadding(this.inputSlots.getItem(1))) {
                PaddingItem paddingItem = (PaddingItem) this.inputSlots.getItem(1).getItem();
                PaddingEnum padding = paddingItem.padding;
                ItemStack itemStack1 = this.inputSlots.getItem(0);
                ItemStack result;

                result = itemStack1.copy();
                TemperatureUtil.setArmorPaddingTag(result, padding.id());

                this.resultSlots.setItem(0, result);

            } else {
                this.resultSlots.setItem(0, ItemStack.EMPTY);
            }
        }
    }

    private void shrinkStackInSlot(int index) {
        ItemStack itemstack = this.inputSlots.getItem(index);
        itemstack.shrink(1);
        this.inputSlots.setItem(index, itemstack);
    }

    protected ItemStack onTake(PlayerEntity player, ItemStack itemStack) {
        itemStack.onCraftedBy(player.level, player, itemStack.getCount());
        this.resultSlots.awardUsedRecipes(player);
        if (isItemArmor(this.inputSlots.getItem(1)) &&
                isItemPadding(itemStack)) {
            String paddingId = TemperatureUtil.getArmorPaddingTag(this.inputSlots.getItem(1));
            PaddingEnum padding = PaddingEnum.getFromId(paddingId);
        } else {
            this.shrinkStackInSlot(0);
            this.shrinkStackInSlot(1);
        }
        this.access.execute((world, pos) -> {
            world.levelEvent(1044, pos, 0);
        });
        return itemStack;
    }

    public static boolean isItemArmor(ItemStack itemStack) {
        return itemStack.getItem() instanceof ArmorItem;
    }

    public static boolean isItemPadding(ItemStack itemStack) {
        return itemStack.getItem() instanceof PaddingItem;
    }

    @Override
    public boolean stillValid(PlayerEntity playerEntity) {
        return stillValid(this.access, playerEntity, BlockRegistry.SEWING_TABLE.get());
    }

    @Override
    public void removed(PlayerEntity player) {
        super.removed(player);
        this.access.execute((world, pos) -> {
            this.clearContainer(player, world, this.inputSlots);
        });
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

    //  0 - 2 = ContainerInventory slots, which map to our Container slot numbers 0 - 2)
    //  3 - 11 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  12 - 38 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            // Index is in Container inventory
            if (index < SLOT_COUNT) {
                // Move to Player inventory
                if (!this.moveItemStackTo(itemstack1, SLOT_COUNT + 9, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            // Move to Container inventory first / second slot
            } else if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                return ItemStack.EMPTY;
            // Index is in Player hot bar
            } else if (index < SLOT_COUNT + 9) {
                // Move to Player inventory
                if (!this.moveItemStackTo(itemstack1, SLOT_COUNT + 9, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            // Index is in Player inventory, move to Player hot bar
            } else if (!this.moveItemStackTo(itemstack1, SLOT_COUNT, SLOT_COUNT + 9, false)) {
                return ItemStack.EMPTY;
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
