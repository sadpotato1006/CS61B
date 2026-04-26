package byow.Core;

import byow.TileEngine.TETile;

import java.io.*;
import java.util.Random;

public class Game implements Serializable {
    public int WIDTH;
    public int HEIGHT;
    public TETile[][] map;
    public Avatar avatar;
    public Random random;
    public static final File SAVE_FILE = new File("save_file");

    public Game(int w, int h){
        WIDTH = w;
        HEIGHT = h;
    }
    public TETile[][] play_with_input_string(String input){
        if(input.isEmpty()) return null;
        input = input.toLowerCase();
        if(input.charAt(0) == 'l') {
            Game loadedGame = this.loadGame();
            if (loadedGame == null) {
                return null;
            }
            this.WIDTH = loadedGame.WIDTH;
            this.HEIGHT = loadedGame.HEIGHT;
            this.map = loadedGame.map;
            this.avatar = loadedGame.avatar;
            this.random = loadedGame.random;
            runCommands(input, 1);
            return map;
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
        while(i < input.length()){
            if(input.charAt(i) == 's'){
                return i;
            }
            i++;
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
                if(index+1 >= input.length()) return;
                char c2 = input.charAt(index);
                if(c2 == 'q'){
                    this.saveAndQuit();
                    break;
                }
                index += 2;
                continue;
            }
            if(c == 'w'){
                avatar.moveHelper(0, 1, map);
                continue;
            }
            if(c == 'a'){
                avatar.moveHelper(-1, 0, map);
                continue;
            }
            if(c == 's'){
                avatar.moveHelper(0, -1, map);
                continue;
            }
            if(c == 'd'){
                avatar.moveHelper(1, 0, map);
                continue;
            }
        }
    }
    public void saveAndQuit(){
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVE_FILE));
            out.writeObject(this);
            out.close();
        } catch (Exception e) {
            System.out.println("Save failed: " + e.getMessage());
        }
    }
    public Game loadGame(){
        if (!SAVE_FILE.exists()) {
            return null;
        }
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(SAVE_FILE));
            Game loadedGame = (Game) in.readObject();
            in.close();
            return loadedGame;
        } catch (Exception e) {
            System.out.println("Load failed: " + e.getMessage());
            return null;
        }
    }

}
