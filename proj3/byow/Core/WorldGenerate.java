package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.ArrayList;
import java.util.Random;

import static byow.Core.Room.connect;

public class WorldGenerate {
    public int width;
    public int height;
    public int num_of_room;
    public int num_of_light;
    public Random random;
    public ArrayList<Room> list;
    public int idGenerator;

    public WorldGenerate(int a, int b, Random r){
        width = a;
        height = b;
        random = r;
        num_of_room = random.nextInt(10) + 5;
        num_of_light = random.nextInt(10) + 5;
        list = new ArrayList<>();
        idGenerator = 0;
    }
    public TETile[][] generateWorld(){
        TETile[][] map = new TETile[width][height];
        setNothing(map);
        generateRoomInMap(map);
        generateLightInMap(map);

        return map;
    }
    public static void setNothing(TETile[][] map){
        for(int i=0;i<map.length;i++){
            for(int j=0;j<map[0].length;j++){
                map[i][j] = Tileset.NOTHING;
            }
        }
    }
    private Room generateRoom(){
        int width = random.nextInt(6) + 3;
        int height = random.nextInt(6) + 3;
        int x = random.nextInt(this.width -width-3) + 2;
        int y = random.nextInt(this.height -height-3) + 2;
        return new Room(x, y, width, height,idGenerator++);
    }
    private boolean hasOverlap(Room newRoom) {
        for (Room oldRoom : list) {
            if (Room.isContained(newRoom, oldRoom)) {
                return true;
            }
        }
        return false;
    }
    public void generateRoomInMap(TETile[][] map){
        int time = 0;
        for(int i = 0; i < num_of_room && time < 90; i++, time++){
            Room r = generateRoom();
            if(hasOverlap(r)){
                i--;
                continue;
            }
            list.add(r);
            r.drawRoom(map);
        }
        Room pre = list.get(0);
        for(int i = 1; i < list.size(); i++){
            Room curr = list.get(i);
            connect(pre, curr, map);
            pre = curr;
        }
    }
    public void generateLightInMap(TETile[][] map){
        int time = 0;
        for(int i = 0; i < num_of_light && time < 90; time++){
            int x = random.nextInt(width-1);
            int y = random.nextInt(height-1);
            if(x>=0 && x<map.length && y>=0 && y<map[0].length && map[x][y].equals(Tileset.FLOOR)){
                map[x][y] = Tileset.LIGHT_OFF;
                i++;
            }
        }
    }
}
