package mrp_v2.mrplibrary.datagen;

import net.minecraft.block.Block;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class BlockLootTables extends net.minecraft.data.loot.BlockLootTables
{
    private final IdentityHashMap<Block, Consumer<Block>> knownBlocks;

    public BlockLootTables()
    {
        this.knownBlocks = new IdentityHashMap<>();
    }

    public final void addLootTable(Block block, Consumer<Block> lootTableFunction)
    {
        this.knownBlocks.put(block, lootTableFunction);
    }

    @Override protected final void addTables()
    {
        for (Map.Entry<Block, Consumer<Block>> entry : this.knownBlocks.entrySet())
        {
            entry.getValue().accept(entry.getKey());
        }
    }

    @Override protected final Iterable<Block> getKnownBlocks()
    {
        return this.knownBlocks.keySet();
    }
}
