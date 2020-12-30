package mrp_v2.mrplibrary.datagen.providers;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ExistingFileHelper;

public abstract class BlockStateProvider extends net.minecraftforge.client.model.generators.BlockStateProvider
{
    private final ExistingFileHelper existingFileHelper;

    public BlockStateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper)
    {
        super(gen, modid, exFileHelper);
        this.existingFileHelper = exFileHelper;
    }
}
