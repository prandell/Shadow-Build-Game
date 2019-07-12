import org.newdawn.slick.*;


/**
 * Scout class that extends unit. Sprite is the bare bones of the unit class,
 * which simply has a high speed, and can move around the map searching
 * for Pylons to activate.
 */
public class Scout extends Unit {

	/* ---------------------------- Constants --------------------------------*/
	/** Speed of scout float */
	private static final float SPEED = 0.3f;
	/** String storing scout image path */
  	private static final String SCOUT_IMG ="assets/units/scout.png";
  	
   /*---------------------------- Scout Methods ------------------------------*/
    /** Scout Constructor.
     * @param x, the initial x coordinate of the Scout.
     * @param y, the initial y coordinate of the Scout.
     * @param camera, the camera which will handle it's rendered location.
     */
   public Scout(float x, float y, Camera camera) throws SlickException {
	   super(x,y,camera);
	   this.setImage(SCOUT_IMG);
   }
	/*---------------------- Inherited Sprite Methods ------------------------*/
   /**	Update method handles movement.
    * @param world, the world object passing through delta and input.
    */
   @Override
   public void update(World world) {
	   //If selected, get input and move
	   if (selected) { 
		   Input input = world.getInput();
		   if (input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
			   setMoveUnit(true);
			   camera.centerOn(this);
			   this.setDestination((float)(camera.screenXToGlobalX(input.getMouseX())),
					   (float)(camera.screenYToGlobalY(input.getMouseY())));

		   }
	   }
	    
	   if(isMoveUnit() == true) {
		   this.move(world,SPEED);
	   }
	             
	}
   
   /*------------------------------------------------------------------------*/
}