package ru.otus.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.SoftReference;
import java.util.*;
import java.util.function.Consumer;

public class SoftCache<K, V> implements HwCache<K, V> {

    private final Logger logger = LoggerFactory.getLogger(SoftCache.class);

    private final Map<K, SoftReference<V>> softCache = new HashMap<>();
    private final Set<HwListener<K, V>> listeners = Collections.newSetFromMap(new WeakHashMap<>());

    private final long timeBetweenClean = 5_000;
    private long lastTimeCleaned = 0;

    @Override
    public void put(K key, V value) {
        deleteNullValues();

        final K aKey = Objects.requireNonNull(key);
        final V aValue = Objects.requireNonNull(value);

        //NOTE: Currently update is not supported by UserDao so hibernate will try to insert DETACHED object
        final String action = softCache.containsKey(key) ? "UPDATE" : "PUT";
        softCache.put(aKey, new SoftReference<>(aValue));

        notifyListeners(l -> l.notify(aKey, aValue, action));
    }

    @Override
    public void remove(K key) {
        deleteNullValues();

        final K aKey = Objects.requireNonNull(key);
        V aValue = softCache.remove(key).get();
        notifyListeners(l -> l.notify(aKey, aValue, "remove"));
    }

    @Override
    public V get(K key) {
        deleteNullValues();

        final K aKey = Objects.requireNonNull(key);
        SoftReference<V> vSoftReference = softCache.get(key);
        V value = vSoftReference.get();
        if (value != null) {
            notifyListeners(l -> l.notify(aKey, value, "get"));
        }

        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(Consumer<HwListener<K, V>> action) {
        for (HwListener<K, V> listener: listeners) {
            try {
                action.accept(listener);
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
        }
    }

    private void deleteNullValues() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis > lastTimeCleaned + timeBetweenClean) {
            lastTimeCleaned = currentTimeMillis;
            softCache.values().removeIf(value -> value.get() == null);
        }
    }
}
