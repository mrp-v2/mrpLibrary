package mrp_v2.mrplibrary.datagen;

import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Function4;
import mrp_v2.mrplibrary.datagen.providers.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DataGeneratorHelper
{
    private final DataGenerator dataGenerator;
    private final String modId;
    private final ExistingFileHelper existingFileHelper;
    private final net.minecraft.data.tags.BlockTagsProvider blockTagsProvider;
    private final boolean includeClient;
    private final boolean includeServer;

    public DataGeneratorHelper(GatherDataEvent event, String modId)
    {
        this.dataGenerator = event.getGenerator();
        this.modId = modId;
        this.existingFileHelper = event.getExistingFileHelper();
        this.blockTagsProvider =
                new net.minecraft.data.tags.BlockTagsProvider(this.dataGenerator, this.modId, this.existingFileHelper);
        includeClient = event.includeClient();
        includeServer = event.includeServer();
    }

    public void addTextureProvider(
            Function3<DataGenerator, ExistingFileHelper, String, ? extends TextureProvider> textureProviderConstructor)
    {
        if (includeClient)
        {
            this.dataGenerator.addProvider(
                    textureProviderConstructor.apply(this.dataGenerator, this.existingFileHelper, this.modId));
        }
    }

    public void addParticleProvider(
            BiFunction<DataGenerator, String, ? extends ParticleProvider> particleProviderConstructor)
    {
        if (includeClient)
        {
            this.dataGenerator.addProvider(particleProviderConstructor.apply(this.dataGenerator, this.modId));
        }
    }

    public void addLootTableProvider(
            Function3<DataGenerator, Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, String, ? extends LootTableProvider> lootTableProviderConstructor,
            Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>> lootTablesProvider)
    {
        if (includeServer)
        {
            this.dataGenerator.addProvider(
                    lootTableProviderConstructor.apply(this.dataGenerator, lootTablesProvider, this.modId));
        }
    }

    public void addLootTables(BlockLootTables blockLootTables)
    {
        if (includeServer)
        {
            this.dataGenerator.addProvider(new LootTableProvider(this.dataGenerator, blockLootTables, this.modId));
        }
    }

    public void addRecipeProvider(BiFunction<DataGenerator, String, ? extends RecipeProvider> recipeProviderConstructor)
    {
        if (includeServer)
        {
            this.dataGenerator.addProvider(recipeProviderConstructor.apply(this.dataGenerator, this.modId));
        }
    }

    public void addItemTagsProvider(
            Function4<DataGenerator, net.minecraft.data.tags.BlockTagsProvider, String, ExistingFileHelper, ? extends ItemTagsProvider> itemTagsProviderConstructor)
    {
        if (includeServer)
        {
            this.dataGenerator.addProvider(itemTagsProviderConstructor
                    .apply(this.dataGenerator, this.blockTagsProvider, this.modId, this.existingFileHelper));
        }
    }

    public void addBlockTagsProvider(
            Function3<DataGenerator, String, ExistingFileHelper, ? extends BlockTagsProvider> blockTagsProviderConstructor)
    {
        if (includeServer)
        {
            this.dataGenerator.addProvider(
                    blockTagsProviderConstructor.apply(this.dataGenerator, this.modId, this.existingFileHelper));
        }
    }

    public void addBlockStateProvider(
            Function3<DataGenerator, String, ExistingFileHelper, ? extends BlockStateProvider> blockStateProviderConstructor)
    {
        if (includeClient)
        {
            this.dataGenerator.addProvider(
                    blockStateProviderConstructor.apply(this.dataGenerator, this.modId, this.existingFileHelper));
        }
    }

    public void addItemModelProvider(
            Function3<DataGenerator, String, ExistingFileHelper, ? extends ItemModelProvider> itemModelProviderConstructor)
    {
        if (includeClient)
        {
            this.dataGenerator.addProvider(
                    itemModelProviderConstructor.apply(this.dataGenerator, this.modId, this.existingFileHelper));
        }
    }

    public void addLanguageProvider(
            BiFunction<DataGenerator, String, ? extends LanguageProvider> languageProviderConstructor)
    {
        if (includeClient)
        {
            this.dataGenerator.addProvider(languageProviderConstructor.apply(this.dataGenerator, this.modId));
        }
    }
}
