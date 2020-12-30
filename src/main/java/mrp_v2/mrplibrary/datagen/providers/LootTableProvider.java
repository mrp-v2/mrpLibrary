package mrp_v2.mrplibrary.datagen.providers;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import mrp_v2.mrplibrary.datagen.BlockLootTables;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LootTableProvider extends net.minecraft.data.LootTableProvider
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
        this.lootTables = ImmutableList.of(Pair.of(lootTablesProvider, LootParameterSets.BLOCK));
        this.modId = modId;
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables()
    {
        return this.lootTables;
    }

    @Override protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker)
    {
        map.forEach((resourceLocation, lootTable) -> LootTableManager
                .func_227508_a_(validationtracker, resourceLocation, lootTable));
    }

    @Override public String getName()
    {
        return super.getName() + ": " + this.modId;
    }
}
