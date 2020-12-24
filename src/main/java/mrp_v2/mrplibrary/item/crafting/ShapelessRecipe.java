package mrp_v2.mrplibrary.item.crafting;

import com.google.gson.JsonObject;
import mrp_v2.mrplibrary.datagen.recipe.RecipeBuilder;
import mrp_v2.mrplibrary.util.ObjectHolder;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class ShapelessRecipe extends net.minecraft.item.crafting.ShapelessRecipe
{
    public ShapelessRecipe(ResourceLocation idIn, String groupIn, ItemStack recipeOutputIn,
            NonNullList<Ingredient> recipeItemsIn)
    {
        super(idIn, groupIn, recipeOutputIn, recipeItemsIn);
    }

    @Override public IRecipeSerializer<?> getSerializer()
    {
        return ObjectHolder.CRAFTING_SHAPELESS_SERIALIZER.get();
    }

    public static class Serializer extends net.minecraft.item.crafting.ShapelessRecipe.Serializer
    {
        @Override public net.minecraft.item.crafting.ShapelessRecipe read(ResourceLocation recipeId, JsonObject json)
        {
            return RecipeBuilder.quietMissingItemError(() -> super.read(recipeId, json), recipeId);
        }
    }
}
