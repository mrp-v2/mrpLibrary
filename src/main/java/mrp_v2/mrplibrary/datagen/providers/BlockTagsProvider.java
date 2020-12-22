package mrp_v2.mrplibrary.datagen.providers;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public abstract class BlockTagsProvider extends net.minecraft.data.BlockTagsProvider
{
    public BlockTagsProvider(DataGenerator generatorIn, String modId, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(generatorIn, modId, existingFileHelper);
    }

    @Override protected abstract void registerTags();

    @Override public String getName()
    {
        return super.getName() + ": " + this.modId;
    }
}
