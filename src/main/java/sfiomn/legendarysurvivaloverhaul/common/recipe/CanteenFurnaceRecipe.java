package sfiomn.legendarysurvivaloverhaul.common.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.common.items.drink.CanteenItem;
import sfiomn.legendarysurvivaloverhaul.registry.RecipeRegistry;

public class CanteenFurnaceRecipe extends SmeltingRecipe {
    public CanteenFurnaceRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
        super(id, group, CookingBookCategory.MISC, ingredient, result, experience, cookingTime);
    }

    @Override
    public boolean isSpecial() {
        //  Avoid the recipe to be displayed in recipe book because of unknown sewing recipe category
        return true;
    }

    public boolean matches(Inventory inventory, Level level) {
        return this.ingredient.test(inventory.getItem(0)) && ThirstUtil.getCapacityTag(inventory.getItem(0)) > 0;
    }

    public ItemStack assemble(Inventory inventory) {
        int hydrationCapacity = ThirstUtil.getCapacityTag(inventory.getItem(0));
        ItemStack result = this.result.copy();
        ThirstUtil.setHydrationEnumTag(result, HydrationEnum.PURIFIED);
        ThirstUtil.setCapacityTag(result, hydrationCapacity);
        return result;
    }

    public ItemStack getResultItem() {
        ItemStack result = this.result.copy();
        int maxHydrationCapacity = 0;
        if (this.result.getItem() instanceof CanteenItem) {
            CanteenItem resultItem = (CanteenItem) this.result.getItem();
            maxHydrationCapacity = resultItem.getMaxCapacity();
        }
        ThirstUtil.setHydrationEnumTag(result, HydrationEnum.PURIFIED);
        ThirstUtil.setCapacityTag(result, maxHydrationCapacity);
        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.CANTEEN_SMELTING_SERIALIZER.get();
    }

    public static class CanteenRecipeSerializer implements RecipeSerializer<CanteenFurnaceRecipe> {
        private final int defaultCookingTime;

        public CanteenRecipeSerializer(int cookingTime) {
            this.defaultCookingTime = cookingTime;
        }

        public CanteenFurnaceRecipe fromJson(ResourceLocation id, JsonObject jsonRecipe) {
            String s = GsonHelper.getAsString(jsonRecipe, "group", "");
            JsonElement jsonelement = (JsonElement)(GsonHelper.isArrayNode(jsonRecipe, "ingredient") ? GsonHelper.getAsJsonArray(jsonRecipe, "ingredient") : GsonHelper.getAsJsonObject(jsonRecipe, "ingredient"));
            Ingredient ingredient = Ingredient.fromJson(jsonelement);
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
            return new CanteenFurnaceRecipe(id, s, ingredient, itemstack, f, i);
        }

        public CanteenFurnaceRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            String s = buffer.readUtf(32767);
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack itemstack = buffer.readItem();
            float f = buffer.readFloat();
            int i = buffer.readVarInt();
            return new CanteenFurnaceRecipe(id, s, ingredient, itemstack, f, i);
        }

        public void toNetwork(FriendlyByteBuf buffer, CanteenFurnaceRecipe recipe) {
            buffer.writeUtf(recipe.group);
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
            buffer.writeFloat(recipe.experience);
            buffer.writeVarInt(recipe.cookingTime);
        }
    }
}
