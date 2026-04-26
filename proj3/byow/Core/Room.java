package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import static java.lang.Math.max;
import static java.lang.Math.min;


public class Room {
    public int x;
    public int y;
    public int room_width;
    public int room_height;
    public int id;

    public Room(int a,int b,int c,int d,int i){
        x=a;
        y=b;
        room_width=c;
        room_height =d;
        id=i;
    }
    public void drawRoom(TETile[][] map){
        for(int i = x; i < x + room_width; i++){
            for(int j = y; j < y + room_height; j++){
                if(isValid(i,j,map)){
                    map[i][j] = Tileset.FLOOR;
                }
            }
        }
        for(int i = y-1; i <= y+ room_height; i++){
            if(isValid(x-1,i,map) && map[x-1][i].equals(Tileset.NOTHING)){
                map[x-1][i] = Tileset.WALL;
            }
            if(isValid(x+room_width,i,map) && map[x+room_width][i].equals(Tileset.NOTHING)){
                map[x+room_width][i] = Tileset.WALL;
            }
        }
        for(int i = x-1; i <= x+room_width; i++){
            if(isValid(i,y-1,map) && map[i][y-1].equals(Tileset.NOTHING)){
                map[i][y-1] = Tileset.WALL;
            }
            if(isValid(i,y+ room_height,map) && map[i][y+ room_height].equals(Tileset.NOTHING)){
                map[i][y+ room_height] = Tileset.WALL;
            }
        }
    }
    public static boolean isValid(int x, int y, TETile[][] map){
        return (x>=0 && x<map.length && y>=0 && y<map[0].length);
    }
    public static void connect(Room a, Room b, TETile[][] map){
        if(isContained(a, b)) return;
        int x1 = a.x + a.room_width / 2;
        int x2 = b.x + b.room_width / 2;
        int y1 = a.y + a.room_height / 2;
        int y2 = b.y + b.room_height / 2;
        Room.drawL(x1, y1, x2, y2, map);
    }
    public static void drawL(int x1, int y1, int x2, int y2, TETile[][] map){
        if(x1>x2){
            drawL(x2,y2,x1,y1, map);
            return;
        }
        for(int i = x1; i <= x2; i++){
            if(isValid(i,y1,map)) map[i][y1] = Tileset.FLOOR;
            if(isValid(i,y1-1,map) && map[i][y1-1] == Tileset.NOTHING) map[i][y1-1] = Tileset.WALL;
            if(isValid(i,y1+1,map) && map[i][y1+1] == Tileset.NOTHING) map[i][y1+1] = Tileset.WALL;
        }
        for(int i = min(y1, y2); i <= max(y1, y2); i++){
            if(isValid(x2,i,map)) map[x2][i] = Tileset.FLOOR;
            if(isValid(x2-1,i,map) && map[x2-1][i] == Tileset.NOTHING) map[x2-1][i] = Tileset.WALL;
            if(isValid(x2+1,i,map) && map[x2+1][i] == Tileset.NOTHING) map[x2+1][i] = Tileset.WALL;
        }
        if(y1<y2){
            if(isValid(x2+1,y1-1,map) && map[x2+1][y1-1] == Tileset.NOTHING) map[x2+1][y1-1] = Tileset.WALL;
        }
        if(y1>y2){
            if(isValid(x2+1,y1+1,map) && map[x2+1][y1+1] == Tileset.NOTHING) map[x2+1][y1+1] = Tileset.WALL;
        }
    }
    public static boolean isContained(Room a, Room b){
        boolean b1 = (a.x <= b.x && a.x + a.room_width >=b.x);
        boolean b2 = (b.x <= a.x && b.x + b.room_width >=a.x);
        boolean b3 = (a.y <=b.y && a.y + a.room_height >= b.y);
        boolean b4 = (b.y <=a.y && b.y + b.room_height >= a.y);
        return (b1 || b2) && (b3 || b4);
    }

}
