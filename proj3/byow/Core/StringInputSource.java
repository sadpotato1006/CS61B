package byow.Core;

public class StringInputSource implements InputSource{
    private String key_string;
    private int index;

    public StringInputSource(String s){
        this.key_string = s;
        this.index = 0;
    }

    public boolean hasNextKey(){
        return index < key_string.length();
    }

    public char getNextKey(){
        return key_string.charAt(index++);
    }
}
