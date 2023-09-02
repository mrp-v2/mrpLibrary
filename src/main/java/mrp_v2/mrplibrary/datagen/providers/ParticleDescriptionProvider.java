package mrp_v2.mrplibrary.datagen.providers;

import mrp_v2.mrplibrary.util.IModLocProvider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;

public abstract class ParticleDescriptionProvider extends net.minecraftforge.common.data.ParticleDescriptionProvider implements IModLocProvider
{
    public final String modId;

    protected ParticleDescriptionProvider(PackOutput output, String modId, ExistingFileHelper existingFileHelper)
    {
        super(output, existingFileHelper);
        this.modId = modId;
    }

    @Override public String getModId()
    {
        return modId;
    }

    @Override public String getName()
    {
        return "Particles: " + modId;
    }
}
