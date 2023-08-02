package mrp_v2.mrplibrary.datagen.providers;

import mrp_v2.mrplibrary.datagen.TintedBlockStateGenerator;
import mrp_v2.mrplibrary.util.IModLocProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public abstract class ItemModelProvider extends net.minecraftforge.client.model.generators.ItemModelProvider
        implements IModLocProvider
{
    public static final String TEXTURE = "texture";
    public static final String WALL = "wall";

    public ItemModelProvider(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper)
    {
        super(generator, modid, existingFileHelper);
    }

    public ItemModelBuilder fenceInventoryTinted(String name, ResourceLocation texture)
    {
        return withExistingParent(name, TintedBlockStateGenerator.FENCE_INVENTORY_TINTED).texture(TEXTURE, texture);
    }

    public ItemModelBuilder wallInventoryTinted(String name, ResourceLocation wall)
    {
        return withExistingParent(name, TintedBlockStateGenerator.WALL_INVENTORY_TINTED).texture(WALL, wall);
    }

    @Override public String getModId()
    {
        return modid;
    }
}
