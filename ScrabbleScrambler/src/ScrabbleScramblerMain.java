import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import java.awt.*;
import java.awt.event.*;


public class ScrabbleScramblerMain {

	//Number of tiles in spread
	public final int NUM_TILES = 7;
	//New JFrame
	public JFrame frame;
	//Array of Panels to display tiles
	public Panel[] tileBoxes = new Panel[NUM_TILES];
	//Array of formatted text fields to display characters
	public JLabel[] tileTextBoxes = new JLabel[NUM_TILES];
	//Array of text fields to display numerical score
	public JLabel[] tileScoreBoxes = new JLabel[NUM_TILES];
	
	public Point[] tileLocations = new Point[NUM_TILES];
	
	public final CircularTileSpread newTileSpread = new CircularTileSpread();
	
	//Mouse tracking values
	public volatile int mouseX = 0;
	public volatile int mouseY = 0;
	public volatile int myX = 0;
	public volatile int myY = 0;
	
	
	public static void main(String[] args) {
		//Launch the window
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ScrabbleScramblerMain window = new ScrabbleScramblerMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
	}

	//Initialize frame
	public ScrabbleScramblerMain() {
		initialize();
	}

	//Method to refresh GUI representation of circular tile spread
	public void updateTiles() {
		for(int i = 0; i<NUM_TILES; i++) {
			tileTextBoxes[i].setText(String.format
					("  %s  ", newTileSpread.getFirstCharacter()));
			tileScoreBoxes[i].setText(String.format
					("      %s      ",newTileSpread.getFirstScore()));
			newTileSpread.shiftOnce();
		}
	}
	
	//Populate tile spread and add values to GUI
	public void newSpread() {
		//Populate circular tile spread
		newTileSpread.fillRandom(NUM_TILES);
		updateTiles();
	}
	

	
	//Adding content to frame
	public void initialize() {
		//Set up the frame
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
			//Width based off of NUM_TILES
		frame.setBounds(100, 100, 55+(95*NUM_TILES), 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		//Create and add tiles
		for(int i = 0; i < NUM_TILES; i++) {
			//Add panels to store text fields
			tileBoxes[i] = new Panel();
			tileBoxes[i].setBounds(34+(95*i), 30, 70, 78);
			tileBoxes[i].setBackground(new Color(242, 207, 167));
			frame.getContentPane().add(tileBoxes[i]);
			tileLocations[i] = tileBoxes[i].getLocation();
			//Add character text boxes to panels
			tileTextBoxes[i] = new JLabel();
			tileTextBoxes[i].setFont(new Font("Arial", Font.BOLD, 35));
			tileBoxes[i].add(tileTextBoxes[i]);
			//Add score text boxes to panels
			tileScoreBoxes[i] = new JLabel();
			tileScoreBoxes[i].setVerticalAlignment(SwingConstants.BOTTOM);
			tileScoreBoxes[i].setHorizontalAlignment(SwingConstants.RIGHT);
			tileBoxes[i].add(tileScoreBoxes[i]);
		}
		
		//Separate loop to add movement and swapping capabilities to tiles
		for(int j = 0; j < NUM_TILES; j++) {
			
			//Create references to the parent of this mouse listener
			Panel parentTile = tileBoxes[j];
			int parentTileIndex = j;

			parentTile.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
			        mouseX = e.getXOnScreen();
			        mouseY = e.getYOnScreen();
	
			        myX = parentTile.getX();
			        myY = parentTile.getY();
				}
				
				public void mouseReleased(MouseEvent e) {
					int firstPos = -1;
					int secondPos = -1;
					for(int i = 0; i < NUM_TILES; i++) {
						if(tileLocations[i].distance(parentTile.getLocation()) < 45)	{
							firstPos = parentTileIndex;
							secondPos = i;
						}
					}
					parentTile.setLocation(tileLocations[parentTileIndex]);
					if(firstPos != -1 && secondPos != -1)
						newTileSpread.swapTiles(firstPos, secondPos);
					updateTiles();
				}
			});
			
			parentTile.addMouseMotionListener(new MouseMotionAdapter() {

				      public void mouseDragged(MouseEvent e) {
				        int deltaX = e.getXOnScreen() - mouseX;
				        int deltaY = e.getYOnScreen() - mouseY;
				        parentTile.setLocation(myX + deltaX, myY + deltaY);
				      }

			});
		
		}
		
		JLabel NewButton = new JLabel("New Spread");
		NewButton.setHorizontalAlignment(SwingConstants.CENTER);
		NewButton.setForeground(Color.DARK_GRAY);
		NewButton.setFont(new Font("Arial", Font.BOLD, 20));
		NewButton.setBounds(36, 170, 130, 30);
		frame.getContentPane().add(NewButton);
		NewButton.addMouseListener(new MouseAdapter()  
		{  
		    public void mouseClicked(MouseEvent e)  
		    {  
		    		newTileSpread.fillRandom(NUM_TILES);
				updateTiles();
		    }  
		}); 

		JLabel ShuffleButton = new JLabel("Shuffle");
		ShuffleButton.setHorizontalAlignment(SwingConstants.CENTER);
		ShuffleButton.setForeground(Color.DARK_GRAY);
		ShuffleButton.setFont(new Font("Arial", Font.BOLD, 20));
		ShuffleButton.setBounds(202, 170, 130, 30);
		frame.getContentPane().add(ShuffleButton);
		ShuffleButton.addMouseListener(new MouseAdapter()  
		{  
		    public void mouseClicked(MouseEvent e)  
		    {  
				if(!newTileSpread.isEmpty()) {
					newTileSpread.shuffle();
					updateTiles();
				}
		    }  
		});
		
		JLabel sortAlpha = new JLabel("Sort ABC");
		sortAlpha.setHorizontalAlignment(SwingConstants.CENTER);
		sortAlpha.setForeground(Color.DARK_GRAY);
		sortAlpha.setFont(new Font("Arial", Font.BOLD, 20));
		sortAlpha.setBounds(368, 170, 130, 30);
		frame.getContentPane().add(sortAlpha);
		sortAlpha.addMouseListener(new MouseAdapter()  
		{  
		    public void mouseClicked(MouseEvent e)  
		    {  
		    		if(!newTileSpread.isEmpty()) {
					newTileSpread.sortAlphabetically();
					updateTiles();
				}
		    }  
		});
		
		JLabel sortNum = new JLabel("Sort 123");
		sortNum.setHorizontalAlignment(SwingConstants.CENTER);
		sortNum.setForeground(Color.DARK_GRAY);
		sortNum.setFont(new Font("Arial", Font.BOLD, 20));
		sortNum.setBounds(534, 170, 130, 30);
		frame.getContentPane().add(sortNum);
		sortNum.addMouseListener(new MouseAdapter()  
		{  
		    public void mouseClicked(MouseEvent e)  {  
		    		if(!newTileSpread.isEmpty()) {
					newTileSpread.sortNumerically();
					updateTiles();
				}
		    }  
		});
	}
}


