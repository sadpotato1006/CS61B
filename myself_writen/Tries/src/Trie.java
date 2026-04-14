public class Trie {
    public Node root = new Node();

    public void add(String s){
        if(s == null || s.isEmpty()) return;
        Node curr = root;
        for(int i = 0; i < s.length(); i++){
            char c = s.charAt(i);
            curr.setNext(c);
            curr = curr.getNext(c);
        }
        curr.isKey = true;
    }

    public boolean exist(String s){
        if(s == null) return false;
        Node curr = root;
        for(int i = 0; i < s.length(); i++){
            char c = s.charAt(i);
            if(!curr.existNext(c)) return false;
            curr = curr.getNext(c);
        }
        return curr.isKey;
    }


}

