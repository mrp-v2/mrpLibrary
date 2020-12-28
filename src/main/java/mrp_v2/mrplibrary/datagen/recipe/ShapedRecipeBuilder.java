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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ShapedRecipeBuilder extends RecipeBuilder
{
    protected List<String> pattern = new ArrayList<>();
    protected Map<Character, Ingredient> key = new LinkedHashMap<>();

    protected ShapedRecipeBuilder(IItemProvider resultIn, int countIn)
    {
        this.result = new RecipeResult(countIn, resultIn.asItem());
    }

    protected ShapedRecipeBuilder(ResourceLocation resultIn, int countIn, @Nullable String resultGroupIn)
    {
        this.result = new RecipeResult(countIn, resultIn, resultGroupIn);
    }

    public static ShapedRecipeBuilder shapedRecipe(IItemProvider resultIn)
    {
        return shapedRecipe(resultIn, 1);
    }

    public static ShapedRecipeBuilder shapedRecipe(IItemProvider resultIn, int countIn)
    {
        return new ShapedRecipeBuilder(resultIn, countIn);
    }

    public static ShapedRecipeBuilder shapedRecipe(ResourceLocation resultIn)
    {
        return shapedRecipe(resultIn, 1);
    }

    public static ShapedRecipeBuilder shapedRecipe(ResourceLocation resultIn, int countIn)
    {
        return shapedRecipe(resultIn, countIn, null);
    }

    public static ShapedRecipeBuilder shapedRecipe(ResourceLocation resultIn, int countIn,
            @Nullable String resultGroupIn)
    {
        return new ShapedRecipeBuilder(resultIn, countIn, resultGroupIn);
    }

    public ShapedRecipeBuilder key(Character symbol, ITag<Item> tagIn)
    {
        return key(symbol, Ingredient.fromTag(tagIn));
    }

    public ShapedRecipeBuilder key(Character symbol, Ingredient ingredientIn)
    {
        if (key.containsKey(symbol))
        {
            throw new IllegalArgumentException("Symbol '" + symbol + "' is already defined!");
        } else if (symbol == ' ')
        {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        } else
        {
            key.put(symbol, ingredientIn);
            return this;
        }
    }

    public ShapedRecipeBuilder key(Character symbol, IItemProvider itemIn)
    {
        return key(symbol, Ingredient.fromItems(itemIn));
    }

    public ShapedRecipeBuilder patternLine(String patternIn)
    {
        if (!pattern.isEmpty() && patternIn.length() != pattern.get(0).length())
        {
            throw new IllegalArgumentException("Pattern must be same width on every line!");
        } else
        {
            pattern.add(patternIn);
            return this;
        }
    }

    @Override public ShapedRecipeBuilder setGroup(@Nullable String groupIn)
    {
        super.setGroup(groupIn);
        return this;
    }

    @Override protected IFinishedRecipe buildRecipe(ResourceLocation id)
    {
        return new FinishedShapedRecipe(id, result, getGroupString(), advancementBuilder, getAdvancementID(id), pattern,
                key);
    }

    @Override public ShapedRecipeBuilder addCriterion(String name, ICriterionInstance criterionIn)
    {
        super.addCriterion(name, criterionIn);
        return this;
    }

    public static class FinishedShapedRecipe extends FinishedRecipe
    {
        protected List<String> pattern;
        protected Map<Character, Ingredient> key;

        public FinishedShapedRecipe(ResourceLocation id, RecipeResult result, String group,
                Advancement.Builder advancementBuilder, ResourceLocation advancementID, List<String> pattern,
                Map<Character, Ingredient> key)
        {
            super(id, result, group, advancementBuilder, advancementID);
            this.pattern = pattern;
            this.key = key;
        }

        @Override public IRecipeSerializer<?> getSerializer()
        {
            return ObjectHolder.CRAFTING_SHAPED_SERIALIZER.get();
        }

        @Override public void serialize(JsonObject json)
        {
            super.serialize(json);
            JsonArray patternJson = new JsonArray();
            for (String patternLine : pattern)
            {
                patternJson.add(patternLine);
            }
            json.add("pattern", patternJson);
            JsonObject keyJson = new JsonObject();
            for (Map.Entry<Character, Ingredient> keyEntry : key.entrySet())
            {
                keyJson.add(String.valueOf(keyEntry.getKey()), keyEntry.getValue().serialize());
            }
            json.add("key", keyJson);
        }
    }
}
