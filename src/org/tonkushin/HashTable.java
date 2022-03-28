package org.tonkushin;

import java.util.Arrays;

/**
 * Хэш-таблица
 *
 * @param <K> ключ
 * @param <V> значение
 */
public class HashTable<K, V> {
    // Кол-во ячеек по умолчанию
    private static final int DEFAULT_CAPACITY = 11;

    // Коэффициент загрузки
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    // Коэффициент загрузки
    private final float loadFactor = 0.75f;

    // Пороговое значение. Когда количество элементов превышает пороговое значение, хэш-таблица вызывает rehash()
    private int threshold;

    // Размер таблицы
    private int size;

    // Массив, содержащий фактические сопоставления ключ-значение
    HashEntry<K, V>[] buckets;

    public HashTable() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    @SuppressWarnings("unchecked")
    public HashTable(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }

        if (!(loadFactor > 0)) {
            throw new IllegalArgumentException("Illegal Load: " + loadFactor);
        }

        if (initialCapacity == 0) {
            initialCapacity = 1;
        }

        buckets = (HashEntry<K, V>[]) new HashEntry[initialCapacity];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean contains(Object value) {
        if (value == null) {
            throw new NullPointerException();
        }

        for (int i = buckets.length - 1; i >= 0; i--) {
            HashEntry<K, V> e = buckets[i];

            while (e != null) {
                if (e.value.equals(value)) {
                    return true;
                }

                e = e.next;
            }
        }

        return true;
    }

    public boolean containsKey(Object key) {
        int idx = hash(key);
        HashEntry<K, V> e = buckets[idx];
        while (e != null) {
            if (e.key.equals(key)) {
                return true;
            }

            e = e.next;
        }

        return false;
    }

    public V get(Object key) {
        int idx = hash(key);
        HashEntry<K, V> e = buckets[idx];

        while (e != null) {
            if (e.key.equals(key)) {
                return e.value;
            }

            e = e.next;
        }

        return null;
    }

    public V put(K key, V value) {
        int idx = hash(key);
        HashEntry<K, V> e = buckets[idx];

        if (value == null) {
            throw new NullPointerException();
        }

        while (e != null) {
            if (e.key.equals(key)) {
                V r = e.value;
                e.value = value;
                return r;
            } else {
                e = e.next;
            }
        }

        if (++size > threshold) {
            rehash();
            idx = hash(key);
        }

        e = new HashEntry<>(key, value);
        e.next = buckets[idx];
        buckets[idx] = e;

        return null;
    }

    public V remove(Object key) {
        int idx = hash(key);
        HashEntry<K, V> e = buckets[idx];
        HashEntry<K, V> last = null;

        while (e != null) {
            if (e.key.equals(key)) {
                if (last == null) {
                    buckets[idx] = e.next;
                } else {
                    last.next = e.next;
                }

                size--;
                return e.value;
            }

            last = e;
            e = e.next;
        }

        return null;
    }

    public void clear() {
        if (size > 0) {
            Arrays.fill(buckets, null);
            size = 0;
        }
    }

    @SuppressWarnings("unchecked")
    private void rehash() {
        HashEntry<K, V>[] oldBuckets = buckets;

        int newCapacity = (2 * buckets.length) + 1;
        threshold = (int) (newCapacity * loadFactor);
        buckets = (HashEntry<K, V>[]) new HashEntry[newCapacity];

        for (int i = oldBuckets.length - 1; i >= 0; i--) {
            HashEntry<K, V> e = oldBuckets[i];
            while (e != null) {
                int idx = hash(e.key);
                HashEntry<K, V> dest = buckets[idx];

                if (dest != null) {
                    HashEntry<K, V> next = dest.next;
                    while (next != null) {
                        dest = next;
                        next = dest.next;
                    }

                    dest.next = e;
                } else {
                    buckets[idx] = e;
                }

                HashEntry<K, V> next = e.next;
                e.next = null;
                e = next;
            }
        }
    }

    private int hash(Object key) {
        int hash = key.hashCode() % buckets.length;
        return hash < 0 ? -hash : hash;
    }

    private static final class HashEntry<K, V> {
        private final K key;
        private V value;
        HashEntry<K, V> next;

        public HashEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public V setValue(V newVal) {
            V oldValue = value;
            if (newVal == null) throw new NullPointerException();
            value = newVal;
            return oldValue;
        }

        public V getValue() {
            return value;
        }

        public K getKey() {
            return key;
        }

        @Override
        public String toString() {
            return "HashEntry{" +
                    "key=" + key +
                    ", value=" + value +
                    ", next=" + next +
                    '}';
        }
    }
}
