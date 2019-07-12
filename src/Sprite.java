import org.newdawn.slick.*;

/** Abstract Sprite super class. This class is the parent class of all render-able game
 * objects. All sprites have a position, image, and camera to translate positions
 * to rendered locations on the screen.
 */
public abstract class Sprite {
	
	/* ------------------------- Instance Variables --------------------------*/
	/** Position variable belonging to all sprites, may be permanent or not */
	private Position position;
	/** Image variable to be set in the constructor of all child classes */
	private Image image;
	/** Camera class belonging to all sprites used for coordinate translation */
	public Camera camera;
   
	/*--------------------- Inheritable Sprite Methods -----------------------*/
    /** Abstract Render method. All Buildings and Units inherit this method
     */
    public abstract void render();
   
    /** Abstract Update method. All Buildings and Units inherit this method, and this
     * method commands the way in which any Sprite functions.
     * @param world, used to pass whatever is needed for the class to function.
     */
    public abstract void update(World world);
    
    /** Sprite Constructor. Every inheriting class follows this template,
     * with or without the addition of extra functionality.
     * @param x, the x coordinate upon creation
     * @param y, the y coordinate upon creation
     * @param camera, the camera used to translate coordinates.
     */
    public Sprite(float x, float y, Camera camera) {
       this.position = new Position(x,y);
       this.camera=camera;
    }
    
	/*-------------------------- Getters & Setters ---------------------------*/
    /** Position getter.
     * @return Position copy
     */
    public Position getPosition() {
    	Position dummy = new Position(this.position.getX(),this.position.getY());
    	return dummy;
    }
    
    /** Position setter
     * @param x, x coordinate to be set
     * @param y, y coordinate to be set
     */
    public void setPosition(float x,float y) {
    	this.position.setX(x);
    	this.position.setY(y);
    }
    
    /** Image setter, called generally in the constructor of a Sprite
     * @param imageLoc, the location of the path of the image.
     */
    public void setImage(String imageLoc) throws SlickException {
    	this.image= new Image(imageLoc);
    }
    
    /** Image getter, called in the render method of a Sprite.
     * @return Image, the image to be rendered.
     */
    public Image getImage() {
    	return this.image;
    }
    
	/*-----------------------------------------------------------------------*/
    
}