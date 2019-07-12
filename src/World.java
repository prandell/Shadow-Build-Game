import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.*;
import org.newdawn.slick.tiled.TiledMap;

/* Attribution to Eleanor McCurty <mcmurtrye@unimelb.edu.au> for sampled code from 
 * Project 1 Sample solution.
 */
/**
 * World class which hosts all Game objects in the world, including all Sprites
 * and the camera. It organises all tasks and takes the primary input from the 
 * user and decides where to delegate tasks.
 */
public class World {
	
	/* ----------------------------- Constants -------------------------------*/
	/** Tile height and width in pixels */
    public static final int TILE_SIZE=64;
    /** CSV comma used to seperate cells during parsing */
    private static final String CSV_SEPARATOR =",";
    /** String storing path of CSV file to initialise initial Sprites */
    private static final String CSV_NAME="assets/objects.csv";
    
    /** String to display on HUD when a Command Centre is selected */
    private static final String CMND_CNTR_STR=
    		"1- Create Scout\n2- Create Builder\n3- Create Engineer\n";
    /** String to display on HUD when a Factory is selected */
    private static final String FACTORY_STR="1- Create Truck\n";
    /** String to display on HUD when a Builder is selected */
    private static final String BUILDER_STR="1- Create Factory\n";
    /** String to display on HUD when a Truck is selected */
    private static final String TRUCK_STR="1- Create Command Centre\n";
    /** String to display on HUD when a Pylon is selected and active */
    private static final String PYLON_STR_ACTIVE="Active\n";
    /** String to display on HUD when a Pylon is selected and inactive */
    private static final String PYLON_STR_INACTIVE="Inactive\n";
    
    /** String storing the name of a factory, for commonality when needed */ 
    public static final String FACTORY="factory";
    /** String storing the name of a Command centre, for commonality when needed */
    public static final String COMMAND_CNTR="command_centre";
    /** String storing the name of a Pylon, for commonality when needed */
    public static final String PYLON="pylon";
    /** String storing the name of an Engineer, for commonality when needed */
    public static final String ENGINEER="engineer";
    /** String storing the name of a Scout, for commonality when needed */
    public static final String SCOUT="scout";
    /** String storing the name of a Builder, for commonality when needed */
    public static final String BUILDER="builder";
    /** String storing the name of a Truck, for commonality when needed */
    public static final String TRUCK="truck";
    /** String storing the name of Metal Resource, for commonality when needed */
    public static final String METAL="metal_mine";
    /** String storing the name of Unobtainium, for commonality when needed */
    public static final String UNOBTAINIUM="unobtainium_mine";
    
    /** The map Height and Width, to be initialised in the constructor */
    public final int MAPHEIGHT, MAPWIDTH;
    
	/* ------------------------- Instance Variables --------------------------*/
    //Comments for these were not required, and are fairly straight forward
	private TiledMap map;
	/** The currently selected sprite */
	private Selectable selected;
	private Camera camera= new Camera();
	private Input lastInput;
	private ArrayList <Building> buildings=new ArrayList<>();
	private ArrayList <Resource> resources= new ArrayList<>();
	private ArrayList <Unit> units=new ArrayList<>();
	private ArrayList <Sprite> toDestroy = new ArrayList<>();
	private int lastDelta;
	
	/** Amount of Metal that is spendable by the Player */
	private int metal = 0;
	/** Amount of Unobtainium the Player has acquired */
	private int unobtainium = 0;

	/*---------------------------- World Methods -----------------------------*/
	/** World Constructor. Initialises the map, map width & height, loads all
	 * initial Game Objects from a CSV, selects the first unit, and centres
	 * the camera over it.
	 */
	public World () throws SlickException {
		map = new TiledMap("assets/main.tmx");
		MAPHEIGHT=map.getHeight()*TILE_SIZE;
		MAPWIDTH=map.getWidth()*TILE_SIZE;
		
		load(CSV_NAME);
		selected=units.get(0);
		camera.centerOn((Sprite)selected);
	}
	
	/** World update method deals with game-specific inputs, such as Sprite 
	 * Selection, and camera WASD movement. It also updates all Sprites and 
	 * empties a cache of Sprites to be removed.
	 * @param input, input from the user.
	 * @param delta, milliseconds since last frame.
	 */
	public void update(Input input, int delta) {
		lastInput = input;
		lastDelta = delta;
		
		//ESC to exit.
		if (input.isKeyDown(Input.KEY_ESCAPE)) {
			System.exit(0);
		}
		
		//Get selection
		getSelection(input);
		
		//Activate free camera roam with WASD
		activateFreeRoam();
		
		//Update the camera, and all Sprites
		camera.update(this);
		updateSprites();
		
		//Empty the cache
		if (!this.toDestroy.isEmpty()) {
			for (Sprite s: this.toDestroy) {
				destroySprite(s);
			}
		}
	} 
	
	//Render method, renders map, Sprites and HUD text displays
	/** Render method, renders the map at the position determined
	 * by the camera. Renders all Sprites, and HUD text last.
	 * @param g, the Graphics from Slick.
	 */
	public void render(Graphics g) {
		map.render((int)camera.globalXToScreenX(0),
				   (int)camera.globalYToScreenY(0));
		renderSprites();
		renderText(g);
	}


