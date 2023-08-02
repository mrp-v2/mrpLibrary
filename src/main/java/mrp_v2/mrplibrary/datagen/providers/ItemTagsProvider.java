package mrp_v2.mrplibrary.datagen.providers;

import mrp_v2.mrplibrary.util.IModLocProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public abstract class ItemTagsProvider extends net.minecraft.data.tags.ItemTagsProvider implements IModLocProvider
{
    protected ItemTagsProvider(DataGenerator dataGenerator, net.minecraft.data.tags.BlockTagsProvider blockTagProvider, String modId,
            @Nullable ExistingFileHelper existingFileHelper)
    {
        super(dataGenerator, blockTagProvider, modId, existingFileHelper);
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
