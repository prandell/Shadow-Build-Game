/**
 * Position class, every Sprite has at least one position.
 * Class also has public methods to do with distance and
 * direction.
 */
public class Position {
	
	/* ------------------------- Instance Variables --------------------------*/
	/** The main purpose of the position class, to store the x/y coordinates */
	private float x,y;
	
	/*-------------------------- Position Methods ----------------------------*/
	/** Position Constructor.
     * @param x, the x coordinate of the position
     * @param y, the y coordinate of the position
     */
	public Position(float x, float y) {
		this.x=x;
		this.y=y;
	}
	
	/** distance method, between this position and another.
	 * @param Other position
	 * @return double distance between this and other.
	 */
	public double distance(Position other) {
		return (Math.sqrt(Math.pow(other.getX() - this.x, 2)
				   + Math.pow(other.getY() - this.y, 2)));
	}
	
	/** direction method, from this position to another.
	 * @param Other position
	 * @return double direction from this to other.
	 */
	public double direction(Position other) {
		return (Math.atan2(other.getY()-this.getY(), other.getX() - this.getX()));
	}
	
	/*--------------------------- Getters & Setters --------------------------*/
	/** x coordinate setter
	 * @param x
	 */
	public void setX(float x) {
		if (x<0) {
			return;
		} else {
			this.x=x;
		}
	}
	
	/** y coordinate setter
	 * @param y
	 */
	public void setY(float y) {
		if (y<0) {
			return;
		} else {
			this.y=y;
		}
	}
	
	/** x coordinate getter
	 * @return x coordinate copy
	 */
	public float getX() {
		float dummy=x;
	   return dummy;
	}
	
	/** y coordinate getter
	 * @return y coordinate copy
	 */
	public float getY() {
		float dummy=y;
	   return dummy;
	}

	/*----------------------- Public static methods --------------------------*/
	/** distance method, between two separate positions.
	 * @param from position
	 * @param to position
	 * @return double distance between from and to.
	 */
	public static double distance(Position from, Position to) {
		return (Math.sqrt(Math.pow(to.getX() - from.getX(), 2)
				   + Math.pow(to.getY() - from.getY(), 2)));
	}
	
	/** distance method, between a position and an x and y coordinate pair.
	 * @param from position
	 * @param to coordinate x
	 * @param to coordinate y
	 * @return double distance between from position and to pair.
	 */
	public static double distance(Position from, float toX, float toY) {
		return (Math.sqrt(Math.pow(toX - from.getX(), 2)
				   + Math.pow(toY - from.getY(), 2)));
	}
	
	/** distance method, between two separate x & y coordinate pairs.
	 * @param from coordinate x
	 * @param from coordinate y
	 * @param to coordinate x
	 * @param to coordinate y
	 * @return double distance between the two coordinate pairs.
	 */
	public static double distance(float fromX, float fromY, 
			float toX,float toY) {
		return (Math.sqrt(Math.pow(toX - fromX, 2)
				   + Math.pow(toY - fromY, 2)));
	}

	/** direction method, from a coordinate pair to a position.
	 * @param from coordinate x
	 * @param from coordinate y
	 * @param Other position
	 * @return double direction from this to other.
	 */
	public static double direction(float thisX, float thisY, 
			Position other) {
		return (Math.atan2(other.getY()-thisY, other.getX()-thisX));
	}
	
	/** direction method, from one coordinate pair to another.
	 * @param from coordinate x
	 * @param from coordinate y
	 * @param to coordinate x
	 * @param to coordinate y
	 * @return double direction from this to other.
	 */
	public static double direction(float thisX, float thisY, 
			float otherX, float otherY) {
		return(Math.atan2(otherY-thisY,otherX-thisX));
	}
	
	/*------------------------------------------------------------------------*/
	
}
