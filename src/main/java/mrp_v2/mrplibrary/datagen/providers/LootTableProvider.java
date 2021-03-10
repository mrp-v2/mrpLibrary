package mrp_v2.mrplibrary.datagen.providers;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import mrp_v2.mrplibrary.datagen.BlockLootTables;
import mrp_v2.mrplibrary.util.IModLocProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LootTableProvider extends net.minecraft.data.LootTableProvider implements IModLocProvider
{
    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>>
            lootTables;
    private final String modId;

    public LootTableProvider(DataGenerator dataGeneratorIn, BlockLootTables blockLootTables, String modId)
    {
        this(dataGeneratorIn, () -> blockLootTables, modId);
    }

    public LootTableProvider(DataGenerator dataGeneratorIn,
            Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>> lootTablesProvider, String modId)
    {
        super(dataGeneratorIn);
        lootTables = ImmutableList.of(Pair.of(lootTablesProvider, LootParameterSets.BLOCK));
        this.modId = modId;
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables()
    {
        return lootTables;
    }

    @Override protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker)
    {
        map.forEach((resourceLocation, lootTable) -> LootTableManager
                .validate(validationtracker, resourceLocation, lootTable));
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
