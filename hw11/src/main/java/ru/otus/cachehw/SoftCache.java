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

    private static final long TIME_BETWEEN_CLEAN = 5_000;
    private long lastTimeCleaned = 0;

    private static String ACTION_UPDATE = "UPDATE";
    private static String ACTION_PUT = "PUT";
    private static String ACTION_REMOVE = "REMOVE";
    private static String ACTION_GET = "GET";

    @Override
    public void put(K key, V value) {
        deleteNullValues();

        final K aKey = Objects.requireNonNull(key);
        final V aValue = Objects.requireNonNull(value);

        //NOTE: Currently update is not supported by UserDao so hibernate will try to insert DETACHED object
        final String action = softCache.containsKey(key) ? ACTION_UPDATE : ACTION_PUT;
        softCache.put(aKey, new SoftReference<>(aValue));

        notifyListeners(l -> l.notify(aKey, aValue, action));
    }

    @Override
    public void remove(K key) {
        deleteNullValues();

        final K aKey = Objects.requireNonNull(key);
        V aValue = softCache.remove(key).get();
        notifyListeners(l -> l.notify(aKey, aValue, ACTION_REMOVE));
    }

    @Override
    public V get(K key) {
        deleteNullValues();

        final K aKey = Objects.requireNonNull(key);
        SoftReference<V> vSoftReference = softCache.get(key);

        if (vSoftReference == null) {
            return null;
        }

        V value = vSoftReference.get();
        if (value != null) {
            notifyListeners(l -> l.notify(aKey, value, ACTION_GET));
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
        if (currentTimeMillis > lastTimeCleaned + TIME_BETWEEN_CLEAN) {
            lastTimeCleaned = currentTimeMillis;
            softCache.values().removeIf(value -> value.get() == null);
        }
    }
}