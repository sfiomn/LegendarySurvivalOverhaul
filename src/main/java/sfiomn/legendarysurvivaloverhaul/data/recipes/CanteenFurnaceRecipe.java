package sfiomn.legendarysurvivaloverhaul.data.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstEnum;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.common.items.CanteenItem;
import sfiomn.legendarysurvivaloverhaul.registry.RecipeRegistry;

public class CanteenFurnaceRecipe extends FurnaceRecipe {
    public CanteenFurnaceRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
        super(id, group, ingredient, result, experience, cookingTime);
    }

    @Override
    public boolean isSpecial() {
        //  Avoid the recipe to be displayed in recipe book because of unknown sewing recipe category
        return true;
    }

    public boolean matches(IInventory inventory, World world) {
        return this.ingredient.test(inventory.getItem(0)) && ThirstUtil.getCapacityTag(inventory.getItem(0).getStack()) > 0;
    }

    public ItemStack assemble(IInventory inventory) {
        int thirstCapacity = ThirstUtil.getCapacityTag(inventory.getItem(0));
        ItemStack result = this.result.copy();
        ThirstUtil.setThirstEnumTag(result, ThirstEnum.PURIFIED);
        ThirstUtil.setCapacityTag(result, thirstCapacity);
        return result;
    }

    public ItemStack getResultItem() {
        ItemStack result = this.result.copy();
        int maxThirstCapacity = 0;
        if (this.result.getItem() instanceof CanteenItem) {
            CanteenItem resultItem = (CanteenItem) this.result.getItem();
            maxThirstCapacity = resultItem.getMaxCapacity();
        }
        ThirstUtil.setThirstEnumTag(result, ThirstEnum.PURIFIED);
        ThirstUtil.setCapacityTag(result, maxThirstCapacity);
        return result;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeRegistry.CANTEEN_SMELTING_SERIALIZER.get();
    }

    public static class CanteenRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CanteenFurnaceRecipe> {
        private final int defaultCookingTime;

        public CanteenRecipeSerializer(int cookingTime) {
            this.defaultCookingTime = cookingTime;
        }

        public CanteenFurnaceRecipe fromJson(ResourceLocation id, JsonObject jsonRecipe) {
            String s = JSONUtils.getAsString(jsonRecipe, "group", "");
            JsonElement jsonelement = (JsonElement)(JSONUtils.isArrayNode(jsonRecipe, "ingredient") ? JSONUtils.getAsJsonArray(jsonRecipe, "ingredient") : JSONUtils.getAsJsonObject(jsonRecipe, "ingredient"));
            Ingredient ingredient = Ingredient.fromJson(jsonelement);
            //Forge: Check if primitive string to keep vanilla or a object which can contain a count field.
            if (!jsonRecipe.has("result")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
            ItemStack itemstack;
            if (jsonRecipe.get("result").isJsonObject()) itemstack = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(jsonRecipe, "result"));
            else {
                String s1 = JSONUtils.getAsString(jsonRecipe, "result");
                ResourceLocation resourcelocation = new ResourceLocation(s1);
                itemstack = new ItemStack(Registry.ITEM.getOptional(resourcelocation).orElseThrow(() -> {
                    return new IllegalStateException("Item: " + s1 + " does not exist");
                }));
            }
            float f = JSONUtils.getAsFloat(jsonRecipe, "experience", 0.0F);
            int i = JSONUtils.getAsInt(jsonRecipe, "cookingtime", this.defaultCookingTime);
            return new CanteenFurnaceRecipe(id, s, ingredient, itemstack, f, i);
        }

        public CanteenFurnaceRecipe fromNetwork(ResourceLocation id, PacketBuffer buffer) {
            String s = buffer.readUtf(32767);
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack itemstack = buffer.readItem();
            float f = buffer.readFloat();
            int i = buffer.readVarInt();
            return new CanteenFurnaceRecipe(id, s, ingredient, itemstack, f, i);
        }

        public void toNetwork(PacketBuffer buffer, CanteenFurnaceRecipe recipe) {
            buffer.writeUtf(recipe.group);
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
            buffer.writeFloat(recipe.experience);
            buffer.writeVarInt(recipe.cookingTime);
        }
    }
}
