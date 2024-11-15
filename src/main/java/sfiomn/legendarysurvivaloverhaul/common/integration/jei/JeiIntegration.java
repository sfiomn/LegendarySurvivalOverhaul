package sfiomn.legendarysurvivaloverhaul.common.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.client.screens.SewingTableScreen;
import sfiomn.legendarysurvivaloverhaul.common.items.CoatItem;
import sfiomn.legendarysurvivaloverhaul.common.items.drink.CanteenItem;
import sfiomn.legendarysurvivaloverhaul.data.recipes.CanteenBlastingRecipe;
import sfiomn.legendarysurvivaloverhaul.data.recipes.CanteenFurnaceRecipe;
import sfiomn.legendarysurvivaloverhaul.data.recipes.SewingRecipe;
import sfiomn.legendarysurvivaloverhaul.registry.BlockRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.RecipeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static net.minecraft.item.crafting.IRecipeType.BLASTING;
import static net.minecraft.item.crafting.IRecipeType.SMELTING;

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
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(SewingTableScreen.class, 40, 37, 20, 20, SewingRecipeCategory.UID);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        World world = Minecraft.getInstance().level;
        if (world != null) {
            RecipeManager rm = world.getRecipeManager();
            registration.addRecipes(rm.getAllRecipesFor(RecipeRegistry.SEWING_RECIPE).stream()
                    .filter(Objects::nonNull).collect(Collectors.toList()), SewingRecipeCategory.UID);

            registration.addRecipes(transferToFurnaceRecipe(rm.getAllRecipesFor(SMELTING).stream()
                    .filter(r -> r instanceof CanteenFurnaceRecipe).collect(Collectors.toList())), VanillaRecipeCategoryUid.FURNACE);

            registration.addRecipes(transferToBlastingRecipe(rm.getAllRecipesFor(BLASTING).stream()
                    .filter(r -> r instanceof CanteenBlastingRecipe).collect(Collectors.toList())), VanillaRecipeCategoryUid.BLASTING);


            registration.addRecipes(sewingCoatRecipes(), SewingRecipeCategory.UID);
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.SEWING_TABLE.get()), SewingRecipeCategory.UID);
    }

    private ArrayList<SewingRecipe> sewingCoatRecipes() {
        ArrayList<SewingRecipe> sewingRecipes = new ArrayList<>();

        for (Item item: ForgeRegistries.ITEMS) {
            if (item instanceof ArmorItem && ForgeRegistries.ITEMS.getKey(item) != null) {
                ResourceLocation itemArmorRegistryName = ForgeRegistries.ITEMS.getKey(item);
                for (RegistryObject<Item> modItem : ItemRegistry.ITEMS.getEntries()) {
                    if (modItem.get() instanceof CoatItem && itemArmorRegistryName != null) {
                        ItemStack result = new ItemStack(item);
                        TemperatureUtil.setArmorCoatTag(result, ((CoatItem) modItem.get()).coat.id());
                        sewingRecipes.add(
                                getCoatRecipe(
                                        "sewing_" + itemArmorRegistryName.getPath() + "_" + modItem.getId().getPath(),
                                        item,
                                        modItem.get(),
                                        result
                                ));
                    }
                }
            }
        }

        return sewingRecipes;
    }

    private SewingRecipe getCoatRecipe(String id, Item base, Item addition, ItemStack result) {
        return new SewingRecipe(
                new ResourceLocation(id),
                Ingredient.of(base),
                Ingredient.of(addition),
                result

        );
    }

    private ArrayList<FurnaceRecipe> transferToFurnaceRecipe(List<FurnaceRecipe> canteenFurnaceRecipes) {
        ArrayList<FurnaceRecipe> furnaceRecipes = new ArrayList<>();
        for (FurnaceRecipe recipe: canteenFurnaceRecipes) {
            ItemStack input = recipe.getIngredients().get(0).getItems()[0];
            int maxThirstCapacity = 0;
            if (input.getItem() instanceof CanteenItem) {
                maxThirstCapacity = ((CanteenItem) input.getItem()).getMaxCapacity();
            }
            ThirstUtil.setCapacityTag(input, maxThirstCapacity);
            furnaceRecipes.add(new FurnaceRecipe(new ResourceLocation(recipe.getId() + "_furnace"),
                    recipe.getGroup(),
                    Ingredient.of(input),
                    recipe.getResultItem(),
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
                    Ingredient.of(input),
                    recipe.getResultItem(),
                    recipe.getExperience(),
                    recipe.getCookingTime()));
        }
        return blastingRecipes;
    }
}
