import java.util.ArrayList;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/** 
 * Engineer Class that extends Unit. Mines resources and is created by
 * Command Centres.
 */
public class Engineer extends Unit {

	/* -------------------- Static Variables & Constants ---------------------*/
	/** String storing image path */
	private static final String ENG_IMG ="assets/units/engineer.png";
	/** Speed of Engineer */
	private static final float SPEED=0.1f;
	/** Milliseconds an Engineer must spend at a Resource to carry it */
	private static final int MINE_TIME=5000;
	
	/* ------------------------- Instance Variables --------------------------*/
	/** Capacity of any Resource that can be carried, updated by Pylons */
	public static int allowance = 2;
	/** Stored position of the last successfully mined Resource */
	private Position currentMine;
	/** Boolean indicating whether metal is being carried */
	private boolean carryingMetal=false;
	/** Boolean indicating whether Unobtainium is being carried */
	private boolean carryingUnob=false;
	/** mineTime variable used to program delays during mining */
	private int mineTime=0;
	
	/*-------------------------- Engineer Methods ----------------------------*/
    /** Engineer Constructor.
     * @param x, the initial x coordinate of the Engineer.
     * @param y, the initial y coordinate of the Engineer.
     * @param camera, the camera which will handle it's rendered location.
     */
	public Engineer(float x, float y, Camera camera) throws SlickException {
		super(x, y, camera);
		this.setImage(ENG_IMG);
	}
	
	/*---------------------- Inherited Sprite Methods ------------------------*/
    /**	Update method handles movement if selected, as well as automatic
     * mining and dumping. Once mining, Engineers will continue to mine
     * and dump resources until the mine is depleted.
     * @param world, the world object passing through delta and input.
     */
	@Override
	public void update(World world) {
		// If selected, take movement input.
		if (selected) {
			if (world.getInput().isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
				Input input = world.getInput();
				setMoveUnit(true);
				camera.centerOn(this);
				this.setDestination((float)(camera.screenXToGlobalX(input.getMouseX())),
						(float)(camera.screenYToGlobalY(input.getMouseY())));
			}
		}
		
		//If moving, continue.
		if (isMoveUnit()==true) {
			this.move(world,SPEED);
		}
		
		//If carrying resources, continue to dump otherwise mine.
		if (carryingMetal || carryingUnob) {
			this.dump(world);
		} else {
			mine(world);
		}
		
	}
	
	/*------------------------ Private Mining Methods ------------------------*/
    /**	Mine method that uses the inherited pastTime Sprite variable to
     * keep track of mining completion. Once enough time has passed, the
     * Engineer searches for the nearest Command Centre.
     * @param world, the world object passing through delta and it's mines.
     */
	private void mine(World world) {
		ArrayList<Resource> mines=world.getResources();
		int delta = world.getDelta();
		//Check for all mines, are we close to one
		for (Resource r: mines) {
			
			if (Position.distance(this.getPosition(), r.getPosition()) 
					< Selectable.TOLERANCE) {
				//if we are, increase the counter
				if(mineTime < MINE_TIME) { 
			        mineTime += delta;
			        break;
			    //if we've waited long enough, find the nearest command centre
			    //carrying the resource of the mine, remembering it's position.
			    } else {
			        currentMine=r.getPosition();
			        mineTime = 0;
			        
			        if (r.getType().equals(World.METAL)) {
			        	r.deplete(Engineer.allowance);
			        	carryingMetal=true;
			        	this.findCentre(world);
			        	break;
			        	
			        } else {
			        	r.deplete(Engineer.allowance);
			        	carryingUnob=true;
			        	this.findCentre(world);
			        	break;
			        }
			    }
			}
		}
	}
	
    /**	Command Centre finding method that searches all existing command 
     * centres, and sets the closest as our destination, and allows us to
     * move there.
     * @param world, the world object passing it's Command Centres'.
     */
	private void findCentre(World world) {
		
		//Set the closest as the first one we find initially.
		ArrayList<Building> buildings = world.getBuildings();
		Position closest=null;
		for (Building b: buildings) {
			if (b instanceof CommandCentre) {
				closest = b.getPosition();
				break;
			}
		}
		
		//See if there are any closer ones.
		double distance = Position.distance(this.getPosition(), closest);
		for (Building b: buildings) {
			if ((b instanceof CommandCentre) && 
					(Position.distance(this.getPosition(), b.getPosition()) 
							< distance)) {
				closest=b.getPosition();
			}
		}
		//Set our destination and allow us to move.
		this.setDestination(closest.getX(), closest.getY());
		setMoveUnit(true);
	}
	
    /**	Resource dumping method that checks if we're close to a command centre,
     * and if we are it dumps the resources and returns to the mine we came from.
     * @param world, the world object passing it's Command Centres'.
     */
	private void dump(World world) {
		ArrayList<Building> buildings = world.getBuildings();
		
		//for all buildings
		for (Building b: buildings) {
			//for all command centres
			if (b instanceof CommandCentre) {
				
				//if were close enough
				if (Position.distance(this.getPosition(), b.getPosition()) 
						< Selectable.TOLERANCE) {
					
					//dump Resource
					if (carryingMetal) {
						world.resourceIncrease(World.METAL);	
					} else {
						world.resourceIncrease(World.UNOBTAINIUM);
					}
					
					//no longer carrying Resource
					carryingUnob=false;
					carryingMetal=false;
					
					//send back to current mine
					this.setDestination(currentMine);
					setMoveUnit(true);
				}
			}
		}			
	}

	/*-----------------------------------------------------------------------*/
	
}
