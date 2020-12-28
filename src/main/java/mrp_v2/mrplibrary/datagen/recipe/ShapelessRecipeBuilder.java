package mrp_v2.mrplibrary.datagen.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mrp_v2.mrplibrary.util.ObjectHolder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ShapelessRecipeBuilder extends RecipeBuilder
{
    protected List<Ingredient> ingredients = new ArrayList<>();

    protected ShapelessRecipeBuilder(IItemProvider resultIn, int countIn)
    {
        this.result = new RecipeResult(countIn, resultIn.asItem());
    }

    protected ShapelessRecipeBuilder(ResourceLocation resultIn, int countIn, @Nullable String resultGroupIn)
    {
        this.result = new RecipeResult(countIn, resultIn, resultGroupIn);
    }

    public static ShapelessRecipeBuilder shapelessRecipe(IItemProvider resultIn)
    {
        return shapelessRecipe(resultIn, 1);
    }

    public static ShapelessRecipeBuilder shapelessRecipe(IItemProvider resultIn, int countIn)
    {
        return new ShapelessRecipeBuilder(resultIn, countIn);
    }

    public static ShapelessRecipeBuilder shapelessRecipe(ResourceLocation resultIn)
    {
        return shapelessRecipe(resultIn, 1);
    }

    public static ShapelessRecipeBuilder shapelessRecipe(ResourceLocation resultIn, int countIn)
    {
        return shapelessRecipe(resultIn, countIn, null);
    }

    public static ShapelessRecipeBuilder shapelessRecipe(ResourceLocation resultIn, int countIn,
            @Nullable String resultGroupIn)
    {
        return new ShapelessRecipeBuilder(resultIn, countIn, resultGroupIn);
    }

    public ShapelessRecipeBuilder addIngredient(ITag<Item> tagIn)
    {
        return addIngredient(Ingredient.fromTag(tagIn));
    }

    public ShapelessRecipeBuilder addIngredient(Ingredient ingredientIn)
    {
        return this.addIngredient(ingredientIn, 1);
    }

    public ShapelessRecipeBuilder addIngredient(Ingredient ingredientIn, int quantity)
    {
        for (int i = 0; i < quantity; i++)
        {
            ingredients.add(ingredientIn);
        }
        return this;
    }

    public ShapelessRecipeBuilder addIngredient(IItemProvider itemIn)
    {
        return addIngredient(Ingredient.fromItems(itemIn));
    }

    public ShapelessRecipeBuilder addIngredient(IItemProvider itemIn, int quantity)
    {
        for (int i = 0; i < quantity; i++)
        {
            addIngredient(Ingredient.fromItems(itemIn));
        }
        return this;
    }

    @Override public ShapelessRecipeBuilder setGroup(@Nullable String groupIn)
    {
        super.setGroup(groupIn);
        return this;
    }

    @Override protected IFinishedRecipe buildRecipe(ResourceLocation id)
    {
        return new FinishedShapelessRecipe(id, result, getGroupString(), advancementBuilder, getAdvancementID(id),
                ingredients);
    }

    @Override public ShapelessRecipeBuilder addCriterion(String name, ICriterionInstance criterionIn)
    {
        super.addCriterion(name, criterionIn);
        return this;
    }

    public static class FinishedShapelessRecipe extends FinishedRecipe
    {
        protected List<Ingredient> ingredients;

        public FinishedShapelessRecipe(ResourceLocation id, RecipeResult result, String group,
                Advancement.Builder advancementBuilder, ResourceLocation advancementID, List<Ingredient> ingredients)
        {
            super(id, result, group, advancementBuilder, advancementID);
            this.ingredients = ingredients;
        }

        @Override public void serialize(JsonObject json)
        {
            super.serialize(json);
            JsonArray ingredientsJson = new JsonArray();
            for (Ingredient ingredient : ingredients)
            {
                ingredientsJson.add(ingredient.serialize());
            }
            json.add("ingredients", ingredientsJson);
        }

        @Override public IRecipeSerializer<?> getSerializer()
        {
            return ObjectHolder.CRAFTING_SHAPELESS_SERIALIZER.get();
        }
    }
}
