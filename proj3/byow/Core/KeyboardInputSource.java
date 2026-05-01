package byow.Core;

import edu.princeton.cs.introcs.StdDraw;

public class KeyboardInputSource implements InputSource{

    public boolean hasNextKey(){
        return true;
    }

    public char getNextKey(){
        while(!StdDraw.hasNextKeyTyped()){
            StdDraw.pause(20);
        }
        return StdDraw.nextKeyTyped();
    }
}
