package mrp_v2.mrplibrary.datagen.recipe;

import javax.annotation.Nullable;

public abstract class RecipeResult
{
    private int count;

    public RecipeResult(int count)
    {
        this.count = count;
    }

    public abstract net.minecraft.util.ResourceLocation getItemID();
    public abstract String getGroupPath();
    public abstract boolean hasGroup();

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public static class Item extends RecipeResult
    {
        private net.minecraft.item.Item item;

        public Item(int count, net.minecraft.item.Item item)
        {
            super(count);
            this.item = item;
        }

        @Override public net.minecraft.util.ResourceLocation getItemID()
        {
            return item.getRegistryName();
        }

        @Override public String getGroupPath()
        {
            return item.getGroup().getPath();
        }

        @Override public boolean hasGroup()
        {
            return item.getGroup() != null;
        }

        public void setItem(net.minecraft.item.Item item)
        {
            this.item = item;
        }
    }

    public static class ResourceLocation extends RecipeResult
    {
        private net.minecraft.util.ResourceLocation resourceLocation;
        @Nullable private String groupPath;

        public ResourceLocation(int count, net.minecraft.util.ResourceLocation resourceLocation)
        {
            this(count, resourceLocation, null);
        }

        public ResourceLocation(int count, net.minecraft.util.ResourceLocation resourceLocation,
                @Nullable String groupPath)
        {
            super(count);
            this.resourceLocation = resourceLocation;
            this.groupPath = groupPath;
        }

        @Override public net.minecraft.util.ResourceLocation getItemID()
        {
            return resourceLocation;
        }

        @Override public String getGroupPath()
        {
            return groupPath;
        }

        public void setGroupPath(@Nullable String groupPath)
        {
            this.groupPath = groupPath;
        }

        @Override public boolean hasGroup()
        {
            return groupPath != null;
        }

        public void setItemID(net.minecraft.util.ResourceLocation resourceLocation)
        {
            this.resourceLocation = resourceLocation;
        }
    }
}
