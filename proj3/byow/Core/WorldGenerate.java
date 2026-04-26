package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.awt.datatransfer.FlavorListener;
import java.util.ArrayList;
import java.util.Random;

import static byow.Core.Room.connect;

public class WorldGenerate {
    public int WIDTH;
    public int HEIGHT;
    public int num_of_room;
    public Random random;
    public ArrayList<Room> list;
    public int idGenerator;

    public WorldGenerate(int a, int b, Random r){
        WIDTH = a;
        HEIGHT = b;
        random = r;
        num_of_room = random.nextInt(10) + 5;
        list = new ArrayList<>();
        idGenerator = 0;
    }
    public TETile[][] generateWorld(){
        TETile[][] ans = new TETile[WIDTH][HEIGHT];
        setNothing(ans);
        int time = 0;
        for(int i = 0; i < num_of_room && time < 90; i++, time++){
            Room r = generateRoom();
            if(hasOverlap(r)){
                i--;
                continue;
            }
            list.add(r);
            r.drawRoom(ans);
        }
        Room pre = list.get(0);
        for(int i = 1; i < list.size(); i++){
            Room curr = list.get(i);
            connect(pre, curr, ans);
            pre = curr;
        }
        return ans;
    }
    private static void setNothing(TETile[][] map){
        for(int i=0;i<map.length;i++){
            for(int j=0;j<map[0].length;j++){
                map[i][j] = Tileset.NOTHING;
            }
        }
    }
    private Room generateRoom(){
        int width = random.nextInt(6) + 3;
        int height = random.nextInt(6) + 3;
        int x = random.nextInt(WIDTH-width-3) + 2;
        int y = random.nextInt(HEIGHT-height-3) + 2;
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

}
