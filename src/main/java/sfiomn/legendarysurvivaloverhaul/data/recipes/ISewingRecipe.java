package sfiomn.legendarysurvivaloverhaul.data.recipes;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

public interface ISewingRecipe extends IRecipe<IInventory> {
    ResourceLocation TYPE_ID = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "sewing");

    @Override
    default IRecipeType<?> getType() {
        return Registry.RECIPE_TYPE.getOptional(TYPE_ID).get();
    }
}
