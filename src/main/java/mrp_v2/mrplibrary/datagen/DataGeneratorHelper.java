package mrp_v2.mrplibrary.datagen;

import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Function4;
import mrp_v2.mrplibrary.datagen.providers.*;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import java.util.function.BiFunction;

public class DataGeneratorHelper
{
    private final DataGenerator dataGenerator;
    private final String modId;
    private final ExistingFileHelper existingFileHelper;
    private final net.minecraft.data.BlockTagsProvider blockTagsProvider;

    public DataGeneratorHelper(GatherDataEvent event, String modId)
    {
        this.dataGenerator = event.getGenerator();
        this.modId = modId;
        this.existingFileHelper = event.getExistingFileHelper();
        this.blockTagsProvider =
                new net.minecraft.data.BlockTagsProvider(this.dataGenerator, this.modId, this.existingFileHelper);
    }

    public void addTextureProvider(
            Function3<DataGenerator, ExistingFileHelper, String, ? extends TextureProvider> textureProviderConstructor)
    {
        this.dataGenerator
                .addProvider(textureProviderConstructor.apply(this.dataGenerator, this.existingFileHelper, this.modId));
    }

    public void addParticleProvider(
            BiFunction<DataGenerator, String, ? extends ParticleProvider> particleProviderConstructor)
    {
        this.dataGenerator.addProvider(particleProviderConstructor.apply(this.dataGenerator, this.modId));
    }

    public void addLootTables(BlockLootTables blockLootTables)
    {
        this.dataGenerator.addProvider(new LootTableProvider(this.dataGenerator, blockLootTables, this.modId));
    }

    public void addRecipeProvider(BiFunction<DataGenerator, String, ? extends RecipeProvider> recipeProviderConstructor)
    {
        this.dataGenerator.addProvider(recipeProviderConstructor.apply(this.dataGenerator, this.modId));
    }

    public void addItemTagsProvider(
            Function4<DataGenerator, net.minecraft.data.BlockTagsProvider, String, ExistingFileHelper, ? extends ItemTagsProvider> itemTagsProviderConstructor)
    {
        this.dataGenerator.addProvider(itemTagsProviderConstructor
                .apply(this.dataGenerator, this.blockTagsProvider, this.modId, this.existingFileHelper));
    }

    public void addBlockTagsProvider(
            Function3<DataGenerator, String, ExistingFileHelper, ? extends BlockTagsProvider> blockTagsProviderConstructor)
    {
        this.dataGenerator.addProvider(
                blockTagsProviderConstructor.apply(this.dataGenerator, this.modId, this.existingFileHelper));
    }

    public void addBlockStateProvider(
            Function3<DataGenerator, String, ExistingFileHelper, ? extends BlockStateProvider> blockStateProviderConstructor)
    {
        this.dataGenerator.addProvider(
                blockStateProviderConstructor.apply(this.dataGenerator, this.modId, this.existingFileHelper));
    }

    public void addItemModelProvider(
            Function3<DataGenerator, String, ExistingFileHelper, ? extends ItemModelProvider> itemModelProviderConstructor)
    {
        this.dataGenerator.addProvider(
                itemModelProviderConstructor.apply(this.dataGenerator, this.modId, this.existingFileHelper));
    }

    public void addLanguageProvider(
            BiFunction<DataGenerator, String, ? extends LanguageProvider> languageProviderConstructor)
    {
        this.dataGenerator.addProvider(languageProviderConstructor.apply(this.dataGenerator, this.modId));
    }
}
