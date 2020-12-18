package mrp_v2.mrplibrary.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;

public abstract class BlockStateProvider extends net.minecraftforge.client.model.generators.BlockStateProvider
{
    private final ExistingFileHelper existingFileHelper;

    public BlockStateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper)
    {
        super(gen, modid, exFileHelper);
        this.existingFileHelper = exFileHelper;
    }

    public void promiseGeneration(ResourceLocation model)
    {
        existingFileHelper.trackGenerated(model, ResourcePackType.CLIENT_RESOURCES, ".json", "models");
    }
}
