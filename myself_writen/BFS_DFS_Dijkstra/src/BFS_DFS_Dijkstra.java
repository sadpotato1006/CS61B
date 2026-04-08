import java.util.*;

public class BFS_DFS_Dijkstra {
    int v;
    int e;
    private ArrayList<ArrayList<edge>> arr_edge = new ArrayList<>();

    private class point implements Comparable<point>{
        int id;
        int sum_dis;
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
    private class edge{
        int to;
        int dis;
        public edge(int t, int d){
            to = t;
            dis = d;
        }
    }
    public BFS_DFS_Dijkstra(int v, int e){
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
            arr_edge.get(s).add(new edge(t, dis));
        }
    }

    public void DFS(int s){ //s -> source
        boolean[] visited = new boolean[v];
        helper_DFS(s, visited);
        System.out.println();
    }
    private void helper_DFS(int s, boolean[] visited){ //前序遍历，不回溯
        if(visited[s]) return;
        visited[s] = true;
        System.out.print(s + " ");
        for(edge e : arr_edge.get(s)){
            helper_DFS(e.to, visited);
        }
    }

    public void BFS(int s){ //s -> source ////前序遍历，不回溯
        Queue<Integer> q = new LinkedList<>();
        boolean[] visited = new boolean[v];
        q.add(s);
        visited[s] = true;
        while(!q.isEmpty()){
            int curr = q.remove();
            System.out.print(curr + " ");
            for(edge e : arr_edge.get(curr)){
                if(!visited[e.to]){
                    q.add(e.to);
                    visited[e.to] = true;
                }
            }
        }
    }

    public void Dijkstra(int s){ //s -> source
        int[] ans = new int[this.v];
        for(int i = 0; i < v; i++){
            ans[i] = 2100000000;
        }
        PriorityQueue<point> pq = new PriorityQueue<>();
        pq.add(new point(s, 0));
        while(!pq.isEmpty()){
            point curr = pq.remove();
            int id = curr.id;
            if(ans[id] != 2100000000) continue;
            ans[id] = curr.sum_dis;
            for(edge e : arr_edge.get(id)){
                if(ans[e.to] == 2100000000){
                    pq.add(new point(e.to, curr.sum_dis + e.dis));
                }
            }
        }
        for(int i = 0; i < v; i++){
            System.out.println("from s to " + i + " distance is " + ans[i]);
        }
    }



}
