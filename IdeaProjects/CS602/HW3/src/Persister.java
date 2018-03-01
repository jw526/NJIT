//package edu.njit.cs602.s2018.assignments;

/**
 * Created by Ravi Varadarajan on 2/20/2018.
 */
public interface Persister<K, T extends Cacheable<K>> {
    /**
     * persist value to be retrieved later
     * @param value
     */
    void persistValue(T value);

    /**
     * Get value for key from persistent store
     * @param key
     * @return
     */
    T getValue(K key);

    /**
     * Remove value associated with key from persistent store
     * @param key
     * @return
     */
    T removeValue(K key);
}
