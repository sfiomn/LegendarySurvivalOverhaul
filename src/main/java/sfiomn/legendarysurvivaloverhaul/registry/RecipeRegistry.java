package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.data.recipes.CanteenBlastingRecipe;
import sfiomn.legendarysurvivaloverhaul.data.recipes.CanteenFurnaceRecipe;
import sfiomn.legendarysurvivaloverhaul.data.recipes.SewingRecipe;

public class RecipeRegistry {

    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, LegendarySurvivalOverhaul.MOD_ID);

    public static final RegistryObject<SewingRecipe.Serializer> SEWING_SERIALIZER = RECIPE_SERIALIZERS.register("sewing", SewingRecipe.Serializer::new);
    public static final RegistryObject<CanteenFurnaceRecipe.CanteenRecipeSerializer> CANTEEN_SMELTING_SERIALIZER = RECIPE_SERIALIZERS.register("canteen_smelting", () -> new CanteenFurnaceRecipe.CanteenRecipeSerializer(100));
    public static final RegistryObject<CanteenBlastingRecipe.CanteenRecipeSerializer> CANTEEN_BLASTING_SERIALIZER = RECIPE_SERIALIZERS.register("canteen_blasting", () -> new CanteenBlastingRecipe.CanteenRecipeSerializer(100));

    public static final IRecipeType<SewingRecipe> SEWING_RECIPE = new SewingRecipe.SewingRecipeType();

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);

        Registry.register(Registry.RECIPE_TYPE, SewingRecipe.TYPE_ID, SEWING_RECIPE);
    }
}
