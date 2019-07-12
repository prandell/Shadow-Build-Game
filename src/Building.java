import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**  
 * Building Abstract Class that extends Sprite. All buildings are Selectable,
 * and have the large highlight image.
 */
public abstract class Building extends Sprite implements Selectable {
	
	/* ------------------------- Static Variables ----------------------------*/
	/** Large highlight image constant among all buildings */
	public static Image highlight;
	
	/* ------------------------- Instance Variables --------------------------*/
	/** Generic Selectable boolean to indicate the Sprite is selected */
	public boolean selected=false;
	
	/*-------------------------- Building Methods ----------------------------*/
    /** Building Constructor.
     * @param x, the final x coordinate of the Building.
     * @param y, the final y coordinate of the Building.
     * @param camera, the camera which will handle it's rendered location.
     */
	public Building(float x, float y, Camera camera) throws SlickException {
		super(x, y, camera);
		Building.highlight=new Image(Selectable.HIGHLIGHT_L);
	}
	
	/*---------------------- Inherited Sprite Methods ------------------------*/
    /** Render method. All buildings render in the same way, and thus no
     * child classes override this method further.
     */
	@Override
	public void render() {
		//render the highlight if we're selected.
		if (this.selected) {
			this.highlight();
		}
		
		this.getImage().drawCentered((int)camera.globalXToScreenX(this.getPosition().getX()),
				(int)camera.globalYToScreenY(this.getPosition().getY()));
	}
	
	/*-------------------- Implemented Selectable Methods --------------------*/
    /** Implemented highlight method. Same for all buildings, as all buildings
     * use the large highlight image.
     */
	@Override
	public void highlight() {
		Building.highlight.drawCentered((int)camera.globalXToScreenX(this.getPosition().getX()),
				(int)camera.globalYToScreenY(this.getPosition().getY()));
	}
	
    /** Implemented select method. Simply sets a boolean to let the Building
     * know whether it can accept input, should be highlighted, and the camera
     * be centred over it.
     */
	@Override
	public void select() {
		this.selected=true;
		camera.centerOn(this);
	}

    /** Implemented deSelect method. Opposite to select.
     */
	@Override
	public void deSelect() {
		this.selected=false;
	}
	
	/*-----------------------------------------------------------------------*/
	
}
