package byow.Core;

import byow.TileEngine.TETile;

import java.io.Serializable;
import java.util.Random;

public class GameState implements Serializable {
    public int WIDTH;
    public int HEIGHT;
    public TETile[][] map;
    public Avatar avatar;
    public Random random;

    public GameState(int width, int height, TETile[][] map, Avatar avatar, Random random) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.map = map;
        this.avatar = avatar;
        this.random = random;
    }
}
