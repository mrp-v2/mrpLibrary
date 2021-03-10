package mrp_v2.mrplibrary.datagen.providers;

import mrp_v2.mrplibrary.util.IModLocProvider;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class LanguageProvider extends net.minecraftforge.common.data.LanguageProvider
        implements IModLocProvider
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Map<String, Map<String, Map<String, String>>> staticTranslations = new HashMap<>();
    protected final String modid;
    protected final String locale;

    public LanguageProvider(DataGenerator gen, String modid, String locale)
    {
        super(gen, modid, locale);
        this.modid = modid;
        this.locale = locale;
    }

    public static TranslationTextComponent makeTextTranslation(String prefix, String modId, String suffix,
            String locale, String name)
    {
        return new TranslationTextComponent(makeStringTranslation(prefix, modId, suffix, locale, name));
    }

    public static String makeStringTranslation(String prefix, String modId, String suffix, String locale, String name)
    {
        Map<String, Map<String, String>> modMap = staticTranslations.computeIfAbsent(modId, k -> new HashMap<>());
        Map<String, String> localeMap = modMap.computeIfAbsent(locale, k -> new HashMap<>());
        String id = prefix + modId + suffix;
        if (localeMap.containsKey(id))
        {
            LOGGER.warn("Overriding translation '" + id + "'");
        }
        localeMap.put(id, name);
        return id;
    }

    public static Function<Object[], TranslationTextComponent> makeFormattedTextTranslation(String prefix, String modId,
            String suffix, String locale, String name)
    {
        String unformattedName = makeStringTranslation(prefix, modId, suffix, locale, name);
        return (Object[] args) -> new TranslationTextComponent(unformattedName, args);
    }

    @Override protected void addTranslations()
    {
        Map<String, Map<String, String>> modMap = staticTranslations.get(modid);
        if (modMap != null)
        {
            Map<String, String> localeMap = modMap.get(locale);
            if (localeMap != null)
            {
                localeMap.forEach(this::add);
            }
        }
    }

    @Override public String getName()
    {
        return super.getName() + " " + modid;
    }

    @Override public String getModId()
    {
        return modid;
    }

    public void add(ItemGroup key, String name)
    {
        TranslationTextComponent test = key.getDisplayName() instanceof TranslationTextComponent ?
                (TranslationTextComponent) key.getDisplayName() : null;
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
        add(keybind.getCategory(), category);
    }

    public void add(KeyBinding keybind, String description)
    {
        add(keybind.getName(), description);
    }
}
