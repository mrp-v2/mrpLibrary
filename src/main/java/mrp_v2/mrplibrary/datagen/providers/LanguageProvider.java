package mrp_v2.mrplibrary.datagen.providers;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.logging.log4j.LogManager;

public abstract class LanguageProvider extends net.minecraftforge.common.data.LanguageProvider
{
    protected final String modid;

    public LanguageProvider(DataGenerator gen, String modid, String locale)
    {
        super(gen, modid, locale);
        this.modid = modid;
    }

    public void add(ItemGroup key, String name)
    {
        TranslationTextComponent test =
                key.getGroupName() instanceof TranslationTextComponent ? (TranslationTextComponent) key.getGroupName() :
                        null;
        if (test != null)
        {
            add(test.getKey(), name);
        } else
        {
            LogManager.getLogger().warn("Could not make a translation for " + key +
                    " because its groupName is not a TranslationTextComponent!");
        }
    }

    public void add(KeyBinding keybind, String description, String category)
    {
        add(keybind, description);
        add(keybind.getKeyCategory(), category);
    }

    public void add(KeyBinding keybind, String description)
    {
        add(keybind.getKeyDescription(), description);
    }

    @Override public String getName()
    {
        return super.getName() + ": " + this.modid;
    }
}
