package sfiomn.legendarysurvivaloverhaul.common.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.common.items.drink.CanteenItem;
import sfiomn.legendarysurvivaloverhaul.registry.RecipeRegistry;

public class CanteenBlastingRecipe extends BlastingRecipe {
    public CanteenBlastingRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
        super(id, group, CookingBookCategory.MISC, ingredient, result, experience, cookingTime);
    }

    @Override
    public boolean isSpecial() {
        //  Avoid the recipe to be displayed in recipe book because of unknown sewing recipe category
        return true;
    }

    @Override
    public boolean matches(Container inventory, @NotNull Level level) {
        return this.ingredient.test(inventory.getItem(0)) && ThirstUtil.getCapacityTag(inventory.getItem(0)) > 0;
    }

    @Override
    public @NotNull ItemStack assemble(Container inventory, @NotNull RegistryAccess access) {
        int hydrationCapacity = ThirstUtil.getCapacityTag(inventory.getItem(0));
        ItemStack result = this.result.copy();
        ThirstUtil.setHydrationEnumTag(result, HydrationEnum.PURIFIED);
        ThirstUtil.setCapacityTag(result, hydrationCapacity);
        return result;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess access) {
        ItemStack result = this.result.copy();
        int maxHydrationCapacity = 0;
        if (this.result.getItem() instanceof CanteenItem resultItem) {
            maxHydrationCapacity = resultItem.getMaxCapacity();
        }
        ThirstUtil.setHydrationEnumTag(result, HydrationEnum.PURIFIED);
        ThirstUtil.setCapacityTag(result, maxHydrationCapacity);
        return result;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.CANTEEN_BLASTING_SERIALIZER.get();
    }

    public static class CanteenRecipeSerializer implements RecipeSerializer<CanteenBlastingRecipe> {
        private final int defaultCookingTime;

        public CanteenRecipeSerializer(int cookingTime) {
            this.defaultCookingTime = cookingTime;
        }

        public CanteenBlastingRecipe fromJson(ResourceLocation id, JsonObject jsonRecipe) {
            String s = GsonHelper.getAsString(jsonRecipe, "group", "");
            JsonElement jsonelement = (JsonElement)(GsonHelper.isArrayNode(jsonRecipe, "ingredient") ? GsonHelper.getAsJsonArray(jsonRecipe, "ingredient") : GsonHelper.getAsJsonObject(jsonRecipe, "ingredient"));
            Ingredient ingredient = Ingredient.fromJson(jsonelement);
            //Forge: Check if primitive string to keep vanilla or a object which can contain a count field.
            if (!jsonRecipe.has("result"))
                throw new JsonSyntaxException("Missing result, expected to find a string or object");
            ItemStack itemstack;
            if (jsonRecipe.get("result").isJsonObject())
                itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonRecipe, "result"));
            else {
                String s1 = GsonHelper.getAsString(jsonRecipe, "result");
                ResourceLocation resourcelocation = new ResourceLocation(s1);
                Item itemRegistry = ForgeRegistries.ITEMS.getValue(resourcelocation);
                if (itemRegistry == null)
                    throw new IllegalStateException("Item: " + s1 + " does not exist");
                itemstack = new ItemStack(itemRegistry);
            }
            float f = GsonHelper.getAsFloat(jsonRecipe, "experience", 0.0F);
            int i = GsonHelper.getAsInt(jsonRecipe, "cookingtime", this.defaultCookingTime);
            return new CanteenBlastingRecipe(id, s, ingredient, itemstack, f, i);
        }

        public CanteenBlastingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            String s = buffer.readUtf(32767);
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack itemstack = buffer.readItem();
            float f = buffer.readFloat();
            int i = buffer.readVarInt();
            return new CanteenBlastingRecipe(id, s, ingredient, itemstack, f, i);
        }

        public void toNetwork(FriendlyByteBuf buffer, CanteenBlastingRecipe recipe) {
            buffer.writeUtf(recipe.group);
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
            buffer.writeFloat(recipe.experience);
            buffer.writeVarInt(recipe.cookingTime);
        }
    }
}
