package mrp_v2.mrplibrary.datagen.providers;

import mrp_v2.mrplibrary.util.IModLocProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;

import java.util.function.Consumer;

public abstract class RecipeProvider extends net.minecraft.data.RecipeProvider implements IModLocProvider
{
    private final String modId;

    protected RecipeProvider(DataGenerator dataGeneratorIn, String modId)
    {
        super(dataGeneratorIn);
        this.modId = modId;
    }

    @Override protected abstract void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer);

    @Override public String getName()
    {
        return super.getName() + ": " + modId;
    }

    @Override public String getModId()
    {
        return modId;
    }
}
