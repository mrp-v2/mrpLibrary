package mrp_v2.mrplibrary.datagen.providers;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.util.IItemProvider;

import java.util.function.Consumer;

public abstract class RecipeProvider extends net.minecraft.data.RecipeProvider
{
    private final String modId;

    protected RecipeProvider(DataGenerator dataGeneratorIn, String modId)
    {
        super(dataGeneratorIn);
        this.modId = modId;
    }

    protected static String getID(IItemProvider item)
    {
        return item.asItem().getRegistryName().getPath();
    }

    @Override protected abstract void registerRecipes(Consumer<IFinishedRecipe> consumer);

    @Override public String getName()
    {
        return super.getName() + ": " + this.modId;
    }
}
