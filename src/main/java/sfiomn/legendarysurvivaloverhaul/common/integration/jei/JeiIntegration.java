package sfiomn.legendarysurvivaloverhaul.common.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.client.screens.SewingTableScreen;
import sfiomn.legendarysurvivaloverhaul.common.items.drink.CanteenItem;
import sfiomn.legendarysurvivaloverhaul.common.recipe.PurificationBlastingRecipe;
import sfiomn.legendarysurvivaloverhaul.common.recipe.PurificationSmeltingRecipe;
import sfiomn.legendarysurvivaloverhaul.common.recipe.SewingRecipe;
import sfiomn.legendarysurvivaloverhaul.registry.BlockRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.RecipeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static net.minecraft.world.item.crafting.RecipeType.BLASTING;
import static net.minecraft.world.item.crafting.RecipeType.SMELTING;

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
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        Level world = Minecraft.getInstance().level;
        if (world != null) {
            RecipeManager rm = world.getRecipeManager();
            registration.addRecipes(SewingRecipeCategory.SEWING_RECIPE_TYPE, rm.getAllRecipesFor(SewingRecipe.Type.INSTANCE).stream()
                    .filter(Objects::nonNull).collect(Collectors.toList()));

            registration.addRecipes(RecipeTypes.SMELTING, transferToSmeltingRecipe(rm.getAllRecipesFor(SMELTING).stream()
                    .filter(r -> r instanceof PurificationSmeltingRecipe).collect(Collectors.toList())));

            registration.addRecipes(RecipeTypes.BLASTING, transferToBlastingRecipe(rm.getAllRecipesFor(BLASTING).stream()
                    .filter(r -> r instanceof PurificationBlastingRecipe).collect(Collectors.toList())));

            registration.addRecipes(SewingRecipeCategory.SEWING_RECIPE_TYPE, customSewingRecipes());
        }
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(SewingTableScreen.class, 90, 36, 20, 20, SewingRecipeCategory.SEWING_RECIPE_TYPE);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.SEWING_TABLE.get()), SewingRecipeCategory.SEWING_RECIPE_TYPE);
    }

    private ArrayList<SewingRecipe> customSewingRecipes() {
        ArrayList<SewingRecipe> sewingRecipes = new ArrayList<>();

        for (Item item: ForgeRegistries.ITEMS) {
            ResourceLocation itemRegistryName = ForgeRegistries.ITEMS.getKey(item);
            if (item instanceof ArmorItem && itemRegistryName != null) {
                sewingRecipes.add(getCoatRecipe(itemRegistryName.getPath() + "_cooling_coat_1", item, ItemRegistry.COOLING_COAT_1.get()));
                sewingRecipes.add(getCoatRecipe(itemRegistryName.getPath() + "_cooling_coat_2", item, ItemRegistry.COOLING_COAT_2.get()));
                sewingRecipes.add(getCoatRecipe(itemRegistryName.getPath() + "_cooling_coat_3", item, ItemRegistry.COOLING_COAT_3.get()));
                sewingRecipes.add(getCoatRecipe(itemRegistryName.getPath() + "_heating_coat_1", item, ItemRegistry.HEATING_COAT_1.get()));
                sewingRecipes.add(getCoatRecipe(itemRegistryName.getPath() + "_heating_coat_2", item, ItemRegistry.HEATING_COAT_2.get()));
                sewingRecipes.add(getCoatRecipe(itemRegistryName.getPath() + "_heating_coat_3", item, ItemRegistry.HEATING_COAT_3.get()));
                sewingRecipes.add(getCoatRecipe(itemRegistryName.getPath() + "_thermal_coat_1", item, ItemRegistry.THERMAL_COAT_1.get()));
                sewingRecipes.add(getCoatRecipe(itemRegistryName.getPath() + "_thermal_coat_2", item, ItemRegistry.THERMAL_COAT_2.get()));
                sewingRecipes.add(getCoatRecipe(itemRegistryName.getPath() + "_thermal_coat_3", item, ItemRegistry.THERMAL_COAT_3.get()));
            }
        }

        return sewingRecipes;
    }

    private SewingRecipe getCoatRecipe(String id, Item base, Item addition) {
        return new SewingRecipe(
                Ingredient.of(base),
                Ingredient.of(addition),
                new ItemStack(base),
                new ResourceLocation(id)
        );
    }

    private ArrayList<SmeltingRecipe> transferToSmeltingRecipe(List<SmeltingRecipe> canteenSmeltingRecipes) {
        ArrayList<SmeltingRecipe> furnaceRecipes = new ArrayList<>();
        for (SmeltingRecipe recipe: canteenSmeltingRecipes) {
            ItemStack input = recipe.getIngredients().get(0).getItems()[0];
            int maxThirstCapacity = 0;
            if (input.getItem() instanceof CanteenItem) {
                maxThirstCapacity = ((CanteenItem) input.getItem()).getMaxCapacity();
            }
            ThirstUtil.setCapacityTag(input, maxThirstCapacity);
            furnaceRecipes.add(
                    new SmeltingRecipe(
                            new ResourceLocation(recipe.getId() + "_smelting"),
                            recipe.getGroup(),
                            CookingBookCategory.MISC,
                            Ingredient.of(input),
                            recipe.getResultItem(null),
                            recipe.getExperience(),
                            recipe.getCookingTime()));
        }
        return furnaceRecipes;
    }

    private ArrayList<BlastingRecipe> transferToBlastingRecipe(List<BlastingRecipe> canteenBlastingRecipes) {
        ArrayList<BlastingRecipe> blastingRecipes = new ArrayList<>();
        for (BlastingRecipe recipe: canteenBlastingRecipes) {
            ItemStack input = recipe.getIngredients().get(0).getItems()[0];
            int maxThirstCapacity = 0;
            if (input.getItem() instanceof CanteenItem) {
                maxThirstCapacity = ((CanteenItem) input.getItem()).getMaxCapacity();
            }
            ThirstUtil.setCapacityTag(input, maxThirstCapacity);
            blastingRecipes.add(new BlastingRecipe(new ResourceLocation(recipe.getId() + "_blasting"),
                    recipe.getGroup(),
                    CookingBookCategory.MISC,
                    Ingredient.of(input),
                    recipe.getResultItem(null),
                    recipe.getExperience(),
                    recipe.getCookingTime()));
        }
        return blastingRecipes;
    }
}
