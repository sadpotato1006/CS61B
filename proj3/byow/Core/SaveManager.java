package byow.Core;

import java.io.*;

public class SaveManager {
    public static final File SAVE_FILE = new File("save_file");

    public static void saveAndQuit(GameState gameState){
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVE_FILE));
            out.writeObject(gameState);
            out.close();
        } catch (Exception e) {
            System.out.println("Save failed: " + e.getMessage());
        }
    }
    public static GameState loadGameState(){
        try {
            if (!SAVE_FILE.exists()) {
                return null;
            }
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(SAVE_FILE));
            GameState loadedGame = (GameState) in.readObject();
            in.close();
            return loadedGame;
        } catch (Exception e) {
            System.out.println("Load failed: " + e.getMessage());
            return null;
        }
    }

}
