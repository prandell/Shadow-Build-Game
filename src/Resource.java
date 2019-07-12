import org.newdawn.slick.SlickException;

/**
 * Resource class that extends Sprite. Resources have a position and type,
 * which determine its location on the map, and capacity. Resources are
 * mined by Engineers.
 */
public class Resource extends Sprite {
	
	/* -------------------- Static Variables & Constants ---------------------*/
	/** Int representing the capacity of Metal Resource mines */
	private static final int METAL_CAPACITY=500;
	/** Int representing the capacity of Unobtainium Resource mines */
	private static final int UNOB_CAPACITY=50;
	/** String storing the path of the image for Metal mines */
	private static final String METAL_IMG ="assets/resources/metal_mine.png";
	/** String storing the path of the image for Unobtainium mines */
	private static final String UNOB_IMG ="assets/resources/unobtainium_mine.png";
	
	/* ------------------------- Instance Variables --------------------------*/
	/** String storing the type of a Resource instance */
	private String type;
	/** Int storing the capacity of the Resource at any given moment */
	private int capacity;
	
	/*-------------------------- Resource Methods ----------------------------*/
	/** Resource Constructor, determines the initial capacity, and Sprite image
	 * from the passed input type. Sets location and passes the camera.
     * @param x, the final x coordinate of the Resource.
     * @param y, the final y coordinate of the Resource.
     * @param camera, the camera which will handle it's rendered location.
	 * @param type, the type of resource.
	 */
	public Resource(float x, float y, Camera camera, String type) throws SlickException {
		super(x, y, camera);
		if (type.equals(World.METAL)) {
			this.setType(World.METAL);
			this.capacity=METAL_CAPACITY;
			this.setImage(METAL_IMG);
		} else if (type.equals(World.UNOBTAINIUM)) {
			this.setType(World.UNOBTAINIUM);
			this.capacity=UNOB_CAPACITY;
			this.setImage(UNOB_IMG);
		}
		
	}
	
	/** Essentially a getter to determine whether the capacity has fallen 
	 * below zero. If so, the Resource mine should be destroyed.
	 * @return boolean, whether or not capacity is zero or less.
	 */
	public boolean isEmpty() {
		if (this.capacity<=0) {
			return true;
		} else {
			return false;
		}
	}
	
	/** Public deplete method, called by engineers whilst mining, to reduce
	 * the capacity of a Resource mine and carry this reduction to a command
	 * centre.
	 * @param reduction, the amount the engineer is taking.
	 */
	public void deplete(int reduction) {
		if (this.capacity-reduction <= 0) {
			this.capacity=0;
		} else {
			this.capacity -= reduction;
		}
	}
	
	/** Public Type getter method used by Engineers to determine which type
	 * of Resource they will be carrying.
	 * @return type, the String of the Resource type.
	 */
	public String getType() {
		return type;
	}

	/** Private Type setter method used only upon construction, such that
	 * the type of a Resource shouldn't be changed afterwards.
	 * @param type, the type to be set to.
	 */
	private void setType(String type) {
		this.type = type;
	}
	
	/*---------------------- Inherited Sprite Methods ------------------------*/
    /**	Render method draws the Resource image onto the screen, translated by
     * it's camera.
     */
	@Override
	public void render() {
		this.getImage().drawCentered((int)camera.globalXToScreenX(this.getPosition().getX()),
				(int)camera.globalYToScreenY(this.getPosition().getY()));	
	}

    /**	Update method checks for if the Resource is empty, so that it should be
     * destroyed.
     * @param world, to add the Resource to the remove cache if empty.
     */
	@Override
	public void update(World world) {
		if (this.isEmpty()) {
			world.removeSprite(this);
		}
	}
	
	/*------------------------------------------------------------------------*/
}
