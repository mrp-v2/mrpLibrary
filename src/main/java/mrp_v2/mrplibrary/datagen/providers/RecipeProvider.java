package mrp_v2.mrplibrary.datagen.providers;

import mrp_v2.mrplibrary.util.IModLocProvider;
import net.minecraft.data.PackOutput;

public abstract class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider implements IModLocProvider
{
    private final String modId;

    protected RecipeProvider(PackOutput output, String modId)
    {
        super(output);
        this.modId = modId;
    }

    @Override public String getModId()
    {
        return modId;
    }
}
