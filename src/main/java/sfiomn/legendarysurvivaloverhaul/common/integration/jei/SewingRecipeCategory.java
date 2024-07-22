package sfiomn.legendarysurvivaloverhaul.common.integration.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.recipe.SewingRecipe;
import sfiomn.legendarysurvivaloverhaul.registry.BlockRegistry;

public class SewingRecipeCategory implements IRecipeCategory<SewingRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "sewing");
    public final static ResourceLocation TEXTURE = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/sewing_table_screen.png");

    public final static RecipeType<SewingRecipe> SEWING_RECIPE_TYPE = new RecipeType<>(UID, SewingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public SewingRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 10, 30, 156, 33);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockRegistry.SEWING_TABLE.get()));
    }

    @Override
    public RecipeType<SewingRecipe> getRecipeType() {
        return SEWING_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block." + LegendarySurvivalOverhaul.MOD_ID + ".sewing_table");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SewingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 8, 9).addIngredients(recipe.getBase());
        builder.addSlot(RecipeIngredientRole.INPUT, 55, 9).addIngredients(recipe.getAddition());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 124, 9).addItemStack(recipe.getResultItem(null));
    }
}
