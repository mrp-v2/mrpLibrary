package mrp_v2.mrplibrary.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mrp_v2.mrplibrary.item.crafting.ConfigurableShapelessRecipe;
import mrp_v2.mrplibrary.item.crafting.IngredientOverride;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class ConfigurableShapelessRecipeBuilder extends ShapelessRecipeBuilder
{
    final Set<DataGenIngredientOverride> overrides;

    public ConfigurableShapelessRecipeBuilder(IItemProvider resultIn, int countIn)
    {
        super(resultIn, countIn);
        this.overrides = new HashSet<>();
    }

    public static ConfigurableShapelessRecipeBuilder configurableShapelessRecipe(IItemProvider resultIn)
    {
        return new ConfigurableShapelessRecipeBuilder(resultIn, 1);
    }

    public static ConfigurableShapelessRecipeBuilder configurableShapelessRecipe(IItemProvider resultIn, int countIn)
    {
        return new ConfigurableShapelessRecipeBuilder(resultIn, countIn);
    }

    public DataGenIngredientOverride.Builder<ConfigurableShapelessRecipeBuilder> addOverride(String condition)
    {
        return new DataGenIngredientOverride.Builder<>(this, this.overrides, condition);
    }

    @Override public ConfigurableShapelessRecipeBuilder addIngredient(ITag<Item> tagIn)
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

    @Override public ConfigurableShapelessRecipeBuilder setGroup(String groupIn)
    {
        super.setGroup(groupIn);
        return this;
    }

    /**
     * Copied from {@link ShapelessRecipeBuilder#build(Consumer, ResourceLocation)}.
     * Modified to use {@link Result} instead of {@link ShapelessRecipeBuilder.Result}.
     */
    @Override public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id)
    {
        this.validate(id);
        this.advancementBuilder.withParentId(new ResourceLocation("recipes/root"))
                .withCriterion("has_the_recipe", RecipeUnlockedTrigger.create(id))
                .withRewards(AdvancementRewards.Builder.recipe(id)).withRequirementsStrategy(IRequirementsStrategy.OR);
        consumerIn
                .accept(new Result(id, this.result, this.count, this.group == null ? "" : this.group, this.ingredients,
                        this.advancementBuilder, new ResourceLocation(id.getNamespace(),
                        "recipes/" + this.result.getGroup().getPath() + "/" + id.getPath()), this.overrides));
    }

    public class Result extends ShapelessRecipeBuilder.Result
    {
        private final Set<DataGenIngredientOverride> overrides;

        public Result(ResourceLocation idIn, Item resultIn, int countIn, String groupIn, List<Ingredient> ingredientsIn,
                Advancement.Builder advancementBuilderIn, ResourceLocation advancementIdIn,
                Set<DataGenIngredientOverride> overrides)
        {
            super(idIn, resultIn, countIn, groupIn, ingredientsIn, advancementBuilderIn, advancementIdIn);
            this.overrides = overrides;
        }

        @Override public void serialize(JsonObject json)
        {
            super.serialize(json);
            if (overrides.size() < 1)
            {
                LogManager.getLogger().warn("Recipe " + getID() + " is configurable, but does not have any overrides!");
            }
            JsonArray overridesArray = new JsonArray();
            for (DataGenIngredientOverride override : this.overrides)
            {
                overridesArray.add(override.serialize());
            }
            json.add(IngredientOverride.OVERRIDES_KEY, overridesArray);
        }

        @Override public IRecipeSerializer<?> getSerializer()
        {
            return ConfigurableShapelessRecipe.Serializer.INSTANCE;
        }
    }
}
