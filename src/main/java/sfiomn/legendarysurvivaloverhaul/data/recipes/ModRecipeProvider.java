package sfiomn.legendarysurvivaloverhaul.data.recipes;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.items.CoatItem;

import java.util.Collection;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        Collection<Item> items = ForgeRegistries.ITEMS.getValues();
        for (Item itemArmor : items) {
            if (itemArmor instanceof ArmorItem) {
                for (Item itemCoat : items) {
                    if (itemCoat instanceof CoatItem) {
                        try {
                            SewingRecipeBuilder.sewingRecipe(Ingredient.of(itemArmor), Ingredient.of(itemCoat), itemArmor).unlocks("has_coat", has(itemCoat)).save(consumer, itemArmor.getRegistryName() + "_" + itemCoat.getRegistryName().getPath());
                        } catch (RuntimeException e) {
                            LegendarySurvivalOverhaul.LOGGER.error("Failed to register armor coat recipe for ingredient: {}", itemArmor, e);
                        }
                    }
                }
            }
        }
    }
}
