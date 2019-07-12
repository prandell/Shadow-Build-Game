import java.util.ArrayList;

import org.newdawn.slick.SlickException;

/**
 * Pylon class that extends building.
 * Pylons are either inactive or active, the latter increases the
 * amount of resources an engineer can carry.
 */
public class Pylon extends Building {
	/* -------------------- Static Variables & Constants ---------------------*/
	/** String storing the active pylon image path */
	public static final String ACT_IMG="assets/buildings/pylon_active.png";
	/** String storing the inactive pylon image path */
	public static final String INACT_IMG="assets/buildings/pylon.png";
	
	/* ------------------------- Instance Variables --------------------------*/
	/** boolean variable determining whether an instance is active or not */
	private boolean active = false;

	/*--------------------------- Pylon Methods ------------------------------*/
    /** Pylon Constructor.
     * @param x, the final x coordinate of the Pylon.
     * @param y, the final y coordinate of the Pylon.
     * @param camera, the camera which will handle it's rendered location.
     */
	public Pylon(float x, float y, Camera camera) throws SlickException {
		super(x, y, camera);
		this.setImage(INACT_IMG);
		
	}
	
	/** Method called by update, whence a unit is close to the pylon's position.
	 * This method then increases the allowance of all engineers, and sets
	 * itself to active, changing it's image in the process
	 */
	private void activate() {
		Engineer.allowance++;
		active=true;
		try {
			this.setImage(ACT_IMG);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	/** Getter method to determine whether a pylon is active or not.
	 * used to display strings on the HUD by world.
	 * @return state, active or inactive (true or false)
	 */
	public boolean isActive() {
		return active;
	}


	/*---------------------- Inherited Sprite Methods ------------------------*/
    /**	Update method checks, for all units, whether or not there is one within
     * TOLERANCE of it's position, and activates if so.
     * @param world, the world object passing through it's stored units.
     */
	@Override
	public void update(World world) {
		//While inactive
		if (!active) {
			ArrayList<Unit> units=world.getUnits();
			//For all units
			for (Unit u: units) {
				//Are they close enough
				if (Position.distance(this.getPosition(), u.getPosition())
						< Selectable.TOLERANCE) {
					//If yes, activate
					this.activate();
				}
			}
		}
	}
	
	/*------------------------------------------------------------------------*/
	
}
