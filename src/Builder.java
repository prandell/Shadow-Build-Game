import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/**
 * Builder Class that extends Unit. Creates Factories and is Created 
 * by Command Centres.
 */
public class Builder extends Unit implements Trainable {

	/* -------------------- Static Variables & Constants ---------------------*/
	/** String storing Image path */
	private static final String BUILD_IMG ="assets/units/builder.png";
	/** Speed of Builder */
	private static final float SPEED=0.1f;
	
	/* ------------------------- Instance Variables --------------------------*/
	/** Generic Trainable boolean indicating Sprite is training */
	private boolean isBusy=false;
	/** Generic Trainable pastTime variable used to program delays */
	private int pastTime=0;
	
	/*-------------------------- Builder Methods -----------------------------*/
    /** Builder Constructor.
     * @param x, the initial x coordinate of the Builder.
     * @param y, the initial y coordinate of the Builder.
     * @param camera, the camera which will handle it's rendered location.
     */
	public Builder(float x, float y, Camera camera) throws SlickException {
		super(x, y, camera);
		this.setImage(BUILD_IMG);
	}
	
	/*---------------------- Inherited Sprite Methods ------------------------*/
    /**	Update method handles movement, and Truck creation if selected. Once 
     * given a command, update proceeds to handle the command until completion.
     * Builders can only be doing one thing at any given time.
     * @param world, the world object passing through delta and input.
     */
	@Override
	public void update(World world) {
		
		if (this.selected && (!isBusy)) {
			Input input=world.getInput();
			
			//If selected, either instructed to move.
			if (input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
				setMoveUnit(true);
				camera.centerOn(this);
				this.setDestination((float)(camera.screenXToGlobalX(input.getMouseX())),
						(float)(camera.screenYToGlobalY(input.getMouseY())));
			
			//Or instructed to build.
			} else if (input.isKeyPressed(Input.KEY_1)) {
				if (world.canBuildHere(this.getPosition()) && 
						world.grantResource(Trainable.FACT_COST)) {
					isBusy=true;
					setMoveUnit(false);
					train(world);
				}
				camera.centerOn(this);
			}
		}
		
		//If already moving or building, continue to do so.
		if (isMoveUnit()==true) {
			this.move(world,SPEED);
		}
		
		if (isBusy) {
			train(world);
		}
		
	}
	
	/*--------------------- Implemented Trainable Methods --------------------*/
    /**	implemented train method that uses the pastTime variable to
     * keep track of training completion. Once enough time has passed, a
     * Factory is created at the Builders' location.
     * @param world, the world object passing through delta
     */
	@Override
	public void train(World world) {
		int delta = world.getDelta();
		
		//If we haven't waited long enough, add more time to the counter.
		if (this.pastTime < Trainable.TRAIN_TIME_M) {
			this.pastTime += delta;
			
		//We've waited long enough, create the Factory.
		} else {
			this.pastTime = 0;
		    isBusy=false;
		    float xPos=this.getPosition().getX();
			float yPos=this.getPosition().getY();
			
		    try {
				world.createSprite(World.FACTORY, xPos,yPos);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		    
		}
	}
	
	/*------------------------------------------------------------------------*/
	
}
