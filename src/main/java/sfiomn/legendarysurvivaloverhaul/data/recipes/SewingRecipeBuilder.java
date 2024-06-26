package sfiomn.legendarysurvivaloverhaul.data.recipes;

import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.registry.RecipeRegistry;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class SewingRecipeBuilder {
    private final Ingredient base;
    private final Ingredient addition;
    private final Item result;
    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    private final RecipeSerializer<?> type;

    public SewingRecipeBuilder(RecipeSerializer<?> type, Ingredient base, Ingredient addition, Item result) {
        this.type = type;
        this.base = base;
        this.addition = addition;
        this.result = result;
    }

    public static SewingRecipeBuilder sewingRecipe(Ingredient base, Ingredient addition, Item result) {
        return new SewingRecipeBuilder(RecipeRegistry.SEWING_SERIALIZER.get(), base, addition, result);
    }

    public SewingRecipeBuilder unlocks(String name, ICriterionInstance advancement) {
        this.advancement.addCriterion(name, advancement);
        return this;
    }

    public void save(Consumer<IFinishedRecipe> p_240504_1_, String id) {
        this.save(p_240504_1_, new ResourceLocation(id));
    }

    public void save(Consumer<IFinishedRecipe> consumer, ResourceLocation id) {
        this.ensureValid(id);
        this.advancement.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(IRequirementsStrategy.OR);
        consumer.accept(new SewingRecipeBuilder.Result(id, this.type, this.base, this.addition, this.result, this.advancement, new ResourceLocation(id.getNamespace(), "recipes/sewing/" + id.getPath())));
    }

    private void ensureValid(ResourceLocation id) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }

    public static class Result implements IFinishedRecipe {
        private final ResourceLocation id;
        private final Ingredient base;
        private final Ingredient addition;
        private final Item result;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;
        private final RecipeSerializer<?> type;

        public Result(ResourceLocation id, RecipeSerializer<?> type, Ingredient base, Ingredient addition, Item result, Advancement.Builder advancement, ResourceLocation advancementId) {
            this.id = id;
            this.type = type;
            this.base = base;
            this.addition = addition;
            this.result = result;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        public void serializeRecipeData(JsonObject json) {
            ResourceLocation itemRegistryName = ForgeRegistries.ITEMS.getKey(this.result);
            if (itemRegistryName != null) {
                json.add("base", this.base.toJson());
                json.add("addition", this.addition.toJson());
                JsonObject jsonobject = new JsonObject();
                jsonobject.addProperty("item", itemRegistryName.toString());
                json.add("result", jsonobject);
            }
        }

        public ResourceLocation getId() {
            return this.id;
        }

        public RecipeSerializer<?> getType() {
            return this.type;
        }

        @Nullable
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Nullable
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}
