package mrp_v2.mrplibrary.datagen.providers;

import mrp_v2.mrplibrary.util.IModLocProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public abstract class BlockTagsProvider extends net.minecraft.data.BlockTagsProvider implements IModLocProvider
{
    public BlockTagsProvider(DataGenerator generatorIn, String modId, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(generatorIn, modId, existingFileHelper);
    }

    @Override protected abstract void addTags();

    @Override public String getName()
    {
        return super.getName() + ": " + modId;
    }

    @Override public String getModId()
    {
        return modId;
    }
}
