package byow.Core;

import edu.princeton.cs.introcs.StdDraw;

public class KeyboardInputSource implements InputSource{

    public boolean hasNextKey(){
        return StdDraw.hasNextKeyTyped();
    }

    public char getNextKey(){
        return Character.toLowerCase(StdDraw.nextKeyTyped());
    }

    // 这个输入源是否已经彻底结束
    public boolean isExhausted(){
        return false;
    }
}
