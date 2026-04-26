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
    public boolean light_open = true;
    public boolean is_limited_vision = false;
    public int limited_vision = 5;

    public GameState(int width, int height, TETile[][] map, Avatar avatar, Random random, boolean light_open, boolean is_limited_vision, int limited_vision) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.map = map;
        this.avatar = avatar;
        this.random = random;
        this.is_limited_vision = is_limited_vision;
        this.limited_vision = limited_vision;
        this.light_open = light_open;
    }
}
