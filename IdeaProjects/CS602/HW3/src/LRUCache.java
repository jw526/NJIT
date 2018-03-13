//Jianchao Wang

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
    private Persister persister;
    private int size;
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
            else return (K) (lruList.get(current++));
        }
    }

    /**
     * Constructor
     * @param size initial size of the cache which can change later
     * @param persister persister instance to use for accessing/modifying evicted items
     */
    public LRUCache(int size, Persister<? extends K,? extends T> persister) {
        if (size <=0) throw new IllegalArgumentException("size not valid");
        itemMap = new HashMap<>(size);
        lruList = new LinkedList<>();
        //lruList = new ArrayList<>();
        this.size = size;
        this.persister = persister;
    }

    /**
     * Modify the cache size
     * @param newSize
     */
    public void modifySize(int newSize) {
        ArrayList<K> tempL = new ArrayList();
        Map<K,Cacheable<K>> tempM = new HashMap<>(newSize);
        Iterator<K> it = getCacheKeys();
        int i = 0;
        while (it.hasNext()&& i < newSize ){
            K k = it.next();
            tempL.add(k);
            tempM.put(k,persister.getValue(k));
            i++;
        }
        lruList.clear();
        itemMap.clear();
        lruList.addAll(tempL);
        itemMap.putAll(tempM);
        this.size = newSize;
    }


    /**
     * Get item with the key (need to get item even if evicted)
     * @param key
     * @return
     */
    public T getItem(K key) {
        Cacheable item = persister.getValue(key);

        //if in the cache
        if (lruList.contains(key)){
            lruList.remove(key);
            lruList.add(0, key);
            return (T) item;
        }
        //not in cache but in persister
        if (persister.getValue(key)!= null) {
            faults++;
            if (lruList.size()>=size){
                //remove from cache based on LRU
                K k = lruList.remove(lruList.size()-1);
                itemMap.remove(k);
                return (T) item;
            }
            lruList.add(0, key);
            itemMap.put(key,item);
            return (T) item;
        }
        //not in either, return null
        return null;
    }

    /**
     * Add/Modify item with the key
     * @param item item to be put
     */
    public void putItem(T item) {
        //new item put in both persister and cache
        if (persister.getValue(item.getKey())==null){
            persister.persistValue(item);
            if (lruList.size()>=size){
                //remove from cache based on LRU
                K k = lruList.remove(lruList.size()-1);
                itemMap.remove(k);
            }
            lruList.add(0, item.getKey());
            itemMap.put(item.getKey(),item);
        }
        else {
            persister.persistValue(item);
            counter++;
        }
    }


    /**
     * Remove an item with the key
     * @param key
     * @return item removed or null if it does not exist
     */
    public T removeItem(K key) {
       if(itemMap.containsKey(key)||lruList.contains(key)){
            lruList.remove(key);
            itemMap.remove(key);
            return (T) (persister.removeValue(key));
       }
       return null;
    }

    /**
     * Get cache keys
     * @return
     */
    public Iterator<K> getCacheKeys() {
        return new CacheKeyIterator<>();
    }

    /**
     * Get fault rate (proportion of accesses (only for retrievals and modifications) not in cache)
     * @return
     */
    public double getFaultRatePercent() {
        return (counter==0? 0: (double) faults/counter *100);
    }

    /**
     * Reset fault rate stats counters
     */
    public void resetFaultRateStats() {
        counter = 0;
        faults = 0;
    }

    public static void main(String [] args) {
        LRUCache<String,SimpleCacheItem> cache = new LRUCache<>(10, new SimpleFakePersister<>());
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
        //for(String s: cache.lruList) {
         //   System.out.println(s);
        //}
        cache.resetFaultRateStats();
        cache.modifySize(50);
        for(String s: cache.lruList) {
            System.out.println(s);
        }
        long start = System.currentTimeMillis();
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
        Iterator it = cache.getCacheKeys();
        while(it.hasNext()){
            System.out.println(it.next());
        }
        System.out.println("Time used: "+(System.currentTimeMillis()-start)+" ms");
        System.out.println(cache.lruList.size());
        System.out.println(cache.itemMap.size());
    }

}
