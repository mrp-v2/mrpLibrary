package mrp_v2.mrplibrary.datagen.providers;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ExistingFileHelper;

import javax.annotation.Nullable;

public abstract class BlockTagsProvider extends net.minecraft.data.BlockTagsProvider
{
    protected final String modId;
    protected final ExistingFileHelper existingFileHelper;

    public BlockTagsProvider(DataGenerator generatorIn, String modId, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(generatorIn);
        this.modId = modId;
        this.existingFileHelper = existingFileHelper;
    }

    @Override protected abstract void registerTags();

    @Override public String getName()
    {
        return super.getName() + ": " + this.modId;
    }
}
