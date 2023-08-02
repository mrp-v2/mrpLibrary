package mrp_v2.mrplibrary.datagen.providers;

import mrp_v2.mrplibrary.util.IModLocProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

public abstract class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider implements IModLocProvider
{
    private final String modId;

    protected RecipeProvider(DataGenerator dataGeneratorIn, String modId)
    {
        super(dataGeneratorIn);
        this.modId = modId;
    }

    @Override
    protected abstract void buildCraftingRecipes(Consumer<FinishedRecipe> consumer);

    @Override public String getName()
    {
        return super.getName() + ": " + modId;
    }

    @Override public String getModId()
    {
        return modId;
    }
}
