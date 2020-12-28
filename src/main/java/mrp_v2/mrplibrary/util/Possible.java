package mrp_v2.mrplibrary.util;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class Possible<T extends ForgeRegistryEntry<T>>
{
    public static <V extends ForgeRegistryEntry<V>> Possible<V> of(V v)
    {
        return new Exists<>(v);
    }

    public static <V extends ForgeRegistryEntry<V>> Possible<V> of(ResourceLocation loc)
    {
        return new DoesNotExist<>(loc);
    }

    public abstract ResourceLocation getId();
    public abstract boolean exists();
    public abstract T get();

    public static class Exists<U extends ForgeRegistryEntry<U>> extends Possible<U>
    {
        protected final U u;

        public Exists(U u)
        {
            this.u = u;
        }

        @Override public ResourceLocation getId()
        {
            return u.getRegistryName();
        }

        @Override public boolean exists()
        {
            return true;
        }

        @Override public U get()
        {
            return u;
        }
    }

    public static class DoesNotExist<U extends ForgeRegistryEntry<U>> extends Possible<U>
    {
        protected final ResourceLocation loc;

        public DoesNotExist(ResourceLocation loc)
        {
            this.loc = loc;
        }

        @Override public ResourceLocation getId()
        {
            return loc;
        }

        @Override public boolean exists()
        {
            return false;
        }

        @Override public U get()
        {
            throw new UnsupportedOperationException();
        }
    }
}
