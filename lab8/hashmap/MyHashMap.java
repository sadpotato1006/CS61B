package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private double maxLoad;
    private int elementNum;
    private int bucketNum;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        this(16, 0.75);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, 0.75);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this.maxLoad = maxLoad;
        this.elementNum = 0;
        this.buckets = createTable(initialSize);
        this.bucketNum = initialSize;
    }

    public void clear(){
        this.elementNum = 0;
        this.buckets = null;
        this.bucketNum = 16;
    }

    public boolean containsKey(K key){
        return (this.get(key) != null);
    }

    public V get(K key){
        if(this.buckets == null) return null;
        int hash = Math.floorMod(key.hashCode(), this.bucketNum);
        if (this.buckets[hash] != null) {
            for (Node a : this.buckets[hash]) {
                if (a.key.equals(key)) {
                    return a.value;
                }
            }
        }
        return null;
    }

    public int size(){
        return this.elementNum;
    }

    public void put(K key, V value){
        int hash = Math.floorMod(key.hashCode(), this.bucketNum);
        if(this.buckets[hash] == null){
            this.buckets[hash] = createBucket();
        }
        for (Node a : this.buckets[hash]) {
            if (a.key.equals(key)) {
                a.value = value;
                return;
            }
        }
        this.buckets[hash].add(createNode(key, value));
        this.elementNum += 1;
        //check whether required resize or not
        if((double) this.elementNum / this.bucketNum > this.maxLoad){
            this.resize();
        }
    }

    private void resize(){
        this.bucketNum *= 2;
        MyHashMap<K, V> t = new MyHashMap<>(this.bucketNum, this.maxLoad);
        for(int i = 0; i < this.buckets.length; i++){
            if(this.buckets[i] != null){
                for(Node a : this.buckets[i]){
                    t.put(a.key, a.value);
                }
            }
        }
        this.buckets = t.buckets;
    }

    public Set<K> keySet(){
        Set<K> s = new HashSet<>();
        for (K k : this) {
            s.add(k);
        }
        return s;
    }

    public V remove(K key){
        V value = this.get(key);
        if(value == null) return null;
        return remove(key, value);
    }

    public V remove(K key, V value){
        V v = this.get(key);
        if(v == null || !v.equals(value)) return null;
        int hash = Math.floorMod(key.hashCode(), this.bucketNum);
        if(this.buckets[hash] == null) return null;
        for(Node a : this.buckets[hash]){
            if(a.key.equals(key)){
                this.buckets[hash].remove(a);
                this.elementNum -= 1;
                return v;
            }
        }
        return null;
    }

    public Iterator<K> iterator(){
        return new hash_map_iterator();
    }
    private class hash_map_iterator implements Iterator<K>{
        int size;
        int sum_visited;
        int curr;
        Iterator<Node> it;
        public hash_map_iterator(){
            this.size = elementNum;
            this.sum_visited = 0;
            this.it = null;
        }

        @Override
        public boolean hasNext() {
            return this.sum_visited < this.size;
        }

        @Override
        public K next() {
            while(this.it == null || !it.hasNext()){
                if(buckets[curr] != null){
                    it = buckets[curr].iterator();
                }
                curr++;
            }
            sum_visited++;
            return it.next().key;
        }
    }
    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    @SuppressWarnings("unchecked")
    private Collection<Node>[] createTable(int tableSize) {
        this.bucketNum = tableSize;
        return (Collection<Node>[]) new Collection[tableSize];
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!



}
