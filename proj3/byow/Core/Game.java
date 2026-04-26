package byow.Core;

import byow.TileEngine.TETile;

import java.util.Random;

public class Game {
    public int WIDTH;
    public int HEIGHT;
    public TETile[][] map;
    public Avatar avatar;
    public Random random;

    public Game(int w, int h){
        WIDTH = w;
        HEIGHT = h;
    }
    public TETile[][] play_with_input_string(String input){
        if(input.isEmpty()) return null;
        input = input.toLowerCase();
        if(input.charAt(0) == 'l') {

        }
        if(input.charAt(0) == 'n') {
            int index = find_seed_end(input);
            if(index == -1) return null;
            String seedString = input.substring(1, index);
            long seed = Long.parseLong(seedString);
            startNewGame(seed);
            runCommands(input, index + 1);
            return map;
        }
        return null;
    }
    public int find_seed_end(String input){
        int i = 1;
        while(input.charAt(i) != 's' && input.charAt(i) != 'S'){
            i++;
            if(i >= input.length()){
                return -1;
            }
        }
        return i;
    }
    public void startNewGame(long seed){
        this.random = new Random(seed);
        WorldGenerate generate = new WorldGenerate(WIDTH, HEIGHT, random);
        this.map = generate.generateWorld();
        this.avatar = new Avatar(map, random);
    }
    public void runCommands(String input, int index){
        while(index < input.length()){
            char c = input.charAt(index);
            index++;
            if(c == ':'){
                char c2 = input.charAt(index);
                if(c2 == 'q' || c2 == 'Q'){
                    avatar.save_and_exit();
                    index++;
                    break;
                }
            }
            if(c == 'w' || c == 'W'){
                avatar.moveHelper(0, 1, map);
                continue;
            }
            if(c == 'a' || c == 'A'){
                avatar.moveHelper(-1, 0, map);
                continue;
            }
            if(c == 's' || c == 'S'){
                avatar.moveHelper(0, -1, map);
                continue;
            }
            if(c == 'd' || c == 'D'){
                avatar.moveHelper(1, 0, map);
                continue;
            }
        }
    }
}
