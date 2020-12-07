package mrp_v2.mrplibrary.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mrp_v2.mrplibrary.item.crafting.EquatableMap;
import mrp_v2.mrplibrary.item.crafting.IngredientOverride;
import net.minecraft.item.crafting.Ingredient;

import java.util.Map;
import java.util.Set;

public class DataGenIngredientOverride
{
    private final String condition;
    private final Map<Ingredient, Ingredient> overrides;
    private final int priority;

    private DataGenIngredientOverride(String condition, Map<Ingredient, Ingredient> overrides, int priority)
    {
        this.condition = condition;
        this.overrides = overrides;
        this.priority = priority;
    }

    public JsonObject serialize()
    {
        JsonObject obj = new JsonObject();
        obj.addProperty(IngredientOverride.CONDITION_KEY, this.condition);
        if (this.priority != 0)
        {
            obj.addProperty(IngredientOverride.PRIORITY_KEY, this.priority);
        }
        JsonArray overrides = new JsonArray();
        for (Map.Entry<Ingredient, Ingredient> entry : this.overrides.entrySet())
        {
            JsonObject pair = new JsonObject();
            pair.add(IngredientOverride.ORIGINAL_KEY, entry.getKey().serialize());
            pair.add(IngredientOverride.REPLACEMENT_KEY, entry.getValue().serialize());
            overrides.add(pair);
        }
        obj.add(IngredientOverride.OVERRIDES_KEY, overrides);
        return obj;
    }

    public static class Builder<T>
    {
        private final T parent;
        private final Set<DataGenIngredientOverride> toAddTo;
        private final String condition;
        private final Map<Ingredient, Ingredient> overrides;
        private int priority;

        public Builder(T parent, Set<DataGenIngredientOverride> toAddTo, String condition)
        {
            this.parent = parent;
            this.toAddTo = toAddTo;
            this.condition = condition;
            overrides = new EquatableMap<>(IngredientOverride::ingredientsEqual, IngredientOverride::ingredientsEqual);
        }

        public Builder<T> priority(int priority)
        {
            this.priority = priority;
            return this;
        }

        public Builder<T> override(Ingredient original, Ingredient replacement)
        {
            this.overrides.put(original, replacement);
            return this;
        }

        public T end()
        {
            this.toAddTo.add(this.build());
            return this.parent;
        }

        private DataGenIngredientOverride build()
        {
            return new DataGenIngredientOverride(this.condition, this.overrides, this.priority);
        }
    }
}
