package mrp_v2.mrplibrary.datagen;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public abstract class BlockLootTables extends BlockLootSubProvider {
    public BlockLootTables(Set<Item> explosionResistant, FeatureFlagSet enabledFeatures)
    {
        super(explosionResistant, enabledFeatures);
    }

    @Override
    protected abstract Iterable<Block> getKnownBlocks();
}
