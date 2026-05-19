package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;


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
    public void initGame(long seed){
        this.random = new Random(seed);
        WorldGenerate generate = new WorldGenerate(WIDTH, HEIGHT, random);
        this.map = generate.generateWorld();
        this.avatar = new Avatar(map, random);
        this.waitingForColonCommand = false;
        this.shouldQuit = false;
        this.light_open = true;
        this.is_limited_vision = false;
        this.limited_vision = 5;
    }
    public TETile[][] play(InputSource source, boolean shouldRender, TERenderer ter){
        if(source == null) return null;
        if(shouldRender) showLogo();
        boolean menuChoice = processMenuChoice(source, shouldRender, ter);
        if(!menuChoice) return null;
        processRestCommand(source, shouldRender, ter);
        return this.map;
    }
    public boolean processMenuChoice(InputSource source, boolean shouldRender, TERenderer ter){
        if(source == null || source.isExhausted()) return false;
        while(true){
            if(shouldRender) StdDraw.pause(20);
            if(source.isExhausted()) return false;
            if(!source.hasNextKey()) continue;
            char menuChoice = source.getNextKey();
            if(menuChoice == 'l'){
                return loadGame();
            }else if(menuChoice == 'n'){
                Long seed = filterSeed(source, shouldRender,ter);
                if(seed == null) return false;
                this.initGame(seed);
                return true;
            }else if(menuChoice == 'q'){
                shouldQuit = true;
                return false;
            }
        }
    }
    public Long filterSeed(InputSource source, boolean shouldRender, TERenderer ter){
        if(source == null || source.isExhausted()) return null;
        StringBuilder string_builder = new StringBuilder();
        while(!source.isExhausted()){
            if(shouldRender) StdDraw.pause(20);
            if(shouldRender) drawSeedScreen(string_builder.toString());
            if(!source.hasNextKey()) continue;
            char c = source.getNextKey();
            if(c == 's'){
                if(string_builder.length() == 0) return null;
                return Long.parseLong(string_builder.toString());
            }
            if(Character.isDigit(c)){
                string_builder.append(c);
            }
        }
        return null;
    }
    public void processRestCommand(InputSource source, boolean shouldRender, TERenderer ter){
        while(!source.isExhausted() && !this.shouldQuit){
            if(shouldRender && ter != null) showAll(ter);
            if(source.hasNextKey()){
                char c = source.getNextKey();
                if(c == 'r'){
                    Long seed = filterSeed(source, shouldRender, ter);
                    if(seed != null){
                        initGame(seed);
                    }
                    continue;
                }else{
                    handleKey(c);
                }
            }
            if(shouldRender) StdDraw.pause(20);
        }
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
    public void handleKey(char c){
        c = Character.toLowerCase(c);
        if(waitingForColonCommand){
            if(c == 'q'){
                saveAndQuit();
                shouldQuit = true;
            }
            waitingForColonCommand = false;
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
        GameState gameState = new GameState(WIDTH, HEIGHT, map, avatar, random, light_open, is_limited_vision, limited_vision);
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
        this.limited_vision = gameState.limited_vision;
        this.light_open = gameState.light_open;
        this.is_limited_vision = gameState.is_limited_vision;
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
        StdDraw.setPenColor(Color.WHITE);
        int xi = (int) StdDraw.mouseX();
        int yi = (int) StdDraw.mouseY();
        if (xi >= 0 && xi < WIDTH && yi >= 0 && yi < HEIGHT) {
            String s = map[xi][yi].description();
            StdDraw.text(WIDTH * 0.1, HEIGHT + 1, s);
        }
        //StdDraw.show();
    }
    public void drawMessage(){
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH * 0.5, HEIGHT + 2, "V:调整视野  L:开/关灯  R:new world  :Q 保存并退出");
        if (is_limited_vision) {
            StdDraw.text(WIDTH * 0.9, HEIGHT + 1, "按+/-键 增加/减小视野");
        }
        //StdDraw.show();
    }
    public void showAll(TERenderer ter){
        Font font = new Font("Monaco", Font.PLAIN, 18);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);

        ter.renderFrame(getDisplayMap());
        drawHUD();
        drawMessage();

        StdDraw.show(); //最后统一展示 避免闪烁
    }
    public TETile[][] getDisplayMap(){
        TETile[][] displayMap = new TETile[map.length][map[0].length];
        for(int i=0;i<displayMap.length;i++){
            for(int j=0;j<displayMap[0].length;j++){
                displayMap[i][j] = Tileset.NOTHING;
            }
        }

        //收集所有灯的位置
        ArrayList<Integer> xList = new ArrayList<>();
        ArrayList<Integer> yList = new ArrayList<>();
        for(int i=0;i<map.length;i++){
            for(int j=0;j<map[0].length;j++){
                if(light_open && map[i][j].equals(Tileset.LIGHT_OFF)) {
                    displayMap[i][j] = Tileset.LIGHT;
                    xList.add(i);
                    yList.add(j);
                }else {
                    displayMap[i][j] = map[i][j];
                }
            }
        }
        for(int i=0; i < xList.size(); i++){
            lightMap(displayMap, xList.get(i), yList.get(i)); //点灯
        }

        //限制视野
        if(is_limited_vision){

            for(int i=0;i<map.length;i++){
                for(int j=0;j<map[0].length;j++){
                    if(abs(i - avatar.x) + abs(j - avatar.y) > limited_vision){
                        displayMap[i][j] = Tileset.NOTHING;
                    }
                }
            }

        }
        return displayMap;
    }
    public void lightMap(TETile[][] displayMap, int x, int y){
        for(int i = x-5; i <= x+5; i++){
            for(int j = y-5; j <= y+5; j++){
                if(i >= 0 && i < WIDTH && j >= 0 && j < HEIGHT && map[i][j].equals(Tileset.FLOOR)){
                    int dx = max(abs(i-x), abs(j-y));
                    int r = max(0, 70 - 15*dx);
                    int g = max(0, 120 - 24*dx);
                    int b = max(0, 255 - 51*dx);
                    if(r==0 && g==0 && b==0) continue;
                    if(displayMap[i][j].getBackgroundColor().getBlue() > b) continue; //避免强光被弱光覆盖
                    displayMap[i][j] = new TETile(map[i][j].character(), new Color(128, 192, 128), new Color(r,g,b), map[i][j].description());
                }
            }
        }
    }
}
