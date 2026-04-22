
import java.util.HashMap;

public class Node{
    public boolean isKey = false;
    private final HashMap<Character, Node> next = new HashMap<>();
    public Node(){

    }

    public Node getNext(char c){
        return this.next.get(c);
    }

    public void setNext(char c){
        if(this.next.containsKey(c)) return;
        this.next.put(c, new Node());
    }

    public boolean existNext(char c){
        return this.getNext(c) != null;
    }
}
