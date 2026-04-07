import java.util.ArrayList;

public class MinHeap<K extends Comparable<K>>{
    private ArrayList<K> arr = new ArrayList<>();

    public MinHeap(){
        arr.add(null);
    }
    public int size(){
        return arr.size() - 1;
    }
    private void swap(int i, int j){
        K t = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, t);
    }
    public void add(K k){
        arr.add(k);
        swimUp(this.size());
    }
    private void swimUp(int i){
        while(i > 1 && arr.get(i/2).compareTo(arr.get(i)) > 0){
            swap(i, i/2);
            i = i/2;
        }
    }
    private void swimDown(int i){
        while(i * 2 <= this.size()){
            int min_index;
            if(i*2+1 <= this.size() && arr.get(i*2+1).compareTo(arr.get(i*2)) < 0){
                min_index = 2 * i + 1;
            }else{
                min_index = 2 * i;
            }
            if(arr.get(i).compareTo(arr.get(min_index)) <= 0) {
                return;
            }
            swap(i, min_index);
            i = min_index;
        }
    }

    public K deleteMin(){
        if(arr.size() <= 1) return null;
        K ans = arr.get(1);
        arr.set(1, arr.get(this.size()));
        arr.remove(this.size());
        if(this.size() > 1) swimDown(1);
        return ans;
    }
    public K getMin(){
        if(arr.size() <= 1) return null;
        return arr.get(1);
    }

}
