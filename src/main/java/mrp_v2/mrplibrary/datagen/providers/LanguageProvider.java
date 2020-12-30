package mrp_v2.mrplibrary.datagen.providers;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.ItemGroup;

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
        add(key.getTranslationKey(), name);
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
