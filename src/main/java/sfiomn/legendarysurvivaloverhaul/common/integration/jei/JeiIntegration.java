package sfiomn.legendarysurvivaloverhaul.common.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.data.recipes.SewingRecipe;
import sfiomn.legendarysurvivaloverhaul.registry.BlockRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.RecipeRegistry;

import java.util.ArrayList;
import java.util.stream.Collectors;

@JeiPlugin
public class JeiIntegration implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new SewingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        World world = Minecraft.getInstance().level;
        if (world != null) {
            RecipeManager rm = world.getRecipeManager();
            registration.addRecipes(rm.getAllRecipesFor(RecipeRegistry.SEWING_RECIPE).stream()
                    .filter(r -> r instanceof SewingRecipe).collect(Collectors.toList()), SewingRecipeCategory.UID);

            registration.addRecipes(customSewingRecipes(), SewingRecipeCategory.UID);
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.SEWING_TABLE.get()), SewingRecipeCategory.UID);
    }

    private ArrayList<SewingRecipe> customSewingRecipes() {
        ArrayList<SewingRecipe> sewingRecipes = new ArrayList<>();

        for (Item item: ForgeRegistries.ITEMS) {
            if (item instanceof ArmorItem && item.getRegistryName() != null) {
                sewingRecipes.add(getCoatRecipe("_cooling_coat_1", item, ItemRegistry.COOLING_COAT_1.get()));
                sewingRecipes.add(getCoatRecipe("_cooling_coat_2", item, ItemRegistry.COOLING_COAT_2.get()));
                sewingRecipes.add(getCoatRecipe("_cooling_coat_3", item, ItemRegistry.COOLING_COAT_3.get()));
                sewingRecipes.add(getCoatRecipe("_heating_coat_1", item, ItemRegistry.HEATING_COAT_1.get()));
                sewingRecipes.add(getCoatRecipe("_heating_coat_2", item, ItemRegistry.HEATING_COAT_2.get()));
                sewingRecipes.add(getCoatRecipe("_heating_coat_3", item, ItemRegistry.HEATING_COAT_3.get()));
                sewingRecipes.add(getCoatRecipe("_thermal_coat_1", item, ItemRegistry.THERMAL_COAT_1.get()));
                sewingRecipes.add(getCoatRecipe("_thermal_coat_2", item, ItemRegistry.THERMAL_COAT_2.get()));
                sewingRecipes.add(getCoatRecipe("_thermal_coat_3", item, ItemRegistry.THERMAL_COAT_3.get()));
            }
        }

        return sewingRecipes;
    }

    private SewingRecipe getCoatRecipe(String id, Item base, Item addition) {
        return new SewingRecipe(
                new ResourceLocation(base.getRegistryName().getPath() + id),
                Ingredient.of(base),
                Ingredient.of(addition),
                new ItemStack(base)
        );
    }
}
