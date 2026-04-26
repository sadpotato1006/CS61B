package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;
import org.checkerframework.checker.units.qual.A;

import javax.lang.model.type.ArrayType;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public class Game {
    public int WIDTH;
    public int HEIGHT;
    public TETile[][] map;
    public Avatar avatar;
    public Random random;
    private boolean waitingForColonCommand = false;
    public boolean shouldQuit = false;
    public boolean light_open = true;
    public boolean is_limited_vision = false;
    public int limited_vision = 5;


    public Game(int w, int h){
        WIDTH = w;
        HEIGHT = h;
    }
    public void startNewGame(long seed){
        this.random = new Random(seed);
        WorldGenerate generate = new WorldGenerate(WIDTH, HEIGHT, random);
        this.map = generate.generateWorld();
        this.avatar = new Avatar(map, random);
    }
    public TETile[][] play_with_input_string(String input){
        if(input.isEmpty()) return null;
        input = input.toLowerCase();
        if(input.charAt(0) == 'l') {
            if(!loadGame()) return null;
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
    public void play_with_keyboard(TERenderer ter){
        showLogo();
        char choice = readMenuChoice();
        if (choice == 'n') {
            long seed = readSeedFromKeyboard();
            startNewGame(seed);
        }
        else if (choice == 'l') {
            boolean loaded = loadGame();
            if (!loaded) {
                return;
            }
        }
        else if (choice == 'q') {
            return;
        }
        while(!this.shouldQuit){
            showAll(ter);
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                this.handleKey(key);
            }
            StdDraw.pause(20);
        }
    }
    private char readMenuChoice() {
        while (true) {
            if (!StdDraw.hasNextKeyTyped()) {
                StdDraw.pause(20);
                continue;
            }
            char key = Character.toLowerCase(StdDraw.nextKeyTyped());
            if (key == 'n' || key == 'l' || key == 'q') {
                return key;
            }
        }
    }
    public long readSeedFromKeyboard(){
        StringBuilder seed = new StringBuilder();
        while(true) {
            drawSeedScreen(seed.toString());
            if (!StdDraw.hasNextKeyTyped()) {
                StdDraw.pause(20);
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            if((key == 's' || key == 'S') && seed.length() > 0) break;
            if (Character.isDigit(key)) {
                seed.append(key);
            }
        }
        return Long.parseLong(seed.toString());
    }
    private void drawSeedScreen(String seed) {
        StdDraw.clear(Color.BLACK);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH * 0.5, HEIGHT * 0.65, "Enter Seed");
        StdDraw.text(WIDTH * 0.5, HEIGHT * 0.5, "N" + seed + "S");
        StdDraw.text(WIDTH * 0.5, HEIGHT * 0.35, "Press S to start");
        StdDraw.show();
    }
    public int find_seed_end(String input){
        int i = 1;
        while(i < input.length()){
            if(input.charAt(i) == 's'){
                return i;
            }
            i++;
        }
        return -1;
    }
    public void runCommands(String input, int index){
        while(index < input.length()){
            char c = input.charAt(index);
            handleKey(c);
            if(shouldQuit){
                return;
            }
            index++;
        }
    }
    public void handleKey(char c){
        c = Character.toLowerCase(c);
        if(waitingForColonCommand){
            if(c == 'q'){
                saveAndQuit();
                shouldQuit = true;
            }
            waitingForColonCommand = false;
            return;
        }else if(c == ':'){
            waitingForColonCommand = true;
        }else if(avatar == null || map == null){
            return;
        }else if(c == 'w'){
            avatar.moveHelper(0, 1, map);
        }else if(c == 'a'){
            avatar.moveHelper(-1, 0, map);
        }else if(c == 's'){
            avatar.moveHelper(0, -1, map);
        }else if(c == 'd'){
            avatar.moveHelper(1, 0, map);
        }else if(c == 'v'){
            is_limited_vision = !is_limited_vision;
        }else if(c == '+' && is_limited_vision){
            limited_vision++;
        }else if(c == '-' && is_limited_vision && limited_vision > 0){
            limited_vision--;
        }else if(c == 'l'){
            light_open = !light_open;
        }
    }
    public void saveAndQuit(){
        GameState gameState = new GameState(WIDTH, HEIGHT, map, avatar, random);
        SaveManager.saveAndQuit(gameState);
    }
    public boolean loadGame(){
        GameState gameState = SaveManager.loadGameState();
        if(gameState == null) return false;
        this.WIDTH = gameState.WIDTH;
        this.HEIGHT = gameState.HEIGHT;
        this.map = gameState.map;
        this.avatar = gameState.avatar;
        this.random = gameState.random;
        this.shouldQuit = false;
        return true;
    }
    public void showLogo(){
        StdDraw.clear(Color.BLACK);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH*0.5, HEIGHT*0.8, "CS61B: THE GAME");
        Font menuFont = new Font("Monaco", Font.PLAIN, 20);
        StdDraw.setFont(menuFont);
        StdDraw.text(WIDTH*0.5, HEIGHT*0.5 + 2, "New Game (N)");
        StdDraw.text(WIDTH*0.5, HEIGHT*0.5, "Load Game (L)");
        StdDraw.text(WIDTH*0.5, HEIGHT*0.5 - 2, "Quit (Q)");
        StdDraw.text(WIDTH*0.5, HEIGHT*0.5 - 4, "选择一项以继续");
        StdDraw.show();
    }
    public void drawHUD() {
        int xi = (int) StdDraw.mouseX();
        int yi = (int) StdDraw.mouseY();
        if (xi >= 0 && xi < WIDTH && yi >= 0 && yi < HEIGHT) {
            String s = map[xi][yi].description();
            Font font = new Font("Monaco", Font.PLAIN, 18);
            StdDraw.setFont(font);
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.text(WIDTH * 0.1, HEIGHT + 1, s);
        }
        StdDraw.text(WIDTH * 0.9, HEIGHT + 2, "按v键调整视野 按l键开/关灯");
        if (is_limited_vision) {
            StdDraw.text(WIDTH * 0.9, HEIGHT + 1, "按+/-键 增加/减小视野");
        }

        StdDraw.show();
    }
    public void showAll(TERenderer ter){
        ter.renderFrame(getDisplayMap());
        drawHUD();

    }
    public TETile[][] getDisplayMap(){
        TETile[][] displayMap = new TETile[map.length][map[0].length];
        for(int i=0;i<displayMap.length;i++){
            for(int j=0;j<displayMap[0].length;j++){
                displayMap[i][j] = Tileset.NOTHING;
            }
        }
        ArrayList<Integer> xList = new ArrayList<>();
        ArrayList<Integer> yList = new ArrayList<>();
        for(int i=0;i<map.length;i++){
            for(int j=0;j<map[0].length;j++){
                if(is_limited_vision && abs(i - avatar.x) + abs(j - avatar.y) > limited_vision){
                    displayMap[i][j] = Tileset.NOTHING;
                }else if(light_open && map[i][j].equals(Tileset.LIGHT_OFF)) {
                    displayMap[i][j] = Tileset.LIGHT;
                    xList.add(i);
                    yList.add(j);
                }else {
                    displayMap[i][j] = map[i][j];
                }
            }
        }
        for(int i=0; i < xList.size(); i++){
            lightMap(displayMap, xList.get(i), yList.get(i));
        }
        return displayMap;
    }
    public void lightMap(TETile[][] displayMap, int x, int y){
        for(int i = x-2; i <= x+2; i++){
            for(int j = y-2; j <= y+2; j++){
                if(i >= 0 && i < WIDTH && j >= 0 && j < HEIGHT && map[i][j].equals(Tileset.FLOOR)){
                    int dx = max(abs(i-x), abs(j-y));
                    int r = max(0, 70 - 25*dx);
                    int g = max(0, 120 - 40*dx);
                    int b = max(0, 255 - 80*dx);
                    if(r==0 && g==0 && b==0) continue;
                    displayMap[i][j] = new TETile(map[i][j].character(), new Color(128, 192, 128), new Color(r,g,b), map[i][j].description());
                }
            }
        }
    }
}
