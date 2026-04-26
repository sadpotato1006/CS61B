package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.Random;

public class Avatar implements Serializable {
    public int x;
    public int y;
    public TETile curr = Tileset.FLOOR;
    public Avatar(TETile[][] map, Random random){
        while(true){
            int xx = random.nextInt(map.length);
            int yy = random.nextInt(map[0].length);
            if(!isValid(xx, yy, map) || !map[xx][yy].equals(Tileset.FLOOR)) continue;
            this.x = xx;
            this.y = yy;
            map[this.x][this.y] = Tileset.AVATAR;
            break;
        }
    }
    public void moveHelper(int dx, int dy, TETile[][] map){
        int xx = this.x + dx;
        int yy = this.y + dy;
        if(!isValid(xx, yy, map) || map[xx][yy].equals(Tileset.WALL) || map[xx][yy].equals(Tileset.NOTHING)) return;
        map[this.x][this.y] = curr;
        this.curr = map[xx][yy];
        this.x += dx;
        this.y += dy;
        map[this.x][this.y] = Tileset.AVATAR;
    }
    private boolean isValid(int x, int y, TETile[][] map){
        int w = map.length;
        int h = map[0].length;
        return x >= 0 && x < w && y >= 0 && y < h;
    }

}
