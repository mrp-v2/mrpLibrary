package mrp_v2.mrplibrary.util;

import net.minecraft.util.ResourceLocation;

public interface IModLocProvider
{
    default ResourceLocation modLoc(String path)
    {
        return new ResourceLocation(getModId(), path);
    }
    String getModId();
}