	/*------------------------ Update Helper Methods -------------------------*/
	/** updateSprites method, loops through all arraylists of sprites and 
	 * updates them in order
	 */
	private void updateSprites() {
		for (Unit u: this.units) {
			u.update(this);
		}
		for (Building b: this.buildings) {
			b.update(this);
		}
		for (Resource r: this.resources) {
			r.update(this);
		}
	}
	
	/** Free Roam method used to send a message to the camera, indicating WASD
	 * keys have been pressed and free roam is activated.
	 */
	private void activateFreeRoam() {
		if (lastInput.isKeyPressed(Input.KEY_W)||
				lastInput.isKeyPressed(Input.KEY_S)||
				lastInput.isKeyPressed(Input.KEY_D)||
				lastInput.isKeyPressed(Input.KEY_A)) {
			camera.centerOff();
		} else {
			
		}
	}
	
	/** getSelection method that deals with selection using input from the user.
	 * This method ensures only one Sprite is ever selected, and handles over-
	 * lapping instances.
	 * @param input, input from the user.
	 */
	private void getSelection(Input input) {
		//If the left mouse button is clicked
		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			//get the coordinates of that click
			float clickX=(float) camera.screenXToGlobalX(input.getMouseX());
			float clickY=(float) camera.screenYToGlobalY(input.getMouseY());
			
			//for all buildings
			for (Building b: this.buildings) {
				// if the click was close enough
				if (Position.distance(b.getPosition(),clickX,clickY) <= 
						Selectable.TOLERANCE) {
					
					//deselect the previously selected sprite
					selected.deSelect();
					
					//select the building
					b.select();
					
					//set selected to the building
					selected=b;
					
					//stop searching
					break;
					
				} else {
					
					//If were not close to the building, deselect it
					b.deSelect();
				}
			}
			
			//for all units, after buildings, so that units take precedence.
			for (Unit u: this.units) {
				//if click was close enough
				if (Position.distance(u.getPosition(),clickX,clickY) <=
						Selectable.TOLERANCE) {
					
					//deselect the current selection
					selected.deSelect();
					//select the unit
					u.select();
					//set selected to the unit
					selected=u;
					
					//stop searching
					break;
				} else {
					//if not close enough, deselect it
					u.deSelect();
				}
			}
		}
	}
	
	/*------------------------- Render Helper Methods ------------------------*/
	/** renderSprites method, loops through all arraylists of sprites and 
	 * renders them in order.
	 */
	private void renderSprites() {
		for (Building b: this.buildings) {
			b.render();
		}
		for (Resource r: this.resources) {
			r.render();
		}
		for (Unit units: this.units) {
			units.render();
		}
	}
	
	/** Renders all text to the HUD, and additional text according to the 
	 * currently selected sprite.
	 * @param g, graphics used to render.
	 */
	private void renderText(Graphics g) {
		
		//Resource amounts
		String resources= String.format("Metal: %d\nUnobtainium: %d",
				this.metal,this.unobtainium);
		g.drawString(resources, 32, 32);
		
		//Draw string depending on type of selected sprite
		if (selected instanceof CommandCentre) {
			g.drawString(CMND_CNTR_STR, 32, 100);
		} else if (selected instanceof Factory) {
			g.drawString(FACTORY_STR, 32, 100);
		} else if (selected instanceof Builder) {
			g.drawString(BUILDER_STR, 32, 100);
		} else if (selected instanceof Truck) {
			g.drawString(TRUCK_STR,32,100);
		} else if (selected instanceof Pylon) {
			if (((Pylon) selected).isActive()) {
				g.drawString(PYLON_STR_ACTIVE, 32, 100);
			} else {
				g.drawString(PYLON_STR_INACTIVE, 32, 100);
			}
		}
	}
	
	/*-------------------- Initialisation Helper Methods ---------------------*/
	/** Method to load all Sprites from a CSV file
	 * @param filename, the String storing the path to the CSV
	 */
	private void load(String filename) throws SlickException {
		
		try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
		
    		String line;
	    	while ((line = br.readLine()) != null) {
	    		
	    		//Array of words contains the split line that has been read
	    		String words[] = line.split(CSV_SEPARATOR);
	    		
	    		//words[1] & words[2] correspond to the x & y coordinates
	    		int xPos = Integer.parseInt(words[1]); 
	    		int yPos = Integer.parseInt(words[2]);
	    		
	    		//words[0] corresponds to the name of the Sprite type to be created
	    		switch (words[0]) {
	    			case COMMAND_CNTR:
	    				buildings.add(new CommandCentre(xPos,yPos,camera));
	    				break;
	    			case METAL:
	    				resources.add(new Resource(xPos,yPos,camera,METAL));
	    				break;
	    			case UNOBTAINIUM:
	    				resources.add(new Resource(xPos,yPos,camera,UNOBTAINIUM));
	    				break;
	    			case PYLON:
	    				buildings.add(new Pylon(xPos,yPos,camera));
	    				break;
	    			case ENGINEER:
	    				units.add(new Engineer(xPos,yPos,camera));
	    				break;	
	    		}
	    		
	    	
	    	}
	    
	    } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*------------------------- Map Property Methods -------------------------*/
	/** Conversion method for x coordinate in pixels, to tiles
	 * @param x, the x coordinate in pixels
	 * @return the x coordinate in Tiles
	 */
	private int worldXToTileX(double x) {
		return (int)(x / TILE_SIZE);
	}
	
	/** Conversion method for y coordinate in pixels, to tiles
	 * @param y, the y coordinate in pixels
	 * @return the y coordinate in Tiles
	 */
	private int worldYToTileY(double y) {
		return (int)(y / TILE_SIZE);
	}
	
	/** Method used by Units to determine whether or not the position they are
	 * attempting to move to is solid or not.
	 * @param x, x coordinate of attempted movement
	 * @param y, y coordiante of attempted movement
	 * @return boolean indicating whether the move is allowed or not.
	 */
	public boolean isPositionFree(double x, double y) {
		int tileId = map.getTileId(worldXToTileX(x), worldYToTileY(y), 0);
		return !Boolean.parseBoolean(map.getTileProperty(tileId,
				"solid", "false"));
	}
	
	/** Method used by Units that implement Trainable to determine whether 
	 * or not the position they are attempting to build at is occupied or not.
	 * @param x, x coordinate of attempted building
	 * @param y, y coordiante of attempted building
	 * @return boolean indicating whether the construction is allowed or not.
	 */
	public boolean canBuildHere(Position p) {
		int tileId = map.getTileId(worldXToTileX(p.getX()),
				worldYToTileY(p.getY()), 0);
		if (!Boolean.parseBoolean(map.getTileProperty(tileId,
				"occupied", "false"))) {
			return true;
		} else {
			return false;
		}
	}
	
	/*------------------------- Getters & Setters ----------------------------*/
	
	/** Resources getter.
	 * @return ArrayList of Resources
	 */
	public ArrayList<Resource> getResources() {
		return resources;
	}
	
	/** Buildings getter.
	 * @return ArrayList of Buildings
	 */
	public ArrayList<Building> getBuildings() {
		return buildings;
	}
	
	/** Units getter.
	 * @return ArrayList of Units
	 */
	public ArrayList<Unit> getUnits() {
		return units;
	}
	
	/** Input getter.
	 * @return Input last recorded.
	 */
	public Input getInput() {
		return lastInput;
	}
	
	/** Delta getter.
	 * @return last delta value
	 */
	public int getDelta() {
		return lastDelta;
	}
	
	/** Essentially a compound setter. Increases metal or unobtainium,
	 * depending on the input String.
	 * @param type, the String determining which Resource to increase
	 */
	public void resourceIncrease(String type) {
		if (type.equals(METAL)) {
			this.metal=this.metal + Engineer.allowance;
		} else {
			this.unobtainium=this.unobtainium+Engineer.allowance;
		}
	}
	
	/** Essentially a setter for metal. Decreases metal by amount.
	 * @param amount, the amount metal should be decreased by.
	 */
	public boolean grantResource(int amount) {
		if (this.metal<amount) {
			return false;
		} else {
			this.metal=this.metal-amount;
			return true;
		}
	}
	
	/*---------------------- Sprite Creation & Deletion -----------------------*/
	/** Method to create a new Sprite and add to it's corresponding Arraylist.
	 * @param type, the String indicating which Sprite is being created
	 * @param xPos, the x coordinate of it's initial position
	 * @param yPos, the y coordinate of it's initial position
	 */
	public void createSprite(String type,float xPos,float yPos) 
			throws SlickException {
		switch (type) {
			case COMMAND_CNTR:
				buildings.add(new CommandCentre(xPos,yPos,camera));
				break;
			case SCOUT:
				units.add(new Scout(xPos,yPos,camera));
				break;
			case ENGINEER:
				units.add(new Engineer(xPos,yPos,camera));
				break;
			case BUILDER:
				units.add(new Builder(xPos,yPos,camera));
				break;
			case TRUCK:
				units.add(new Truck(xPos,yPos,camera));
				break;
			case FACTORY:
				buildings.add(new Factory(xPos,yPos,camera));
				break;
		}
	}
	
	/** Method to add a Sprite that needs to be removed to an Arraylist cache,
	 * that is emptied at the end of every world update.
	 * @param toRemove, the sprite to be destroyed.
	 */
	public void removeSprite(Sprite toRemove) {
		this.toDestroy.add(toRemove);
	}

	/** Method to actually destroy a sprite and free its memory
	 * @param toRemove, the sprite to be destroyed.
	 */
	private void destroySprite(Sprite toRemove) {
		if (toRemove instanceof Resource) {
			this.resources.remove(toRemove);
			this.resources.trimToSize();
		} else if (toRemove instanceof Truck) {
			this.units.remove(toRemove);
			this.resources.trimToSize();
		}
	}
	
	/*-------------------------------------------------------------------------*/
	
}
	
	
	
