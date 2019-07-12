import org.newdawn.slick.*;
/**
 * Camera class used to restrict the game's view to a subset of the
 * entire world. Camera can lock onto a target, or freeroam with the WASD keys.
 */
public class Camera implements Moveable {
	
	/* -------------------- Static Variables & Constants ---------------------*/
	/** Speed camera moves during WASD input, and autolock */
	private static final float SPEED=0.4f;
	
	/* ------------------------- Instance Variables --------------------------*/
	/** Target sprite (if there is one) to get position from */
	private Sprite target;
	/** camera coordinates to render the map at */
	private float cameraX, cameraY;
	/** coordinates of the target position the camera is aimimg for */
	private float targetX, targetY;
	/** previous coordinates of the target position the camera is aiming for */
	private float lastTargetX,lastTargetY;
	/** boolean that guards the use of WASD keys */
	private boolean freeRoam=false;

	/*--------------------------- Camera Methods -----------------------------*/
    /** Camera method used to lock onto a selectable target. The method itself
     * doesn't check for this condition, it's only passed selectable Sprites.
     * @param target, the Selectable Sprite to center the camera on
     */
	public void centerOn(Sprite target) {
		this.target=target;
		freeRoam=false;
	}
	
    /** Camera method used to indicate that WASD are being used to move away
     * from the previously targeted Sprite.
     */
	public void centerOff() {
		freeRoam=true;
	}
	
    /** Private Camera method to calculate the render location according to
     * which WASD key is being pressed. 
     */
	private void freeroam(World world) {
		Input input = world.getInput();
		int delta = world.getDelta();
		
		if (input.isKeyDown(Input.KEY_W)) {
			targetY=cameraY-delta*SPEED;
		} else if (input.isKeyDown(Input.KEY_S)){
			targetY=cameraY+delta*SPEED;			
		} else if (input.isKeyDown(Input.KEY_D)) { 
			targetX=cameraX+delta*SPEED;
		} else if (input.isKeyDown(Input.KEY_A)){ 
			targetX=cameraX-delta*SPEED;
		} else {}
	}
	
    /** Main Camera update method. Finds the position it needs to render at,
     * according to either the target it's following or WASD keys. Ensures
     * it isn't passing over the map edges.
     * @param world, to pass over input and delta if required.
     */
	public void update(World world) {
		//If WASD keys were pressed, move according to further WASD input
		if (freeRoam) {
			freeroam(world);
		
		//Otherwise, continue to track the target Sprite
		} else {
			this.targetX = target.getPosition().getX() - App.WINDOW_WIDTH / 2;
			this.targetY = target.getPosition().getY() - App.WINDOW_HEIGHT / 2;
		}
		
		//Set the camera coordinates ready to render the map at
		cameraX = (float) Math.min(targetX, world.MAPWIDTH - App.WINDOW_WIDTH);
		cameraX = Math.max(cameraX, 0);
		cameraY = (float) Math.min(targetY, world.MAPHEIGHT - App.WINDOW_HEIGHT);
		cameraY = Math.max(cameraY, 0);
		
		//If the last position we were meant to render at, is very far from
		// the new position, we need to progressively move to the new camera
		// position to prevent 'snapping'
		if ((Math.abs(this.cameraX-lastTargetX)> (Selectable.TOLERANCE/2)) 
				|| (Math.abs(this.cameraY-lastTargetY) > (Selectable.TOLERANCE/2))) {
			move(world,SPEED);
		}
		
		//update the last camera position
		lastTargetX=cameraX;
		lastTargetY=cameraY;
		
	}
	
	/*------------------- Coordinate Translation Methods---------------------*/
    /** X translation method used by all Sprites to determine whether or not
     * they are being rendered on the screen.
     * @param x, to be translated by cameraX.
     * @return translated x coordinate.
     */
	public double globalXToScreenX(double x) {
		return x - cameraX;
	}
	
    /** Y translation method used by all Sprites to determine whether or not
     * they are being rendered on the screen.
     * @param y, to be translated by cameraY.
     * @return translated y coordinate.
     */
	public double globalYToScreenY(double y) {
		return y - cameraY;
	}
	
    /** X translation method used by certain Sprites that need to determine 
     * whether they can move or build onto a particular map tile. 
     * @param x, to be translated by cameraX.
     * @return translated x coordinate.
     */
	public double screenXToGlobalX(double x) {
		return x + cameraX;
	}
	
    /** Y translation method used by certain Sprites that need to determine 
     * whether they can move or build onto a particular map tile. 
     * @param y, to be translated by cameraY.
     * @return translated y coordinate.
     */
	public double screenYToGlobalY(double y) {
		return y + cameraY;
	}

	/*-------------------- Implemented Moveable Methods ---------------------*/
	
	/** Implemented move method, that is used to dynamically move to the new
	 * target position without 'snapping'. Attempts to decelerate (badly).
     * @param world, to pass delta.
     * @param speed.
     */
	@Override
	public void move(World world, float speed) {
		//get the input speed
		float newSpeed = speed;
		
		//find the distance and retrieve delta
		double distance = Position.distance(lastTargetX, lastTargetY, 
				cameraX, cameraY);
		int delta=world.getDelta();
		
		//cheap attempt at deceleration. Smoother looking transitions.
		if (distance<50) {
			newSpeed=0.35f;
		} else if (distance < 35){
			newSpeed=0.25f;
		} else if (distance < 28) {
			newSpeed=0.15f;
		} else if (distance < 18) {
			newSpeed=0.10f;
		} else if (distance < 5) {
			stop();
			return;
		}
		
		//finally calculate the change in position towards the destination.
		double direction = Position.direction(lastTargetX, lastTargetY,
				cameraX, cameraY);
		double dx = (double)Math.cos(direction) * delta * newSpeed;
		double dy = (double)Math.sin(direction) * delta * newSpeed;
		this.cameraX=(float) (lastTargetX+dx);
		this.cameraY=(float) (lastTargetY+dy);
	}
	
	/** Implemented stop method, nothing is required in this case.
     */
	@Override
	public void stop() {}
	
	/*----------------------------------------------------------------------*/
	
}
