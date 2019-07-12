import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/**
 * Factory Class that extends Building and Implements Trainable.
 * Factories train trucks.
 */
public class Factory extends Building implements Trainable {
	
	/* -------------------- Static Variables & Constants ---------------------*/
	/** String storing image location */
	public static final String FACT_IMG="assets/buildings/factory.png";
	
	/* ------------------------- Instance Variables --------------------------*/
	/** Generic Trainable boolean indicating if Sprite is training */
	private boolean isBusy=false;
	/** Generic Trainable pastTime variable used to program delays */
	private int pastTime=0;
	
	/*--------------------------- Factory Methods ----------------------------*/
    /** Factory Constructor.
     * @param x, the final x coordinate of the Factory.
     * @param y, the final y coordinate of the Factory.
     * @param camera, the camera which will handle it's rendered location.
     */
	public Factory(float x, float y, Camera camera) throws SlickException {
		super(x, y, camera);
		this.setImage(FACT_IMG);
	}
	
	/*---------------------- Inherited Sprite Methods ------------------------*/
    /**	Update method handles Truck creation command if selected. Once 
     * given a command, update proceeds to handle the command until completion.
     * Factories can only train one truck at a time.
     * @param world, the world object passing through delta and input.
     */
	@Override
	public void update(World world) {
		//If selected and not already training.
		if (this.selected && (!isBusy)) {
			Input input=world.getInput();
			
			if (input.isKeyPressed(Input.KEY_1)) {
				if (world.grantResource(Trainable.TRUCK_COST)) {
					isBusy=true;
					train(world);
				}
			}
		}
		
		//If training, continue.
		if (isBusy) {
			train(world);
		}
	}
	
	/*--------------------- Implemented Trainable Methods --------------------*/
    /**	Implemented train method that uses the pastTime variable to
     * keep track of training completion. Once enough time has passed, a
     * Truck is created at the Factories location.
     * @param world, the world object passing through delta
     */
	@Override
	public void train(World world) {
		int delta = world.getDelta();
		//If we haven't waited long enough, add more time to counter.
		if (this.pastTime < Trainable.TRAIN_TIME_S) {
			this.pastTime += delta;
		//we've waited long enough, create the Truck.
		} else {
			this.pastTime = 0;
		    isBusy=false;
		    float xPos=this.getPosition().getX();
			float yPos=this.getPosition().getY();
			
		    try {
				world.createSprite(World.TRUCK, xPos,yPos);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}

	/*------------------------------------------------------------------------*/
	
}
