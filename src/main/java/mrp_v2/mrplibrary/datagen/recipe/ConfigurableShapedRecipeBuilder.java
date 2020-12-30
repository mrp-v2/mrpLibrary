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
import java.util.Map;
import java.util.Set;

public class ConfigurableShapedRecipeBuilder extends ShapedRecipeBuilder
{
    protected Set<DataGenIngredientOverride> overrides = new HashSet<>();

    protected ConfigurableShapedRecipeBuilder(IItemProvider resultIn, int countIn)
    {
        super(resultIn, countIn);
    }

    public ConfigurableShapedRecipeBuilder(ResourceLocation resultIn, int countIn, @Nullable String resultGroupIn)
    {
        super(resultIn, countIn, resultGroupIn);
    }

    public static ConfigurableShapedRecipeBuilder configurableShapedRecipe(ResourceLocation resultIn)
    {
        return configurableShapedRecipe(resultIn, 1);
    }

    public static ConfigurableShapedRecipeBuilder configurableShapedRecipe(ResourceLocation resultIn, int countIn)
    {
        return configurableShapedRecipe(resultIn, countIn, null);
    }

    public static ConfigurableShapedRecipeBuilder configurableShapedRecipe(ResourceLocation resultIn, int countIn,
            @Nullable String resultGroupIn)
    {
        return new ConfigurableShapedRecipeBuilder(resultIn, countIn, resultGroupIn);
    }

    public static ConfigurableShapedRecipeBuilder configurableShapedRecipe(IItemProvider resultIn)
    {
        return configurableShapedRecipe(resultIn, 1);
    }

    public static ConfigurableShapedRecipeBuilder configurableShapedRecipe(IItemProvider resultIn, int countIn)
    {
        return new ConfigurableShapedRecipeBuilder(resultIn, countIn);
    }

    public DataGenIngredientOverride.Builder<ConfigurableShapedRecipeBuilder> addOverride(String condition)
    {
        return new DataGenIngredientOverride.Builder<>(this, this.overrides, condition);
    }

    @Override public ConfigurableShapedRecipeBuilder key(Character symbol, Tag<Item> tagIn)
    {
        super.key(symbol, tagIn);
        return this;
    }

    @Override public ConfigurableShapedRecipeBuilder key(Character symbol, IItemProvider itemIn)
    {
        super.key(symbol, itemIn);
        return this;
    }

    @Override public ConfigurableShapedRecipeBuilder key(Character symbol, Ingredient ingredientIn)
    {
        super.key(symbol, ingredientIn);
        return this;
    }

    @Override public ConfigurableShapedRecipeBuilder patternLine(String patternIn)
    {
        super.patternLine(patternIn);
        return this;
    }

    @Override public ConfigurableShapedRecipeBuilder addCriterion(String name, ICriterionInstance criterionIn)
    {
        super.addCriterion(name, criterionIn);
        return this;
    }

    @Override public ConfigurableShapedRecipeBuilder setGroup(@Nullable String groupIn)
    {
        super.setGroup(groupIn);
        return this;
    }

    @Override protected IFinishedRecipe buildRecipe(ResourceLocation id)
    {
        return new FinishedConfigurableShapedRecipe(id, result, getGroupString(), advancementBuilder,
                getAdvancementID(id), pattern, key, overrides);
    }

    public static class FinishedConfigurableShapedRecipe extends ShapedRecipeBuilder.FinishedShapedRecipe
    {
        protected Set<DataGenIngredientOverride> overrides;

        public FinishedConfigurableShapedRecipe(ResourceLocation id, RecipeResult result, String group,
                Advancement.Builder advancementBuilder, ResourceLocation advancementID, List<String> pattern,
                Map<Character, Ingredient> key, Set<DataGenIngredientOverride> overrides)
        {
            super(id, result, group, advancementBuilder, advancementID, pattern, key);
            this.overrides = overrides;
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
            return ObjectHolder.CRAFTING_SHAPED_CONFIGURABLE_SERIALIZER.get();
        }
    }
}
