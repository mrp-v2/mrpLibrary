package mrp_v2.mrplibrary.item.crafting;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiFunction;

public class EquatableMap<K, V> implements Map<K, V>
{
    private final BiFunction<K, K, Boolean> keyEqualityComparator;
    private final BiFunction<V, V, Boolean> valueEqualityComparator;
    private final ArrayList<Entry<K, V>> entries;

    public EquatableMap(BiFunction<K, K, Boolean> keyEqualityComparator,
            BiFunction<V, V, Boolean> valueEqualityComparator)
    {
        this.keyEqualityComparator = keyEqualityComparator;
        this.valueEqualityComparator = valueEqualityComparator;
        this.entries = new ArrayList<>();
    }

    @Override public int size()
    {
        return this.entries.size();
    }

    @Override public boolean isEmpty()
    {
        return this.entries.size() == 0;
    }

    @Override public boolean containsKey(Object key)
    {
        return getEntry(key) != null;
    }

    @Override public boolean containsValue(Object value)
    {
        for (Entry<K, V> entry : this.entries)
        {
            if (valueEqualityComparator.apply(entry.getValue(), (V) value))
            {
                return true;
            }
        }
        return false;
    }

    @Nullable @Override public V get(Object key)
    {
        Entry<K, V> entry = getEntry(key);
        if (entry != null)
        {
            return entry.getValue();
        }
        return null;
    }

    @Nullable public V put(K key, V value)
    {
        if (containsKey(key))
        {
            return this.getEntry(key).setValue(value);
        }
        this.entries.add(new Entry<>(key, value));
        return null;
    }

    @Nullable @Override public V remove(Object key)
    {
        Entry<K, V> old = getEntry(key);
        return entries.remove(old) ? old.value : null;
    }

    @Override public void putAll(Map<? extends K, ? extends V> m)
    {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet())
        {
            this.entries.add(new Entry<>(entry.getKey(), entry.getValue()));
        }
    }

    @Override public void clear()
    {
        this.entries.clear();
    }

    public Set<K> keySet()
    {
        HashSet<K> set = new HashSet<>(this.entries.size());
        for (Entry<K, V> entry : this.entries)
        {
            set.add(entry.getKey());
        }
        return set;
    }

    @Override public Collection<V> values()
    {
        HashSet<V> set = new HashSet<>(this.entries.size());
        for (Entry<K, V> entry : this.entries)
        {
            set.add(entry.getValue());
        }
        return set;
    }

    @Override public Set<Map.Entry<K, V>> entrySet()
    {
        return new HashSet<>(this.entries);
    }

    @Nullable private Entry<K, V> getEntry(Object key)
    {
        for (Entry<K, V> entry : this.entries)
        {
            if (keyEqualityComparator.apply(entry.getKey(), (K) key))
            {
                return entry;
            }
        }
        return null;
    }

    public static class Entry<K, V> implements Map.Entry<K, V>
    {
        private final K key;
        private V value;

        public Entry(K key, V value)
        {
            this.key = key;
            this.value = value;
        }

        @Override public K getKey()
        {
            return this.key;
        }

        @Override public V getValue()
        {
            return this.value;
        }

        @Override public V setValue(V value)
        {
            V old = this.value;
            this.value = value;
            return old;
        }
    }
}
