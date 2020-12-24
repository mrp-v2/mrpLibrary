package mrp_v2.mrplibrary.item.crafting;

import com.google.gson.JsonObject;
import mrp_v2.mrplibrary.datagen.recipe.RecipeBuilder;
import mrp_v2.mrplibrary.util.ObjectHolder;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class ShapedRecipe extends net.minecraft.item.crafting.ShapedRecipe
{
    public ShapedRecipe(ResourceLocation idIn, String groupIn, int recipeWidthIn, int recipeHeightIn,
            NonNullList<Ingredient> recipeItemsIn, ItemStack recipeOutputIn)
    {
        super(idIn, groupIn, recipeWidthIn, recipeHeightIn, recipeItemsIn, recipeOutputIn);
    }

    @Override public IRecipeSerializer<?> getSerializer()
    {
        return ObjectHolder.CRAFTING_SHAPED_SERIALIZER.get();
    }

    public static class Serializer extends net.minecraft.item.crafting.ShapedRecipe.Serializer
    {
        @Override public net.minecraft.item.crafting.ShapedRecipe read(ResourceLocation recipeId, JsonObject json)
        {
            return RecipeBuilder.quietMissingItemError(() -> super.read(recipeId, json), recipeId);
        }
    }
}
