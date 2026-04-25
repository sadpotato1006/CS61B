package byow.lab13;

import byow.Core.RandomUtils;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    /** The width of the window of this game. */
    private int width;
    /** The height of the window of this game. */
    private int height;
    /** The current round the user is on. */
    private int round;
    /** The Random object used to randomly generate Strings. */
    private Random rand;
    /** Whether or not the game is over. */
    private boolean gameOver;
    /** Whether or not it is the player's turn. Used in the last section of the
     * spec, 'Helpful UI'. */
    private boolean playerTurn;
    /** The characters we generate random Strings from. */
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    /** Encouraging phrases. Used in the last section of the spec, 'Helpful UI'. */
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        long seed = Long.parseLong(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        //TODO: Initialize random number generator
        rand = new Random(seed);
        gameOver = false;
        round = 0;
        playerTurn = false;
    }

    public String generateRandomString(int n) {
        //TODO: Generate random string of letters of length n
        StringBuilder ans = new StringBuilder();
        for(int i = 0; i < n; i++){
            int a = this.rand.nextInt(26);
            ans.append(CHARACTERS[a]);
        }
        return ans.toString();
    }

    public void drawFrame(String s) {
        //TODO: Take the string and display it in the center of the screen
        //TODO: If game is not over, display relevant game information at the top of the screen
        StdDraw.clear(Color.BLACK);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(this.width/2.0, this.height/2.0, s);
        if(!gameOver){
            Font font1 = new Font("Monaco", Font.BOLD, 20);
            StdDraw.setFont(font1);
            StdDraw.text(3, this.height - 1, "ROUND: " + this.round);
            if(playerTurn){
                StdDraw.text(this.width/2.0, this.height - 1, "Type!");
            }else{
                StdDraw.text(this.width/2.0, this.height - 1, "Watch!");
            }
            StdDraw.text(this.width-5, this.height-1, ENCOURAGEMENT[rand.nextInt(ENCOURAGEMENT.length)]);
        }
        StdDraw.show();
    }

    public void flashSequence(String letters) {
        //TODO: Display each character in letters, making sure to blank the screen between letters
        for(int i = 0; i < letters.length(); i++){
            String s = String.valueOf(letters.charAt(i));
            drawFrame(s);
            StdDraw.pause(1000);
            drawFrame("");
            StdDraw.pause(500);
        }
    }

    public String solicitNCharsInput(int n) {
        //TODO: Read n letters of player input
        StringBuilder s = new StringBuilder();
        while(s.length() < n){
            if(StdDraw.hasNextKeyTyped()){
                s.append(StdDraw.nextKeyTyped());
                drawFrame(s.toString());
            }
        }
        return s.toString();
    }

    public void endOfGame(){
        StdDraw.clear(Color.BLACK);
        Font font = new Font("Monaco", Font.BOLD, 35);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(this.width/2.0, this.height/2.0, "Game Over! You made it to round: " + this.round);
        StdDraw.show();
    }

    public void startGame() {
        //TODO: Set any relevant variables before the game starts
        gameOver = false;
        round = 0;
        //TODO: Establish Engine loop
        while(!this.gameOver){
            playerTurn = false;
            round++;
            drawFrame("Round: " + this.round);
            StdDraw.pause(1000);
            String s = generateRandomString(this.round);
            flashSequence(s);
            playerTurn = true;
            while (StdDraw.hasNextKeyTyped()) { //清空之前的输入
                StdDraw.nextKeyTyped();
            }
            drawFrame("");
            String input = solicitNCharsInput(this.round);
            StdDraw.pause(1000);
            if(!s.equals(input)){
                gameOver = true;
                break;
            }
            StdDraw.pause(10);
        }
        endOfGame();
    }

}
