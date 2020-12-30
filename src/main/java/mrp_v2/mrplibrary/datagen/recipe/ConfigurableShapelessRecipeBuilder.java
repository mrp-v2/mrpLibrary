package mrp_v2.mrplibrary.datagen.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mrp_v2.mrplibrary.datagen.DataGenIngredientOverride;
import mrp_v2.mrplibrary.item.crafting.IngredientOverride;
import mrp_v2.mrplibrary.util.ObjectHolder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConfigurableShapelessRecipeBuilder extends ShapelessRecipeBuilder
{
    protected Set<DataGenIngredientOverride> overrides = new HashSet<>();

    protected ConfigurableShapelessRecipeBuilder(IItemProvider resultIn, int countIn)
    {
        super(resultIn, countIn);
    }

    public ConfigurableShapelessRecipeBuilder(ResourceLocation resultIn, int countIn, @Nullable String resultGroupIn)
    {
        super(resultIn, countIn, resultGroupIn);
    }

    public static ConfigurableShapelessRecipeBuilder configurableShapelessRecipe(ResourceLocation resultIn)
    {
        return configurableShapelessRecipe(resultIn, 1);
    }

    public static ConfigurableShapelessRecipeBuilder configurableShapelessRecipe(ResourceLocation resultIn, int countIn)
    {
        return configurableShapelessRecipe(resultIn, countIn, null);
    }

    public static ConfigurableShapelessRecipeBuilder configurableShapelessRecipe(ResourceLocation resultIn, int countIn,
            @Nullable String resultGroupIn)
    {
        return new ConfigurableShapelessRecipeBuilder(resultIn, countIn, resultGroupIn);
    }

    public static ConfigurableShapelessRecipeBuilder configurableShapelessRecipe(IItemProvider resultIn)
    {
        return configurableShapelessRecipe(resultIn, 1);
    }

    public static ConfigurableShapelessRecipeBuilder configurableShapelessRecipe(IItemProvider resultIn, int countIn)
    {
        return new ConfigurableShapelessRecipeBuilder(resultIn, countIn);
    }

    public DataGenIngredientOverride.Builder<ConfigurableShapelessRecipeBuilder> addOverride(String condition)
    {
        return new DataGenIngredientOverride.Builder<>(this, this.overrides, condition);
    }

    @Override public ConfigurableShapelessRecipeBuilder addIngredient(Tag<Item> tagIn)
    {
        super.addIngredient(tagIn);
        return this;
    }

    @Override public ConfigurableShapelessRecipeBuilder addIngredient(IItemProvider itemIn)
    {
        super.addIngredient(itemIn);
        return this;
    }

    @Override public ConfigurableShapelessRecipeBuilder addIngredient(IItemProvider itemIn, int quantity)
    {
        super.addIngredient(itemIn, quantity);
        return this;
    }

    @Override public ConfigurableShapelessRecipeBuilder addIngredient(Ingredient ingredientIn)
    {
        super.addIngredient(ingredientIn);
        return this;
    }

    @Override public ConfigurableShapelessRecipeBuilder addIngredient(Ingredient ingredientIn, int quantity)
    {
        super.addIngredient(ingredientIn, quantity);
        return this;
    }

    @Override public ConfigurableShapelessRecipeBuilder addCriterion(String name, ICriterionInstance criterionIn)
    {
        super.addCriterion(name, criterionIn);
        return this;
    }

    @Override public ConfigurableShapelessRecipeBuilder setGroup(@Nullable String groupIn)
    {
        super.setGroup(groupIn);
        return this;
    }

    @Override protected IFinishedRecipe buildRecipe(ResourceLocation id)
    {
        return new FinishedConfigurableShapelessRecipe(id, result, getGroupString(), advancementBuilder,
                getAdvancementID(id), ingredients, overrides);
    }

    public static class FinishedConfigurableShapelessRecipe extends ShapelessRecipeBuilder.FinishedShapelessRecipe
    {
        protected Set<DataGenIngredientOverride> overrides;

        public FinishedConfigurableShapelessRecipe(ResourceLocation id, RecipeResult result, String group,
                Advancement.Builder advancementBuilder, ResourceLocation advancementID, List<Ingredient> ingredients,
                Set<DataGenIngredientOverride> overrides)
        {
            super(id, result, group, advancementBuilder, advancementID, ingredients);
            this.ingredients = ingredients;
        }

        @Override public void serialize(JsonObject json)
        {
            super.serialize(json);
            if (overrides.size() < 1)
            {
                throw new IllegalStateException("Recipe " + getID() + " should not be configurable");
            }
            JsonArray overridesJson = new JsonArray();
            for (DataGenIngredientOverride override : this.overrides)
            {
                overridesJson.add(override.serialize());
            }
            json.add(IngredientOverride.OVERRIDES_KEY, overridesJson);
        }

        @Override public IRecipeSerializer<?> getSerializer()
        {
            return ObjectHolder.CRAFTING_SHAPELESS_CONFIGURABLE_SERIALIZER.get();
        }
    }
}
