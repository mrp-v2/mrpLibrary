package mrp_v2.mrplibrary.datagen;

import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Function4;
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

    public void addLootTables(BlockLootTables blockLootTables)
    {
        this.dataGenerator.addProvider(new LootTableProvider(this.dataGenerator, blockLootTables, this.modId));
    }

    public void addRecipeGenerator(
            BiFunction<DataGenerator, String, ? extends RecipeProvider> recipeGeneratorConstructor)
    {
        this.dataGenerator.addProvider(recipeGeneratorConstructor.apply(this.dataGenerator, this.modId));
    }

    public void addItemTagGenerator(
            Function4<DataGenerator, net.minecraft.data.BlockTagsProvider, String, ExistingFileHelper, ? extends ItemTagsProvider> itemTagGeneratorConstructor)
    {
        this.dataGenerator.addProvider(itemTagGeneratorConstructor
                .apply(this.dataGenerator, this.blockTagsProvider, this.modId, this.existingFileHelper));
    }

    public void addBlockTagGenerator(
            Function3<DataGenerator, String, ExistingFileHelper, ? extends BlockTagsProvider> blockTagGeneratorConstructor)
    {
        this.dataGenerator.addProvider(
                blockTagGeneratorConstructor.apply(this.dataGenerator, this.modId, this.existingFileHelper));
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
