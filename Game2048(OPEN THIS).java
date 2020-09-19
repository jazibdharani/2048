package jazib.game;

import javax.swing.*; //All the below are packages that are needed to create this game

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Game2048 extends JPanel {
	public static final long serialVersionUID = 1L;
	public static final Color BG_COLOR = new Color(0, 0, 0); //Sets the background color
	  public static final String FONT_NAME = "SHOWCARD GOTHIC"; //Sets number fonts to Showcard Gothic
	  public static final int TILE_SIZE = 64; //Creates tile size
	  public static final int TILES_MARGIN = 16; //Sets tile margins to 16
	  JButton play = new JButton ("Play");
	 
	  public Tile[] myTiles;
	  boolean myWin = false;
	  boolean myLose = false;

	  public Game2048() {
	    setPreferredSize(new Dimension(340, 400)); //Sets window size
	    setFocusable(true); //Centers
	    addKeyListener(new KeyAdapter() { //Adds the keyboard listener (up,down,left,right)
	      public void keyPressed(KeyEvent e) { //When the key is pressed
	        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { //If escape is clicked that resets the game
	          resetGame();
	        }
	        if (!canMove()) { //When it cant move
	          myLose = true;
	        }

	        if (!myWin && !myLose) {
	          switch (e.getKeyCode()) {
	            case KeyEvent.VK_LEFT: //Checks if left was clicked
	              left(); //Method to move left
	              break;
	            case KeyEvent.VK_RIGHT: //Checks if right was clicked
	              right(); //Method to move right
	              break;
	            case KeyEvent.VK_DOWN://Checks if down was clicked
	              down(); //Method to move down
	              break;
	            case KeyEvent.VK_UP://Checks if up was clicked
	              up(); //Method to move up
	              break;
	          }
	        }

	        if (!myWin && !canMove()) { //When to end game
	          myLose = true;
	        }

	        repaint(); //Method
	      }
	    });
	    resetGame(); //Method to start game from beginning
	  }

	  public void resetGame() { //This method resets the game
	    myWin = false;
	    myLose = false;
	    myTiles = new Tile[4 * 4]; //Makes it 4 x 4 again
	    for (int i = 0; i < myTiles.length; i++) { //Removes it and resets like the beginning
	      myTiles[i] = new Tile();
	    }
	    addTile();
	    addTile();
	  }

	  public void left() { //This method moves all numbers to the left
	    boolean needAddTile = false; //Sets adding another number to false as later it will check
	    for (int i = 0; i < 4; i++) { //Loop for all rows
	      Tile[] line = getLine(i); //Makes it easier for moving other ways too
	      Tile[] merged = mergeLine(moveLine(line)); 
	      setLine(i, merged); 
	      if (!needAddTile && !compare(line, merged)) { //Checks if it should add another number
	        needAddTile = true; //Sets the loop to true so later it will add a number
	      }
	    }

	    if (needAddTile) { //If it needs to add another number
	      addTile(); //adds another number in the tiles
	    }
	  }

	  public void right() { //Uses the left method just rotates the check
	    myTiles = rotate(180);
	    left();
	    myTiles = rotate(180);
	  }
 
	  public void up() { //Uses the left method just rotates the check
	    myTiles = rotate(270);
	    left();
	    myTiles = rotate(90);
	  }

	  public void down() { //Uses the left method just rotates the check
	    myTiles = rotate(90);
	    left();
	    myTiles = rotate(270);
	  }

	  public Tile tileAt(int x, int y) { //The adding
	    return myTiles[x + y * 4];
	  }

	  public void addTile() { //Method to add numbers in tiles
	    List<Tile> list = availableSpace(); //uses method to check available spaces
	    if (!availableSpace().isEmpty()) { //Where there is no number
	      int index = (int) (Math.random() * list.size()) % list.size();
	      Tile emptyTime = list.get(index);
	      emptyTime.value = Math.random() < 0.9 ? 2 : 4; //Adds a random number up to 4
	    }
	  }

	  public List<Tile> availableSpace() { //Checking for empty spaces
	    final List<Tile> list = new ArrayList<Tile>(16);
	    for (Tile t : myTiles) { //All tiles
	      if (t.isEmpty()) {
	        list.add(t);
	      }
	    }
	    return list; //Returns the value if it is empty
	  }

	  public boolean isFull() { //Check for the grid if the tiles are full
	    return availableSpace().size() == 0;
	  }

	  boolean canMove() {
	    if (!isFull()) {
	      return true; //right now it changes the variable for can move true
	    }
	    for (int x = 0; x < 4; x++) { //Checking the board on the x axis
	      for (int y = 0; y < 4; y++) { //Checking the board on the y axis
	        Tile t = tileAt(x, y);
	        if ((x < 3 && t.value == tileAt(x + 1, y).value) //Checks if it can move
	          || ((y < 3) && t.value == tileAt(x, y + 1).value)) {
	          return true; //right now it changes the variable for can move true
	        }
	      }
	    }
	    return false; //right now it changes the variable for can move false
	  }

	  public boolean compare(Tile[] line1, Tile[] line2) {
	    if (line1 == line2) { //Checks the numbers beside each other
	      return true; //If they are equal changes variable to true so they can merge
	    } else if (line1.length != line2.length) { //If unequal
	      return false; //Changes to false so they don't merge
	    }

	    for (int i = 0; i < line1.length; i++) {
	      if (line1[i].value != line2[i].value) { //If they don't match
	        return false; //Will not add up
	      }
	    }
	    return true;
	  }

	  public Tile[] rotate(int angle) { //This is to rotate the moves
	    Tile[] newTiles = new Tile[4 * 4]; //The grid
	    int x_offset = 3; 
	    int y_offset = 3;
	    if (angle == 90) {
	      y_offset = 0; //As this is the first don't really need to rotate
	    } else if (angle == 270) { //Angle of which arrow key clicked
	      x_offset = 0;
	    }

	    double rad = Math.toRadians(angle);
	    int cos = (int) Math.cos(rad);
	    int sin = (int) Math.sin(rad);
	    for (int x = 0; x < 4; x++) {
	      for (int y = 0; y < 4; y++) {
	        int xNew = (x * cos) - (y * sin) + x_offset;
	        int yNew = (x * sin) + (y * cos) + y_offset;
	        newTiles[(xNew) + (yNew) * 4] = tileAt(x, y);
	      }
	    }
	    return newTiles;
	  }

	  public Tile[] moveLine(Tile[] oldLine) {
	    LinkedList<Tile> l = new LinkedList<Tile>();
	    for (int i = 0; i < 4; i++) {
	      if (!oldLine[i].isEmpty())
	        l.addLast(oldLine[i]);
	    }
	    if (l.size() == 0) {
	      return oldLine;
	    } else {
	      Tile[] newLine = new Tile[4];
	      ensureSize(l, 4);
	      for (int i = 0; i < 4; i++) {
	        newLine[i] = l.removeFirst();
	      }
	      return newLine;
	    }
	  }

	  public Tile[] mergeLine(Tile[] oldLine) {
	    LinkedList<Tile> list = new LinkedList<Tile>();
	    for (int i = 0; i < 4 && !oldLine[i].isEmpty(); i++) {
	      int num = oldLine[i].value;
	      if (i < 3 && oldLine[i].value == oldLine[i + 1].value) {
	        num *= 2;
	        int ourTarget = 2048;
	        if (num == ourTarget) {
	          myWin = true;
	        }
	        i++;
	      }
	      list.add(new Tile(num));
	    }
	    if (list.size() == 0) {
	      return oldLine;
	    } else {
	      ensureSize(list, 4);
	      return list.toArray(new Tile[4]);
	    }
	  }

	  public static void ensureSize(java.util.List<Tile> l, int s) {
	    while (l.size() != s) {
	      l.add(new Tile());
	    }
	  }

	  public Tile[] getLine(int index) {
	    Tile[] result = new Tile[4];
	    for (int i = 0; i < 4; i++) {
	      result[i] = tileAt(i, index);
	    }
	    return result;
	  }

	  public void setLine(int index, Tile[] re) {
	    System.arraycopy(re, 0, myTiles, index * 4, 4);
	  }

	  public void paint(Graphics g) {
	    super.paint(g);
	    g.setColor(BG_COLOR);
	    g.fillRect(0, 0, this.getSize().width, this.getSize().height);
	    for (int y = 0; y < 4; y++) {
	      for (int x = 0; x < 4; x++) {
	        drawTile(g, myTiles[x + y * 4], x, y);
	      }
	    }
	  }

	  public void drawTile(Graphics g2, Tile tile, int x, int y) { //Based on code from Joe Pelz
	    Graphics2D g = ((Graphics2D) g2);
	    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
	    int value = tile.value;
	    int offset1 = offsetCoors(x);
	    int offset2 = offsetCoors(y);
	    g.setColor(tile.getBackground());
	    g.fillRoundRect(offset1, offset2, TILE_SIZE, TILE_SIZE, 14, 14);
	    g.setColor(tile.getForeground());
	    final int size = value < 100 ? 36 : value < 1000 ? 32 : 24;
	    final Font font = new Font(FONT_NAME, Font.BOLD, size);
	    g.setFont(font);

	    String s = String.valueOf(value);
	    final FontMetrics fm = getFontMetrics(font);

	    final int w = fm.stringWidth(s);
	    final int h = -(int) fm.getLineMetrics(s, g).getBaselineOffsets()[2];

	    if (value != 0)
	      g.drawString(s, offset1 + (TILE_SIZE - w) / 2, offset2 + TILE_SIZE - (TILE_SIZE - h) / 2 - 2);

	    if (myWin || myLose) { //Whenever you win or lose
	      g.setColor(new Color(255, 255, 255, 30));
	      g.fillRect(0, 0, getWidth(), getHeight());
	      g.setColor(new Color(78, 139, 202));
	      g.setFont(new Font(FONT_NAME, Font.BOLD, 48));
	      if (myWin) {
	        g.drawString("You won!", 68, 150); //Comes when you win
	      }
	      if (myLose) {
	        g.drawString("Game over!", 50, 130); //When you cant move
	        g.drawString("You lose!", 64, 200);
	      }
	      if (myWin || myLose) {
	        g.setFont(new Font(FONT_NAME, Font.PLAIN, 16));
	        g.setColor(new Color(128, 128, 128, 128));
	        g.drawString("Press ESC to play again", 80, getHeight() - 40);
	      }
	    }
	    g.setFont(new Font(FONT_NAME, Font.PLAIN, 18));

	  }

	  public static int offsetCoors(int arg) {
	    return arg * (TILES_MARGIN + TILE_SIZE) + TILES_MARGIN;
	  }

	  static class Tile {
	    int value;

	    public Tile() {
	      this(0);
	    }

	    public Tile(int num) {
	      value = num;
	    }

	    public boolean isEmpty() {
	      return value == 0;
	    }

	    public Color getForeground() {
	      return value < 16 ? new Color(0, 0, 0) :  new Color(0, 0, 0); //Text colors
	    }

	    public Color getBackground() { //Changes colors
	      switch (value) {
	        case 2:    return new Color(82, 0, 165);
	        case 4:    return new Color(214, 0, 210);
	        case 8:    return new Color(255, 246, 0);
	        case 16:   return new Color(0, 232, 27);
	        case 32:   return new Color(0, 102, 237);
	        case 64:   return new Color(186, 0, 0);
	        case 128:  return new Color(247, 107, 0);
	        case 256:  return new Color(0, 247, 238);
	        case 512:  return new Color(164, 247, 0);
	        case 1024: return new Color(32, 143, 170);
	        case 2048: return new Color(169, 31, 137);
	      }
	      return new Color(68, 68, 68); //Tile color
	    }
	  }
	  
	  public static void main(String[] args) { //Plays the whole game (adds the frame)
		JFrame game = new JFrame(); //Creates the frame
	    game.setTitle("2048! By: Jazib Dharani, Made in cooperation: Alam Khokhar and Credits to: Joe Pelz");
	    game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //Goes away when exited
	    game.setSize(340, 400); //Sets the size
	    game.setResizable(false); //Makes it solid state not liquid state

	    game.add(new Game2048()); //Starts new game

	    game.setLocationRelativeTo(null); //Centers
	    game.setVisible(true); //Shows the game
	  }
	}
