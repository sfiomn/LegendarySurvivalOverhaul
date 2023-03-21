package sfiomn.legendarysurvivaloverhaul.common.integration.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.data.recipes.SewingRecipe;
import sfiomn.legendarysurvivaloverhaul.registry.BlockRegistry;

public class SewingRecipeCategory implements IRecipeCategory<SewingRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "sewing");
    public final static ResourceLocation TEXTURE = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/sewing_table_screen.png");

    private final IDrawable background;
    private final IDrawable icon;

    public SewingRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 10, 30, 156, 33);
        this.icon = helper.createDrawableIngredient(new ItemStack(BlockRegistry.SEWING_TABLE.get()));
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends SewingRecipe> getRecipeClass() {
        return SewingRecipe.class;
    }

    @Override
    public String getTitle() {
        return BlockRegistry.SEWING_TABLE.get().getName().getString();
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
    public void setIngredients(SewingRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, SewingRecipe recipe, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, 7, 8);
        recipeLayout.getItemStacks().init(1, true, 54, 8);

        recipeLayout.getItemStacks().init(2, false, 123, 8);
        recipeLayout.getItemStacks().set(ingredients);
    }
}
