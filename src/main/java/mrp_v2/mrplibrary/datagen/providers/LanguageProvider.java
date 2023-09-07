package mrp_v2.mrplibrary.datagen.providers;

import mrp_v2.mrplibrary.util.IModLocProvider;
import net.minecraft.client.KeyMapping;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
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

    /**
     * Assumes the content of the component is translatable. Will throw an error if not.
     */
    public void add(Component component, String name) {
        if (component.getContents() instanceof TranslatableContents translatableContents) {
            add(translatableContents, name);
        } else {
            throw new IllegalArgumentException("The content of the component was not translatable!");
        }
    }
}
