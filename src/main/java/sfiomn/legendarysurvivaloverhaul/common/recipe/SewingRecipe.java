package sfiomn.legendarysurvivaloverhaul.common.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

public class SewingRecipe implements Recipe<SimpleContainer> {
    private final Ingredient base;
    private final Ingredient addition;
    private final ItemStack result;
    private final ResourceLocation id;

    public SewingRecipe(Ingredient base, Ingredient addition, ItemStack result, ResourceLocation id) {
        this.base = base;
        this.addition = addition;
        this.result = result;
        this.id = id;
    }

    public Ingredient getBase() {
        return base;
    }

    public Ingredient getAddition() {
        return addition;
    }

    @Override
    public boolean matches(@NotNull SimpleContainer simpleContainer, Level level) {
        if (level.isClientSide)
            return false;

        return base.test(simpleContainer.getItem(0)) &&
                addition.test(simpleContainer.getItem(1));
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull SimpleContainer simpleContainer, @NotNull RegistryAccess registryAccess) {
        ItemStack itemstack = this.result.copy();

        CompoundTag compoundTag = simpleContainer.getItem(0).getTag();
        if (compoundTag != null) {
            itemstack.setTag(compoundTag.copy());
        }

        return itemstack;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {

        return result.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<SewingRecipe> {
        public static final Type INSTANCE = new Type();
        public static final ResourceLocation ID = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "sewing");
    }

    public static class Serializer implements RecipeSerializer<SewingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "sewing");


        @Override
        public @NotNull SewingRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject jsonObject) {
            Ingredient base = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject, "base"));
            Ingredient addition = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject, "addition"));
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));

            return new SewingRecipe(base, addition, result, recipeId);
        }

        @Override
        public @Nullable SewingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf friendlyByteBuf) {
            Ingredient base = Ingredient.fromNetwork(friendlyByteBuf);
            Ingredient addition = Ingredient.fromNetwork(friendlyByteBuf);
            ItemStack result = friendlyByteBuf.readItem();

            return new SewingRecipe(base, addition, result, recipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, SewingRecipe sewingRecipe) {
            sewingRecipe.base.toNetwork(friendlyByteBuf);
            sewingRecipe.addition.toNetwork(friendlyByteBuf);
            friendlyByteBuf.writeItem(sewingRecipe.result);
        }
    }
}
