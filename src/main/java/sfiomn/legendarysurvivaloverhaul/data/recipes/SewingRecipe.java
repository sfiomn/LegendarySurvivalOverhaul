package sfiomn.legendarysurvivaloverhaul.data.recipes;

import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;
import sfiomn.legendarysurvivaloverhaul.registry.RecipeRegistry;

import javax.annotation.Nullable;

public class SewingRecipe implements ISewingTableRecipe {

    private final ResourceLocation id;
    private final ItemStack result;
    private final Ingredient base;
    private final Ingredient addition;

    public SewingRecipe(ResourceLocation id, Ingredient base, Ingredient addition, ItemStack result) {
        this.id = id;
        this.base = base;
        this.addition = addition;
        this.result = result;
    }

    @Override
    public boolean matches(IInventory inv, World world) {
        return base.test(inv.getItem(0)) && addition.test(inv.getItem(1));
    }

    @Override
    public ItemStack assemble(IInventory inventory) {
        ItemStack itemstack = this.result.copy();
        CompoundNBT compoundnbt = inventory.getItem(0).getTag();
        if (compoundnbt != null) {
            itemstack.setTag(compoundnbt.copy());
        }

        return itemstack;
    }

    @Override
    public boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_) {
        return false;
    }

    @Override
    public ItemStack getResultItem() {
        return result.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeRegistry.SEWING_SERIALIZER.get();
    }

    public static class SewingRecipeType implements IRecipeType<SewingRecipe> {
        @Override
        public String toString() {
            return SewingRecipe.TYPE_ID.toString();
        }
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<SewingRecipe> {

        //  Read Json files and convert it in Recipes
        @Override
        public SewingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            Ingredient base = Ingredient.fromJson(JSONUtils.getAsJsonObject(json, "base"));
            Ingredient addition = Ingredient.fromJson(JSONUtils.getAsJsonObject(json, "addition"));
            ItemStack result = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));

            return new SewingRecipe(recipeId, base, addition, result);
        }

        @Nullable
        @Override
        public SewingRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            Ingredient base = Ingredient.fromNetwork(buffer);
            Ingredient addition = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();

            return new SewingRecipe(recipeId, base, addition, result);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, SewingRecipe recipe) {
            recipe.base.toNetwork(buffer);
            recipe.addition.toNetwork(buffer);
            buffer.writeItem(recipe.getResultItem());
        }
    }
}
