import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
/** 
 * Truck Class, extends Unit and implements Trainable. Trucks are created by
 * Factories and can train new command centres, being destroyed themselves
 * once they have done so. 
 */
public class Truck extends Unit implements Trainable {
	
	/* -------------------- Static Variables & Constants ---------------------*/
	/** String storing truck image path */
	private static final String TRUCK_IMG ="assets/units/truck.png";
	/** Speed of a truck */
	private static final float SPEED=0.25f;
	
	/* ------------------------- Instance Variables --------------------------*/
	/** Generic Trainable boolean indicating whether Truck is busy training */
	private boolean isBusy=false;
	/** Generic Trainable pastTime variable used to program delays */
	private int pastTime=0;
	
	/*--------------------------- Truck Methods ------------------------------*/
	/** Truck Constructor.
     * @param x, the initial x coordinate of the Truck.
     * @param y, the initial y coordinate of the Truck.
     * @param camera, the camera which will handle it's rendered location.
	 */
	public Truck(float x, float y, Camera camera) throws SlickException {
		super(x, y, camera);
		this.setImage(TRUCK_IMG);
	}

	/*---------------------- Inherited Sprite Methods ------------------------*/
    /**	Update method handles movement if selected, as well as the training of
     * Command Centres if instructed to do so.
     * @param world, the world object passing through delta and input.
     */
	@Override
	public void update(World world) {
		//only pass input if selected, and not already training
		if (this.selected && (!isBusy)) {
			
			Input input=world.getInput();
			
			//Move
			if (input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
				setMoveUnit(true);
				camera.centerOn(this);
				this.setDestination((float)(camera.screenXToGlobalX(input.getMouseX())),
						(float)(camera.screenYToGlobalY(input.getMouseY())));
			//Or train
			} else if (input.isKeyPressed(Input.KEY_1)) {
				if (world.canBuildHere(this.getPosition()) && world.grantResource(CENTRE_COST)) {
					isBusy=true;
					//halt movement when training.
					setMoveUnit(false);
					train(world);
				}
				camera.centerOn(this);
			}
		}
		
		//If moving, continue
		if (isMoveUnit()==true) {
			this.move(world,SPEED);
		}
		
		//if training, continue
		if (isBusy) {
			train(world);
		}
	}
	
	/*--------------------- Implemented Trainable Methods --------------------*/
    /**	Implemented train method that uses the pastTime variable to
     * keep track of training completion. Once enough time has passed, a
     * Command Centre is created at the Trucks location, and the Truck is 
     * passed to the cache to be destroyed.
     * @param world, the world object passing through delta
     */
	public void train(World world) {
		int delta = world.getDelta();
		
		//While waiting increase time
		if (this.pastTime < Trainable.TRAIN_TIME_L) {
			this.pastTime += delta;
			
		//No longer waiting
		} else {
			//Reset delay, create Cmnd Cntr at location, pass self to cache.
			this.pastTime = 0;
		    float xPos=this.getPosition().getX();
			float yPos=this.getPosition().getY();
		    try {
				world.createSprite(World.COMMAND_CNTR, xPos,yPos);
				world.removeSprite(this);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		    isBusy=false;
		}
	}
	
	/*------------------------------------------------------------------------*/

}
