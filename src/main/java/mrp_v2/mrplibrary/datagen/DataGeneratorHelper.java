package mrp_v2.mrplibrary.datagen;

import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Function4;
import com.mojang.datafixers.util.Function5;
import mrp_v2.mrplibrary.datagen.providers.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DataGeneratorHelper
{
    private final DataGenerator dataGenerator;
    private final String modId;
    private final ExistingFileHelper existingFileHelper;
    private BlockTagsProvider blockTagsProvider;
    private final boolean includeClient;
    private final boolean includeServer;
    private final CompletableFuture<HolderLookup.Provider> lookupProvider;

    public DataGeneratorHelper(GatherDataEvent event, String modId)
    {
        this.dataGenerator = event.getGenerator();
        this.modId = modId;
        this.existingFileHelper = event.getExistingFileHelper();
        includeClient = event.includeClient();
        includeServer = event.includeServer();
        lookupProvider = event.getLookupProvider();
    }

    public void addTextureProvider(
            Function3<PackOutput, ExistingFileHelper, String, ? extends TextureProvider> textureProviderConstructor)
    {
        if (includeClient)
        {
            this.dataGenerator.addProvider(true, (DataProvider.Factory<? extends DataProvider>) output ->
                    textureProviderConstructor.apply(output, this.existingFileHelper, this.modId));
        }
    }

    public void addParticleProvider(
            Function3<PackOutput, String, ExistingFileHelper, ? extends ParticleDescriptionProvider> particleProviderConstructor)
    {
        if (includeClient)
        {
            this.dataGenerator.addProvider(true, (DataProvider.Factory<? extends DataProvider>) output -> particleProviderConstructor.apply(output, this.modId, this.existingFileHelper));
        }
    }

    public void addLootTableProvider(
            Function3<PackOutput, Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, String, ? extends LootTableProvider> lootTableProviderConstructor,
            Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>> lootTablesProvider)
    {
        if (includeServer)
        {
            this.dataGenerator.addProvider(true, (DataProvider.Factory<? extends DataProvider>) output ->
                    lootTableProviderConstructor.apply(output, lootTablesProvider, this.modId));
        }
    }

    public void addLootTables(net.minecraft.data.loot.LootTableProvider.SubProviderEntry... blockLootTables)
    {
        if (includeServer)
        {
            this.dataGenerator.addProvider(true, (DataProvider.Factory<? extends DataProvider>) output -> new LootTableProvider(output, Set.of(), List.of(blockLootTables), this.modId));
        }
    }

    public void addRecipeProvider(BiFunction<PackOutput, String, ? extends RecipeProvider> recipeProviderConstructor)
    {
        if (includeServer)
        {
            this.dataGenerator.addProvider(true, (DataProvider.Factory<DataProvider>) output -> recipeProviderConstructor.apply(output, this.modId));
        }
    }

    public void addItemTagsProvider(
            Function5<PackOutput, CompletableFuture<HolderLookup.Provider>, BlockTagsProvider, String, ExistingFileHelper, ? extends ItemTagsProvider> itemTagsProviderConstructor)
    {
        if (includeServer)
        {
            if (this.blockTagsProvider == null) {
                throw new IllegalStateException("Cannot add an ItemTagsProvider without first adding a BlockTagsProvider!");
            }
            this.dataGenerator.addProvider(true, (DataProvider.Factory<DataProvider>) output -> itemTagsProviderConstructor
                    .apply(output, this.lookupProvider, this.blockTagsProvider, this.modId, this.existingFileHelper));
        }
    }

    public void addBlockTagsProvider(
            Function4<PackOutput, CompletableFuture<HolderLookup.Provider>, String, ExistingFileHelper, ? extends BlockTagsProvider> blockTagsProviderConstructor)
    {
        if (includeServer)
        {
            this.blockTagsProvider = blockTagsProviderConstructor.apply(this.dataGenerator.getPackOutput(), this.lookupProvider, this.modId, this.existingFileHelper);
            this.dataGenerator.addProvider(true, (DataProvider.Factory<DataProvider>) output -> this.blockTagsProvider);
        }
    }

    public void addBlockStateProvider(
            Function3<PackOutput, String, ExistingFileHelper, ? extends BlockStateProvider> blockStateProviderConstructor)
    {
        if (includeClient)
        {
            this.dataGenerator.addProvider(true, (DataProvider.Factory<DataProvider>) output -> blockStateProviderConstructor.apply(output, modId, existingFileHelper));
        }
    }

    public void addItemModelProvider(
            Function3<PackOutput, String, ExistingFileHelper, ? extends ItemModelProvider> itemModelProviderConstructor)
    {
        if (includeClient)
        {
            this.dataGenerator.addProvider(true, (DataProvider.Factory<DataProvider>) output ->
                    itemModelProviderConstructor.apply(output, this.modId, this.existingFileHelper));
        }
    }

    public void addLanguageProvider(
            BiFunction<PackOutput, String, ? extends LanguageProvider> languageProviderConstructor)
    {
        if (includeClient)
        {
            this.dataGenerator.addProvider(true, (DataProvider.Factory<DataProvider>) output -> languageProviderConstructor.apply(output, this.modId));
        }
    }
}
