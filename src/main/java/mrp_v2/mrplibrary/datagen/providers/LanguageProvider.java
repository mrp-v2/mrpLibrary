package mrp_v2.mrplibrary.datagen.providers;

import mrp_v2.mrplibrary.util.IModLocProvider;
import net.minecraft.client.KeyMapping;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.CreativeModeTab;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class LanguageProvider extends net.minecraftforge.common.data.LanguageProvider
        implements IModLocProvider
{
    private static final Logger LOGGER = LogManager.getLogger();
    protected final String modId;
    protected final String locale;

    public LanguageProvider(PackOutput output, String modId, String locale)
    {
        super(output, modId, locale);
        this.modId = modId;
        this.locale = locale;
    }

    @Override
    protected abstract void addTranslations();

    @Override public String getName()
    {
        return super.getName() + " " + modId;
    }

    @Override public String getModId()
    {
        return modId;
    }

    public void add(CreativeModeTab key, String name)
    {
        if (key.getDisplayName().getContents() instanceof TranslatableContents test)
        {
            add(test.getKey(), name);
        } else
        {
            LOGGER.warn("Could not make a translation for " + key + " because its groupName is not a TranslationTextComponent!");
        }
    }

    public void add(KeyMapping keybind, String description, String category)
    {
        add(keybind, description);
        add(keybind.getCategory(), category);
    }

    public void add(KeyMapping keybind, String description)
    {
        add(keybind.getName(), description);
    }

    public void add(TranslatableContents key, String name)
    {
        add(key.getKey(), name);
    }
}
