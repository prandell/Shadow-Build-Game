/**
 * Selectable interface, inherited by Buildings and Units. Has abstract methods
 * for selecting and deselecting, as well as highlighting. Stores the paths
 * of the highlight images and the tolerance by which a Sprite can be selected.
 */
public interface Selectable {
	
	/* -------------------- Static Variables & Constants ---------------------*/
	/** The blanket distance tolerance for selection and other game actions */
	static final double TOLERANCE = 32;
	/** String storing the image path for building highlights */
	static final String HIGHLIGHT_L = "assets/highlight_large.png";
	/** String storing the image path for unit highlights */
	static final String HIGHLIGHT = "assets/highlight.png";
	
	/*------------------------- Selectable Methods ---------------------------*/
	/** select method, instructs a selectable sprite to set it's selected 
	 * boolean to true, which in turn allows a series of extra commands to be
	 * given, and highlights the Sprite
	 */
	public abstract void select();
	
	/** deSelect method, instructs a selectable sprite to set it's selected 
	 * boolean to false, removing the highlight and preventing input parsing
	 */
	public abstract void deSelect();
	
	/** highlight method, simply renders the appropriate image prior to 
	 * rendering the image of the selected sprite.
	 */
	public abstract void highlight();
	
	/*------------------------------------------------------------------------*/
	
}
