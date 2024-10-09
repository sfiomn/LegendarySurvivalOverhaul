package sfiomn.legendarysurvivaloverhaul.common.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.client.screens.SewingTableScreen;
import sfiomn.legendarysurvivaloverhaul.common.items.CoatItem;
import sfiomn.legendarysurvivaloverhaul.common.recipe.SewingRecipe;
import sfiomn.legendarysurvivaloverhaul.registry.BlockRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

import static sfiomn.legendarysurvivaloverhaul.util.internal.ThirstUtilInternal.HYDRATION_ENUM_TAG;

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
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(ItemRegistry.CANTEEN.get(), CanteenSubtypeInterpreter.INSTANCE);
        registration.registerSubtypeInterpreter(ItemRegistry.LARGE_CANTEEN.get(), CanteenSubtypeInterpreter.INSTANCE);
        IModPlugin.super.registerItemSubtypes(registration);
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        Level world = Minecraft.getInstance().level;
        if (world != null) {
            RecipeManager rm = world.getRecipeManager();
            registration.addRecipes(SewingRecipeCategory.SEWING_RECIPE_TYPE, rm.getAllRecipesFor(SewingRecipe.Type.INSTANCE).stream()
                    .filter(Objects::nonNull).collect(Collectors.toList()));
        }

        registration.addRecipes(SewingRecipeCategory.SEWING_RECIPE_TYPE, sewingCoatRecipes());
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(SewingTableScreen.class, 40, 37, 20, 20, SewingRecipeCategory.SEWING_RECIPE_TYPE);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.SEWING_TABLE.get()), SewingRecipeCategory.SEWING_RECIPE_TYPE);
    }

    public static class CanteenSubtypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {
        public static final CanteenSubtypeInterpreter INSTANCE = new CanteenSubtypeInterpreter();

        private CanteenSubtypeInterpreter() {
        }

        public String apply(ItemStack itemStack, UidContext context) {
            if (!itemStack.hasTag() || !Objects.requireNonNull(itemStack.getTag()).contains(HYDRATION_ENUM_TAG)) {
                return "";
            } else {
                return itemStack.getTag().getString(HYDRATION_ENUM_TAG);
            }
        }
    }

    private ArrayList<SewingRecipe> sewingCoatRecipes() {
        ArrayList<SewingRecipe> sewingRecipes = new ArrayList<>();

        for (Item item: ForgeRegistries.ITEMS) {
            if (item instanceof ArmorItem itemArmor && ForgeRegistries.ITEMS.getKey(itemArmor) != null) {
                ResourceLocation itemArmorRegistryName = ForgeRegistries.ITEMS.getKey(itemArmor);
                for (RegistryObject<Item> modItem : ItemRegistry.ITEMS.getEntries()) {
                    if (modItem.get() instanceof CoatItem itemCoat && itemArmorRegistryName != null) {
                        ItemStack result = new ItemStack(itemArmor);
                        TemperatureUtil.setArmorCoatTag(result, itemCoat.coat.id());
                        sewingRecipes.add(
                                getCoatRecipe(
                                        "sewing_" + itemArmorRegistryName.getPath() + "_" + modItem.getId().getPath(),
                                        itemArmor,
                                        itemCoat,
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
                Ingredient.of(base),
                Ingredient.of(addition),
                result,
                new ResourceLocation(id)
        );
    }
}
