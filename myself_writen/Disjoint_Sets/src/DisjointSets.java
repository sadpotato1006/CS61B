public class DisjointSets {
    private int size;
    private int[] arr;

    public DisjointSets(int s){
        this.size = s;
        this.arr = new int[size];
        for(int i = 0; i < this.size; i++){
            arr[i] = -1;
        }
    }

    public int size(){
        return this.size;
    }

    public void connect(int x, int y){
        int rx = root(x);
        int ry = root(y);
        if(rx == ry){
            return;
        }
        if(-arr[rx] <= -arr[ry]){
            arr[ry] += arr[rx];
            arr[rx] = ry;
        }
        else{
            arr[rx] += arr[ry];
            arr[ry] = rx;
        }
    }

    private int root(int x){
        if(arr[x] < 0){
            return x;
        }
        arr[x] = root(arr[x]);
        return arr[x];
    }

    public boolean isConnected(int x, int y){
        return root(x) == root(y);
    }

}
