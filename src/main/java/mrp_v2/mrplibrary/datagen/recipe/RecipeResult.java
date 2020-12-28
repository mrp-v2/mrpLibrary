package mrp_v2.mrplibrary.datagen.recipe;

import mrp_v2.mrplibrary.util.Possible;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RecipeResult
{
    private int count;
    private Possible<net.minecraft.item.Item> item;

    public RecipeResult(int count)
    {
        this.count = count;
    }

    public RecipeResult(int count, Item item)
    {
        this(count);
        this.item = Possible.of(item);
    }

    public RecipeResult(int count, ResourceLocation loc, String group)
    {
        this(count);
        this.item = new ItemDoesNotExist(loc, group);
    }

    public net.minecraft.util.ResourceLocation getItemID()
    {
        return item.getId();
    }

    public String getGroupPath()
    {
        return item.exists() ? item.get().getGroup().getPath() : ((ItemDoesNotExist) item).groupPath;
    }

    public boolean hasGroup()
    {
        return item.exists() || (item instanceof ItemDoesNotExist && ((ItemDoesNotExist) item).groupPath != null);
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public void setItem(net.minecraft.item.Item item)
    {
        this.item = Possible.of(item);
    }

    public void setItem(net.minecraft.util.ResourceLocation loc, @Nullable String groupPath)
    {
        item = new ItemDoesNotExist(loc, groupPath);
    }

    public static class ItemDoesNotExist extends Possible.DoesNotExist<Item>
    {
        @Nullable private final String groupPath;

        public ItemDoesNotExist(net.minecraft.util.ResourceLocation resourceLocation, @Nullable String groupPath)
        {
            super(resourceLocation);
            this.groupPath = groupPath;
        }
    }
}
