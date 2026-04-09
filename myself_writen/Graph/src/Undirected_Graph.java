import java.lang.constant.Constable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Undirected_Graph {
    int v;
    int e;
    private ArrayList<ArrayList<edge>> arr_edge = new ArrayList<>();

    private static class point implements Comparable<point>{
        public int id;
        public int sum_dis;
        public point(int i){
            id = i;
            sum_dis = 0;
        }
        public point(int i, int s){
            id = i;
            sum_dis = s;
        }
        @Override
        public int compareTo(point p){
            return this.sum_dis - p.sum_dis;
        }
    }

    private static class edge implements Comparable<edge> {
        public int from;
        public int to;
        public int dis;
        public edge(int f, int t, int d){
            from = f;
            to = t;
            dis = d;
        }
        @Override
        public int compareTo(edge e){
            return this.dis - e.dis;
        }
    }

    public Undirected_Graph(int v, int e){
        this.v = v;
        this.e = e;
        for(int i = 0; i < v; i++){
            arr_edge.add(i, new ArrayList<>());
        }
        Scanner scanner = new Scanner(System.in); //输入
        for(int i = 0; i < e; i++){
            int s = scanner.nextInt();
            int t = scanner.nextInt();
            int dis = scanner.nextInt();
            arr_edge.get(s).add(new edge(s, t, dis));
            arr_edge.get(t).add(new edge(t, s, dis));
        }
    }

    public void prim(int s){  //s -> source
        ArrayList<edge> ans = new ArrayList<>();
        boolean[] visited = new boolean[this.v];
        PriorityQueue<edge> pq = new PriorityQueue<>();
        pq.add(new edge(s, s, 0));
        while(!pq.isEmpty()){
            edge curr = pq.remove();
            if(visited[curr.to]) continue;
            if(curr.from != curr.to) ans.add(curr);
            visited[curr.to] = true;
            for(edge e : arr_edge.get(curr.to)){
                if(visited[e.to]) continue;
                pq.add(e);
            }
        }
        for(edge e : ans){
            System.out.println(e.from + "-> " + e.to + "  distance: " + e.dis);
        }
    }

    public void kruskal(){
        ArrayList<edge> arr = new ArrayList<>();
        ArrayList<edge> ans = new ArrayList<>();
        for(ArrayList<edge> a : arr_edge){
            for(edge e : a){
                if(e.from < e.to){
                    arr.add(e);
                }
            }
        }
        arr.sort(null);
        DisjointSets un = new DisjointSets(this.v);
        for(edge e : arr){
            if(un.isConnected(e.from, e.to)) continue;
            un.connect(e.from, e.to);
            ans.add(e);
        }
        for(edge e : ans){
            System.out.println(e.from + "-> " + e.to + "  distance: " + e.dis);
        }

    }

    public static class DisjointSets {
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

}
