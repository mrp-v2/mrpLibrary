package mrp_v2.mrplibrary.datagen.providers;

import mrp_v2.mrplibrary.util.IModLocProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Set;

public class LootTableProvider extends net.minecraft.data.loot.LootTableProvider implements IModLocProvider
{
    private final String modId;

    public LootTableProvider(PackOutput output, Set<ResourceLocation> requiredTables, List<SubProviderEntry> subProviderEntries, String modId)
    {
        super(output, Set.of(), subProviderEntries);
        this.modId = modId;
    }

    @Override public String getName()
    {
        return super.getName() + ": " + modId;
    }

    @Override public String getModId()
    {
        return modId;
    }
}
