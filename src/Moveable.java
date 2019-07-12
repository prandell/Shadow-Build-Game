/**
 * Moveable Interface, implemented by any class that 
 * needs to progressively move to a location.
 */
public interface Moveable {
	
	/* -------------------- Static Variables & Constants ---------------------*/
	/** Tolerance for arrival at a destination  */
	static final double STOP_DIST=0.25;	
	
	/*--------------------------- Moveable Methods ---------------------------*/
	/** Move method, used to calculate progressive position updates when given
	 * a destination.
     * @param world, to pass input.
     * @param speed, the speed at which an object should move.
     */
	public abstract void move(World world,float speed);
	
	/** Stop method, used to halt the move method, either by force or upon 
	 * arrival at a destination.
     */
	public abstract void stop();
	
	/*------------------------------------------------------------------------*/
}
