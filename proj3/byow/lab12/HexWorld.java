package byow.lab12;
import org.junit.Test;

import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(7);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.SAND;
            case 3: return Tileset.GRASS;
            case 4: return Tileset.WATER;
            case 5: return Tileset.MOUNTAIN;
            case 6: return Tileset.TREE;
            default: return Tileset.NOTHING;
        }
    }
    public static void fillWithNothing(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }
    private static void fillWithHex(TETile[][] tiles, int s){
        int x = 10;
        int y = 8;
        int[] num = {3, 4, 5, 4, 3};
        for(int i = 0; i < num.length; i++){
            int xx = x + i * (2 * s - 1);
            int yy = y;
            for(int j = 0; j < num[i]; j++){
                addHexagon(tiles, s, xx, yy);
                yy += 2 * s;
            }
            if (i < num.length - 1) {
                // 下一列更高，说明下一列要往下错开 s 格
                if (num[i + 1] > num[i]) {
                    y -= s;
                }
                // 下一列更矮，说明下一列要往上错开 s 格
                else {
                    y += s;
                }
            }
        }


    }
    public static void addHexagon(TETile[][] tiles, int s, int x, int y){
        TETile tet = randomTile();
        for(int i = 0; i < s ; i++){
            for(int j = 0; j < s + 2 * i; j++){
                tiles[x + j - i][y + i] = tet;
            }
        }
        x = x - s + 1;
        y = y + s;
        for(int i = 0; i < s; i++){
            for(int j = 0; j < (3 * s - 2) - 2 * i; j++){
                tiles[x + j + i][y + i] = tet;
            }
        }
//        x = x + s - 1;
//        y = y - s;
//
//        int[] dx = {0, 2*s-1, 2*s-1, 0, -2*s+1, -2*s+1};
//        int[] dy = {2*s, s, -s, -2*s, -s, s};
//        for(int i = 0; i < 6; i++){
//            int xx = x + dx[i], yy = y + dy[i];
//            if(xx - s + 1 >= 0 && xx + 2*s - 2 < WIDTH
//                    && yy >= 0 && yy + 2*s - 1  < HEIGHT
//                    && tiles[xx][yy].equals(Tileset.NOTHING)
//                    && tiles[xx + s - 1][yy].equals(Tileset.NOTHING)
//                    && tiles[xx][yy + 2 * s - 1].equals(Tileset.NOTHING)
//                    && tiles[xx + s - 1][yy + 2 * s - 1].equals(Tileset.NOTHING)
//                    && tiles[xx - s + 1][yy + s - 1].equals(Tileset.NOTHING)
//                    && tiles[xx - s + 1][yy + s].equals(Tileset.NOTHING)
//                    && tiles[xx + 2 * s - 2][yy + s].equals(Tileset.NOTHING)
//                    && tiles[xx + 2 * s - 2][yy + s - 1].equals(Tileset.NOTHING)){
//                addHexagon(tiles, s, xx, yy);
//            }
//        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] mainTiles = new TETile[WIDTH][HEIGHT];

        fillWithNothing(mainTiles);

        fillWithHex(mainTiles, 4);

        ter.renderFrame(mainTiles);
    }
}
