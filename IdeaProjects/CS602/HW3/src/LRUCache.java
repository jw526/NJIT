//package edu.njit.cs602.s2018.assignments;

import java.util.*;

/**
 * Created by Ravi Varadarajan on 2/20/2018.
 */
public class LRUCache<K,T extends Cacheable<K>> {


    // Stores items for look up
    private final Map<K,Cacheable<K>> itemMap;

    // list in LRU order
    private final List<K> lruList;

    private LRUCache.CacheKeyIterator itr;
    private Persister persister;
    private int size;
    private Cacheable item;
    //private K key;
    private int faults = 0;
    private int counter = 0;


    /**
     * To be implemented!!
     * Iterator class only for only keys of cached items; order should be in LRU order, most recently used first
     * @param <K>
     */
    private class CacheKeyIterator<K> implements Iterator<K> {
        int current = 0;

        @Override
        public boolean hasNext() {
           return (current < lruList.size());
        }

        @Override
        public K next() {
            if (!hasNext()) throw new NoSuchElementException();
            return (K) (lruList.get(++current));
        }
    }

    /**
     * Constructor
     * @param size initial size of the cache which can change later
     * @param persister persister instance to use for accessing/modifying evicted items
     */
    public LRUCache(int size, Persister<? extends K,? extends T> persister) {
        itemMap = new HashMap<>(size);
        lruList = new ArrayList<>(size);
        this.size = size;
        this.itr = new CacheKeyIterator<K>();
        this.persister = persister;
    }

    /**
     * Modify the cache size
     * @param newSize
     */
    public void modifySize(int newSize) {
        this.size = newSize;
    }


    /**
     * Get item with the key (need to get item even if evicted)
     * @param key
     * @return
     */
    public T getItem(K key) {
        counter++;
        //if in the cache
        if (itemMap.get(key)!=null) return (T) itemMap.get(key);
        //not in cache but in persister
        if (persister.getValue(key)!= null) {
            item = persister.getValue(key);
            faults++;
            if (lruList.size()<size){
                lruList.add(0,key);
                itemMap.put(key,item);
                //itemMap.put(key,item);
                return (T) item;
            }
            else{
                //remove from cache based on LRU
                removeItem(key);
                lruList.add(0,key);
                itemMap.put(key,item);
                return (T) item;
            }
        }
        //not in both, return null
        return null;
    }

    /**
     * Add/Modify item with the key
     * @param item item to be put
     */
    public void putItem(T item) {
        persister.persistValue(item);
    }


    /**
     * Remove an item with the key
     * @param key
     * @return item removed or null if it does not exist
     */
    public T removeItem(K key) {
        if(itr.hasNext()){
            lruList.remove(lruList.size()-1);
            return (T) (itemMap.remove(key));
        }
        return null;
    }

    /**
     * Get cache keys
     * @return
     */
    public Iterator<K> getCacheKeys() {
        return new CacheKeyIterator<K>();
    }

    /**
     * Get fault rate (proportion of accesses (only for retrievals and modifications) not in cache)
     * @return
     */
    public double getFaultRatePercent() {
        return (counter == 0 ? 0:(double) faults/counter);
    }

    /**
     * Reset fault rate stats counters
     */
    public void resetFaultRateStats() {
        counter = 0;
        faults = 0;
    }

    public static void main(String [] args) {
        LRUCache<String,SimpleCacheItem> cache = new LRUCache<>(20, new SimpleFakePersister<>());
        for (int i=0; i < 100; i++) {
            cache.putItem(new SimpleCacheItem("name"+i, (int) (Math.random()*200000)));
            String name = "name" + (int) (Math.random() * i);
            SimpleCacheItem cacheItem = cache.getItem(name);
            if (cacheItem != null) {
                System.out.println("Salary for " + name + "=" + cacheItem.getAnnualSalary());
            }
            cache.putItem(new SimpleCacheItem("name"+ (int) (Math.random() * i), (int) (Math.random()*200000)));
            name = "name" + (int) (Math.random() * i);
            cache.removeItem(name);
            System.out.println("Fault rate percent=" + cache.getFaultRatePercent());
        }
        for (int i=0; i < 30; i++) {
            String name = "name" + (int) (Math.random() * i);
            cache.removeItem(name);
        }
        cache.resetFaultRateStats();
        cache.modifySize(50);
        for (int i=0; i < 100; i++) {
            cache.putItem(new SimpleCacheItem("name"+i, (int) (Math.random()*200000)));
            String name = "name" + (int) (Math.random() * i);
            SimpleCacheItem cacheItem = cache.getItem(name);
            if (cacheItem != null) {
                System.out.println("Salary for " + name + "=" + cacheItem.getAnnualSalary());
            }
            cache.putItem(new SimpleCacheItem("name"+ (int) (Math.random() * i), (int) (Math.random()*200000)));
            name = "name" + (int) (Math.random() * i);
            cache.removeItem(name);
            System.out.println("Fault rate percent=" + cache.getFaultRatePercent());
        }
    }



}
