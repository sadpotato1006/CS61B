package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import static java.lang.Math.max;
import static java.lang.Math.min;


public class Room {
    public int x;
    public int y;
    public int width;
    public int height;
    public int id;

    public Room(int a,int b,int c,int d,int i){
        x=a;
        y=b;
        width=c;
        height=d;
        id=i;
    }
    public void drawRoom(TETile[][] ans){
        for(int i = x; i < x + width; i++){
            for(int j = y; j < y + height; j++){
                if(isValued(i,j)){
                    ans[i][j] = Tileset.FLOOR;
                }
            }
        }
        for(int i = y-1; i <= y+height; i++){
            if(isValued(x-1,i) && ans[x-1][i].equals(Tileset.NOTHING)){
                ans[x-1][i] = Tileset.WALL;
            }
            if(isValued(x+width,i) && ans[x+width][i].equals(Tileset.NOTHING)){
                ans[x+width][i] = Tileset.WALL;
            }
        }
        for(int i = x-1; i <= x+width; i++){
            if(isValued(i,y-1) && ans[i][y-1].equals(Tileset.NOTHING)){
                ans[i][y-1] = Tileset.WALL;
            }
            if(isValued(i,y+height) && ans[i][y+height].equals(Tileset.NOTHING)){
                ans[i][y+height] = Tileset.WALL;
            }
        }
    }
    public static boolean isValued(int x, int y){
        return (x>=0 && x<WorldGenerate.WIDTH && y>=0 && y<WorldGenerate.HEIGHT);
    }
    public static void connect(Room a, Room b, TETile[][] ans){
        if(isContained(a, b)) return;
        int x1 = a.x + a.width / 2;
        int x2 = b.x + b.width / 2;
        int y1 = a.y + a.height / 2;
        int y2 = b.y + b.height / 2;
        Room.drawL(x1, y1, x2, y2, ans);
    }
    public static void drawL(int x1, int y1, int x2, int y2, TETile[][] ans){
        if(x1>x2){
            drawL(x2,y2,x1,y1, ans);
            return;
        }
        for(int i = x1; i <= x2; i++){
            if(isValued(i,y1)) ans[i][y1] = Tileset.FLOOR;
            if(isValued(i,y1-1) && ans[i][y1-1] == Tileset.NOTHING) ans[i][y1-1] = Tileset.WALL;
            if(isValued(i,y1+1) && ans[i][y1+1] == Tileset.NOTHING) ans[i][y1+1] = Tileset.WALL;
        }
        for(int i = min(y1, y2); i <= max(y1, y2); i++){
            if(isValued(x2,i)) ans[x2][i] = Tileset.FLOOR;
            if(isValued(x2-1,i) && ans[x2-1][i] == Tileset.NOTHING) ans[x2-1][i] = Tileset.WALL;
            if(isValued(x2+1,i) && ans[x2+1][i] == Tileset.NOTHING) ans[x2+1][i] = Tileset.WALL;
        }
        if(y1<y2){
            if(isValued(x2+1,y1-1) && ans[x2+1][y1-1] == Tileset.NOTHING) ans[x2+1][y1-1] = Tileset.WALL;
        }
        if(y1>y2){
            if(isValued(x2+1,y1+1) && ans[x2+1][y1+1] == Tileset.NOTHING) ans[x2+1][y1+1] = Tileset.WALL;
        }
    }
    public static boolean isContained(Room a, Room b){
        boolean b1 = (a.x <= b.x && a.x + a.width >=b.x);
        boolean b2 = (b.x <= a.x && b.x + b.width >=a.x);
        boolean b3 = (a.y <=b.y && a.y + a.height >= b.y);
        boolean b4 = (b.y <=a.y && b.y + b.height >= a.y);
        return (b1 || b2) && (b3 || b4);
    }

}
