import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


/* Abstract Parent Unit Class that defines all variables and methods common
 * to all units in the game. All units are Moveable and Selectable.
 */
/**  
 * Abstract Unit Parent Class that defines, implements Moveable and Selectable.
 * All units move in the same way, except for the speed. All units are 
 * selected, highlighted and rendered in the same way.
 */
public abstract class Unit extends Sprite implements Moveable, Selectable {
	
	/* ------------------------- Static Variables ----------------------------*/
	/** Small highlight image constant among all buildings */
	public static Image highlight;
	
	/*-----------------------------Instance variables ------------------------*/
	/** Boolean to guard movement */
	private boolean moveUnit=false;
	/** Position destination determines where to move to, set by input */
	private Position destination;
	/** Boolean selection, used to guard against input and highlighting */
	public boolean selected=false;

	/*----------------------------- Unit Methods -----------------------------*/
    /** Unit Constructor. Sets highlight and destination.
     * @param x, the initial x coordinate of the Unit.
     * @param y, the initial y coordinate of the Unit.
     * @param camera, the camera which will handle it's rendered location.
     */
	public Unit(float x, float y, Camera camera) throws SlickException {
		super(x, y, camera);
		float dummyX=x;
		float dummyY=y;
		//initial destination is current position.
		this.destination=new Position(dummyX,dummyY);
		Unit.highlight= new Image(Selectable.HIGHLIGHT);
	}
	
	/*---------------------- Inherited Sprite Methods ------------------------*/
    /** Render method. All Units render in the same way, and thus no
     * child classes override this method further.
     */
	@Override
	public void render() {
		if (this.selected) {
			this.highlight();
		}
		this.getImage().drawCentered((int)camera.globalXToScreenX(this.getPosition().getX()),
				(int)camera.globalYToScreenY(this.getPosition().getY()));
		
	}
	
	/*-------------------- Implemented Selectable Methods --------------------*/
	/** highlight method, simply renders the highlight image at the translated
	 * unit position
	 */
	@Override
	public void highlight() {
		Unit.highlight.drawCentered((int)camera.globalXToScreenX(this.getPosition().getX()),
				(int)camera.globalYToScreenY(this.getPosition().getY()));
	}
	
	/** select method, simply sets the boolean "selected" to true, and centres
	 * the camera over its location
	 */
	@Override
	public void select() {
		this.selected=true;
		camera.centerOn(this);
	}
	
	/** deselect method, resets the "selected" boolean to false
	 */
	@Override
	public void deSelect() {
		this.selected=false;
	}

	
	/*--------------------- Implemented Moveable Methods ---------------------*/
	/** Standard move method implementation, constant across all units. uses 
	 * the position class to calculate distance and direction, and uses this
	 * and worlds delta to progressively move a unit towards its destination.
	 * @param world, the world object passing delta.
	 * @param speed, the speed at which the unit should move.
	 */
	@Override
	public void move(World world,float speed) {
		int delta=world.getDelta();
		
		//calculate the distance to the destination from current position
		double distance = Position.distance(this.getPosition(),
				this.getDestination());
		
		//If close to destination, stop moving
		if (distance <= STOP_DIST) {
			stop();
			
		//Otherwise, get direction and move a small distance toward it.
		} else {
			double direction = this.getPosition().direction(this.getDestination());
			
			// Calculate the appropriate dx and dy distances
			double dx = (double)Math.cos(direction) * delta * speed;
			double dy = (double)Math.sin(direction) * delta * speed;
			
			// Check the tile is free before moving; otherwise, we stop moving
			if (world.isPositionFree(this.getPosition().getX() + dx,
					this.getPosition().getY() + dy)) {
				this.setPosition((float)(this.getPosition().getX() + dx),
							(float)(this.getPosition().getY() + dy));
			} else {
				stop();
			}
		}
	}
	
	/** Stop method which simply sets the boolean guard to false. The move
	 * method is not called when the guard is false.
	 */
	@Override
	public void stop() {
		this.setMoveUnit(false);
		
	}
	
	/*--------------------------- Getters & Setters --------------------------*/
	/** Destination getter, used in the move method to calculate distance and
	 * direction
	 * @return Position of destination copy
	 */
    public Position getDestination() {
    	Position dummy = new Position(this.destination.getX(),this.destination.getY());
    	return dummy;
    }
    
    /** Destination setter, used in update or elsewhere to set the unit moving
     * toward the position set.
     * @param x, x coordinate of destination.
     * @param y, y coordinate of destination.
     */
    public void setDestination(float x,float y) {
    	this.destination.setX(x);
    	this.destination.setY(y);
    }
    
    /** Destination setter, used in update or elsewhere to set the unit moving
     * toward the position set.
     * @param Position other, the exact position of the destination.
     */
    public void setDestination(Position other) {
    	this.destination=other;
    }
    
    /** moveUnit getter, used to determine if a unit is moving, so that it 
     * cannot be instructed to do anything else, in certain cases.
     * @return boolean, indicating whether unit is moving or not (T or F).
     */
	public boolean isMoveUnit() {
		return moveUnit;
	}
	
	/** moveUnit setter, used to set the boolean to true or false, depending
	 * on whether we want to move or stop a unit.
	 * @param moveUnit, the boolean to be set.
	 */
	public void setMoveUnit(boolean moveUnit) {
		this.moveUnit = moveUnit;
	}
	
	/*------------------------------------------------------------------------*/
	
}
