package sfiomn.legendarysurvivaloverhaul.common.containers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.common.items.CoatItem;
import sfiomn.legendarysurvivaloverhaul.common.recipe.SewingRecipe;
import sfiomn.legendarysurvivaloverhaul.registry.BlockRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.ContainerRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.RecipeRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SewingTableContainer extends ItemCombinerMenu {
    public static final int INPUT_SLOT = 0;
    public static final int ADDITIONAL_SLOT = 1;
    public static final int RESULT_SLOT = 2;
    @Nullable
    private SewingRecipe selectedRecipe;

    //  Constructor specified in registry
    public SewingTableContainer(int windowId, Inventory playerInventory, FriendlyByteBuf data) {
        this(windowId, playerInventory, ContainerLevelAccess.NULL);
    }

    //  Constructor specified in Sewing Table block
    public SewingTableContainer(int windowId, Inventory playerInventory, ContainerLevelAccess access) {
        super(ContainerRegistry.SEWING_TABLE_CONTAINER.get(), windowId, playerInventory, access);
    }

    @Override
    protected @NotNull ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create()
                .withSlot(INPUT_SLOT, 18, 39, (itemStack) -> true)
                .withSlot(ADDITIONAL_SLOT, 65, 39, (itemStack) -> true)
                .withResultSlot(RESULT_SLOT, 134, 39).build();
    }

    private List<SewingRecipe> getCurrentRecipe() {
        SimpleContainer inputSlots = new SimpleContainer(this.inputSlots.getContainerSize());
        for (int i=0; i<this.inputSlots.getContainerSize(); i++) {
            inputSlots.addItem(this.inputSlots.getItem(i));
        }
        return this.player.level().getRecipeManager().getRecipesFor(RecipeRegistry.SEWING_RECIPE.get(), inputSlots, this.player.level());
    }

    @Override
    public void createResult() {
        List<SewingRecipe> sewingRecipes = getCurrentRecipe();
        ItemStack itemStack = ItemStack.EMPTY;

        //  Proceed to the found recipe
        if (!sewingRecipes.isEmpty()) {
            this.selectedRecipe = sewingRecipes.get(0);
            itemStack = this.selectedRecipe.getResultItem(null);
            this.resultSlots.setRecipeUsed(this.selectedRecipe);

        //  Check a coat is possible
        } else if (isItemArmor(inputSlots.getItem(INPUT_SLOT)) && isItemCoat(inputSlots.getItem(1))) {
            //  Check coat effect not already applied on amor
            if (!Objects.equals(TemperatureUtil.getArmorCoatTag(inputSlots.getItem(0)),
                    ((CoatItem) (inputSlots.getItem(ADDITIONAL_SLOT).getItem())).coat.id())) {
                itemStack = this.inputSlots.getItem(INPUT_SLOT).copy();
                CoatItem coatItem = (CoatItem) inputSlots.getItem(ADDITIONAL_SLOT).getItem();
                TemperatureUtil.setArmorCoatTag(itemStack, coatItem.coat.id());
            }
        }

        this.resultSlots.setItem(0, itemStack);
        this.broadcastChanges();
    }

    private void shrinkStackInSlot(int index) {
        this.inputSlots.removeItem(index, 1);
    }

    @Override
    protected boolean mayPickup(Player player, boolean b) {
        return true;
    }

    protected void onTake(Player player, ItemStack itemStack) {
        itemStack.onCraftedBy(player.level(), player, itemStack.getCount());
        this.resultSlots.awardUsedRecipes(player, Collections.singletonList(itemStack));

        this.shrinkStackInSlot(INPUT_SLOT);
        this.shrinkStackInSlot(ADDITIONAL_SLOT);

        this.player.level().playSound((Player) null, player.blockPosition(), SoundRegistry.SEWING_TABLE.get(), SoundSource.BLOCKS);
    }

    @Override
    protected boolean isValidBlock(BlockState blockState) {
        return blockState.is(BlockRegistry.SEWING_TABLE.get());
    }

    public static boolean isItemArmor(ItemStack itemStack) {
        return itemStack.getItem() instanceof ArmorItem;
    }

    public static boolean isItemCoat(ItemStack itemStack) {
        return itemStack.getItem() instanceof CoatItem;
    }
}
