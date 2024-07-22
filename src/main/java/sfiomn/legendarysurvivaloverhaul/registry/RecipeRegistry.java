package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.recipe.PurificationBlastingRecipe;
import sfiomn.legendarysurvivaloverhaul.common.recipe.PurificationSmeltingRecipe;
import sfiomn.legendarysurvivaloverhaul.common.recipe.SewingRecipe;

public class RecipeRegistry {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, LegendarySurvivalOverhaul.MOD_ID);

    public static final RegistryObject<RecipeSerializer<SewingRecipe>> SEWING_SERIALIZER = RECIPE_SERIALIZERS.register("sewing", () -> SewingRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<PurificationSmeltingRecipe>> PURIFICATION_SMELTING_SERIALIZER = RECIPE_SERIALIZERS.register("purification_smelting", () -> PurificationSmeltingRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<PurificationBlastingRecipe>> PURIFICATION_BLASTING_SERIALIZER = RECIPE_SERIALIZERS.register("purification_blasting", () -> PurificationBlastingRecipe.Serializer.INSTANCE);

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPE = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, LegendarySurvivalOverhaul.MOD_ID);

    public static final RegistryObject<RecipeType<SewingRecipe>> SEWING_RECIPE = RECIPE_TYPE.register("sewing", () -> RecipeType.simple(SewingRecipe.Type.ID));

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
        RECIPE_TYPE.register(eventBus);
    }
}
