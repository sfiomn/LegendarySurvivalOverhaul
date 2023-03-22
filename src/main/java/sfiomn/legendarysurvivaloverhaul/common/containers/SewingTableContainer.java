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
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.common.items.CoatItem;
import sfiomn.legendarysurvivaloverhaul.data.recipes.SewingRecipe;
import sfiomn.legendarysurvivaloverhaul.registry.BlockRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.ContainerRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.RecipeRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class SewingTableContainer extends Container {
    private final World world;
    private final IWorldPosCallable access;
    private final int SLOT_COUNT;
    @Nullable
    private SewingRecipe selectedRecipe;
    private final List<SewingRecipe> sewingRecipes;
    protected final CraftResultInventory resultSlot = new CraftResultInventory();
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
        this.sewingRecipes = this.world.getRecipeManager().getAllRecipesFor(RecipeRegistry.SEWING_RECIPE);

        addSlot(new Slot(inputSlots, 0, 18, 39));
        addSlot(new Slot(inputSlots, 1, 65, 39));

        addSlot(new Slot(resultSlot, 2, 134, 39) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            public ItemStack onTake(PlayerEntity player, ItemStack itemStack) {
                return SewingTableContainer.this.onTake(player, itemStack);
            }
        });

        this.SLOT_COUNT = this.inputSlots.getContainerSize() + this.resultSlot.getContainerSize();
        layoutPlayerInventorySlots(playerInventory, 8, 84);
    }

    public void slotsChanged(IInventory inventory) {
        super.slotsChanged(inventory);
        if (inventory == this.inputSlots) {
            this.createResult();
        }
    }

    public void createResult() {
        List<SewingRecipe> sewingRecipes = this.world.getRecipeManager().getRecipesFor(RecipeRegistry.SEWING_RECIPE, this.inputSlots, this.world);
        ItemStack itemStack = ItemStack.EMPTY;

        //  Proceed to the found recipe
        if (!sewingRecipes.isEmpty()) {
            this.selectedRecipe = sewingRecipes.get(0);
            itemStack = this.selectedRecipe.assemble(this.inputSlots);
            this.resultSlot.setRecipeUsed(this.selectedRecipe);

            //  Check a coat is possible
        } else if (isItemArmor(inputSlots.getItem(0)) && isItemCoat(inputSlots.getItem(1))) {
            //  Check coat effect not already applied on amor
            if (!Objects.equals(TemperatureUtil.getArmorCoatTag(inputSlots.getItem(0)),
                    ((CoatItem) (inputSlots.getItem(1).getItem())).coat.id())) {
                itemStack = this.inputSlots.getItem(0).copy();
                CoatItem coatItem = (CoatItem) inputSlots.getItem(1).getItem();
                TemperatureUtil.setArmorCoatTag(itemStack, coatItem.coat.id());
            }
        }

        this.resultSlot.setItem(0, itemStack);
        this.broadcastChanges();
    }

    private void shrinkStackInSlot(int index) {
        this.inputSlots.removeItem(index, 1);
    }

    protected ItemStack onTake(PlayerEntity player, ItemStack itemStack) {
        itemStack.onCraftedBy(player.level, player, itemStack.getCount());
        this.resultSlot.awardUsedRecipes(player);

        this.shrinkStackInSlot(0);
        this.shrinkStackInSlot(1);

        this.access.execute((world, pos) -> {
            world.playSound((PlayerEntity) null, pos, SoundRegistry.SEWING_TABLE.get(), SoundCategory.BLOCKS, 1.0f, 1.0f);
        });
        return itemStack;
    }

    public static boolean isItemArmor(ItemStack itemStack) {
        return itemStack.getItem() instanceof ArmorItem;
    }

    public static boolean isItemCoat(ItemStack itemStack) {
        return itemStack.getItem() instanceof CoatItem;
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
            } else if (!this.moveItemStackTo(itemstack1, 0, 2, false)) {
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
