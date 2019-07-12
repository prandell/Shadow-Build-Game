
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/**
 * Command Centre Class that extends Building and Implements Trainable.
 * Command Centres train all Units except trucks.
 */
public class CommandCentre extends Building implements Trainable {
	
	/* -------------------- Static Variables & Constants ---------------------*/
	/** String storing image path */
	public static final String COM_IMG="assets/buildings/command_centre.png";
	
	/* ------------------------- Instance Variables --------------------------*/
	/** Generic Trainable boolean for when Sprite is training */
	private boolean isBusy=false;
	/** Specific Trainable boolean for when training a Scout */
	private boolean trainingScout=false;
	/** Specific Trainable boolean for when training a Engineer */
	private boolean trainingEng=false;
	/** Specific Trainable boolean for when training a Builder */
	private boolean trainingBuilder=false;
	/** Generic Trainable pastTime variable used to program delays */
	private int pastTime=0;
	
	/*------------------------ Command Centre Methods ------------------------*/
    /** Command Centre Constructor.
     * @param x, the final x coordinate of the Command Centre.
     * @param y, the final y coordinate of the Command Centre.
     * @param camera, the camera which will handle it's rendered location.
     */
	public CommandCentre(float x, float y, Camera camera) throws SlickException {
		super(x, y, camera);
		this.setImage(COM_IMG);
	}

	/*---------------------- Inherited Sprite Methods ------------------------*/
    /**	Update method handles Unit creation commands if selected. Once 
     * given a command, update proceeds to handle the command until completion.
     * Command Centres can only train one Unit at a time.
     * @param world, the world object passing through delta and input.
     */
	@Override
	public void update(World world) {
		// only accept input if selected, and not already busy
		if (this.selected && (!isBusy)) {
			Input input=world.getInput();
			
			// Request to create a Scout
			if (input.isKeyPressed(Input.KEY_1)) {
				//Do we have enough metal
				if (world.grantResource(Trainable.SCOUT_COST)) {
					trainingScout=true;
					isBusy=true;
				}

			// Request to create a Builder
			} else if (input.isKeyPressed(Input.KEY_2)) {
				//Do we have enough metal
				if (world.grantResource(Trainable.BUILD_COST)) {
					trainingBuilder=true;
					isBusy=true;
				}
				
			// Request to create an Engineer
			} else if (input.isKeyPressed(Input.KEY_3)) {
				//Do we have enough metal
				if (world.grantResource(Trainable.ENG_COST)) {
					trainingEng=true;
					isBusy=true;
				}
			} 
		}
		
		//If already training, continue to do so.
		if (isBusy) {
			train(world);
		}
		
	}
	
	/*--------------------- Implemented Trainable Methods --------------------*/
    /**	Implemented train method that uses the pastTime variable to
     * keep track of training completion. Once enough time has passed, the
     * requested Unit is created at the Centres location.
     * @param world, the world object passing through delta
     */
	@Override
	public void train(World world) {
		int delta = world.getDelta();
		
		//Waiting until enough time has past
		if(this.pastTime < Trainable.TRAIN_TIME_S) {
			this.pastTime += delta;
		
		//finished waiting
		} else {
			
			//reset delay
			this.pastTime = 0;
			
			//get position
			float xPos=this.getPosition().getX();
			float yPos=this.getPosition().getY();
			
			//create whichever unit we were training
			if (trainingScout) {
				try {
					world.createSprite(World.SCOUT, xPos,yPos);
				} catch (SlickException e) {
					e.printStackTrace();
				}
			} else if (trainingBuilder) {
		        try {
					world.createSprite(World.BUILDER, xPos,yPos);
				} catch (SlickException e) {
					e.printStackTrace();
				}
			} else if (trainingEng) {
		        try {
					world.createSprite(World.ENGINEER, xPos,yPos);
				} catch (SlickException e) {
					e.printStackTrace();
				}
			}
			//no longer busy
			isBusy=false;
			trainingScout=false;
			trainingBuilder=false;
			trainingEng=false;
		       
		}
	}

	/*------------------------------------------------------------------------*/
	
}
