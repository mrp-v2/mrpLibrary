package mrp_v2.mrplibrary.item.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import mrp_v2.mrplibrary.util.ObjectHolder;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Set;

public class ConfigurableShapelessRecipe extends ConfigurableCraftingRecipe
{
    private boolean isSimple;

    private ConfigurableShapelessRecipe(ResourceLocation id, String group, ItemStack recipeOutput,
            NonNullList<Ingredient> recipeItems)
    {
        super(id, group, recipeOutput, recipeItems);
        this.isSimple = recipeItems.stream().allMatch(Ingredient::isSimple);
    }

    private ConfigurableShapelessRecipe(ResourceLocation id, String group, ItemStack recipeOutput,
            NonNullList<Ingredient> recipeItems, Set<IngredientOverride> overrides)
    {
        super(id, group, recipeOutput, recipeItems, overrides);
        this.isSimple = recipeItems.stream().allMatch(Ingredient::isSimple);
    }

    @Override public NonNullList<Ingredient> getIngredients()
    {
        if (!this.hasCalculatedIngredients)
        {
            super.getIngredients();
            this.isSimple = this.recipeItems.stream().allMatch(Ingredient::isSimple);
        }
        return super.getIngredients();
    }

    @Override public boolean matches(CraftingInventory inv, World worldIn)
    {
        RecipeItemHelper recipeitemhelper = new RecipeItemHelper();
        java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
        int i = 0;
        for (int j = 0; j < inv.getSizeInventory(); ++j)
        {
            ItemStack itemstack = inv.getStackInSlot(j);
            if (!itemstack.isEmpty())
            {
                ++i;
                if (isSimple)
                {
                    recipeitemhelper.func_221264_a(itemstack, 1);
                } else
                {
                    inputs.add(itemstack);
                }
            }
        }
        return i == this.recipeItems.size() && (isSimple ? recipeitemhelper.canCraft(this, null) :
                net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs, this.recipeItems) != null);
    }

    @Override public boolean canFit(int width, int height)
    {
        return width * height >= this.recipeItems.size();
    }

    @Override public IRecipeSerializer<?> getSerializer()
    {
        return ObjectHolder.CRAFTING_SHAPELESS_CONFIGURABLE_SERIALIZER.get();
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
            implements IRecipeSerializer<ConfigurableShapelessRecipe>
    {
        @Override public ConfigurableShapelessRecipe read(ResourceLocation recipeId, JsonObject json)
        {
            NonNullList<Ingredient> ingredients = readIngredients(JSONUtils.getJsonArray(json, "ingredients"));
            if (ingredients.isEmpty())
            {
                throw new JsonParseException("No ingredients for shapeless recipe");
            } else if (ingredients.size() > MAX_WIDTH * MAX_HEIGHT)
            {
                throw new JsonParseException(
                        "Too many ingredients for shapeless recipe the max is " + (MAX_WIDTH * MAX_HEIGHT));
            } else
            {
                return new ConfigurableShapelessRecipe(recipeId, JSONUtils.getString(json, "group", ""),
                        ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result")), ingredients,
                        IngredientOverride.getOverridesFromJson(json));
            }
        }

        private static NonNullList<Ingredient> readIngredients(JsonArray json)
        {
            NonNullList<Ingredient> ingredients = NonNullList.create();
            for (int i = 0; i < json.size(); ++i)
            {
                Ingredient ingredient = Ingredient.deserialize(json.get(i));
                if (!ingredient.hasNoMatchingItems())
                {
                    ingredients.add(ingredient);
                }
            }
            return ingredients;
        }

        @Nullable @Override public ConfigurableShapelessRecipe read(ResourceLocation recipeId, PacketBuffer buffer)
        {
            String group = buffer.readString(32767);
            int ingredientsSize = buffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(ingredientsSize, Ingredient.EMPTY);
            for (int j = 0; j < ingredients.size(); ++j)
            {
                ingredients.set(j, Ingredient.read(buffer));
            }
            ItemStack recipeOutput = buffer.readItemStack();
            return new ConfigurableShapelessRecipe(recipeId, group, recipeOutput, ingredients);
        }

        @Override public void write(PacketBuffer buffer, ConfigurableShapelessRecipe recipe)
        {
            buffer.writeString(recipe.group);
            buffer.writeVarInt(recipe.recipeItems.size());
            for (Ingredient ingredient : recipe.getIngredients())
            {
                ingredient.write(buffer);
            }
            buffer.writeItemStack(recipe.recipeOutput);
        }
    }
}
