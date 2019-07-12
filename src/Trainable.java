/**
 * Trainable interface, inherited by any Building or Unit which can
 * train and create other Buildings or Units. Stores mostly the 
 * different costs and weight times associated with training, as
 * well as the method to do so.
 */
public interface Trainable {
	
	/* -------------------- Static Variables & Constants ---------------------*/
	/** Cost in Metal required to create a Scout */
	public static final int SCOUT_COST = 5;
	/** Cost in Metal required to create a Builder */
	public static final int BUILD_COST =10;
	/** Cost in Metal required to create an Engineer */
	public static final int ENG_COST= 20;
	/** Cost in Metal required to create a Truck */
	public static final int TRUCK_COST = 150;
	/** Cost in Metal required to create a Factory */
	public static final int FACT_COST = 100;
	/** Cost in Metal required to create a Command Centre */
	public static final int CENTRE_COST=0;
	/** Large time delay in milliseconds required before creation */
	public static final int TRAIN_TIME_L=15000;
	/** Medium time delay in milliseconds required before creation */
	public static final int TRAIN_TIME_M=10000;
	/** Small time delay in milliseconds required before creation */
	public static final int TRAIN_TIME_S=5000;
	
	/*-------------------------- Trainable Methods ---------------------------*/
	/** Abstract train method which handles the costs, delays associated with
	 * training and creating new Sprites
	 * @param world, to pass the creation message when needed.
	 */
	public abstract void train(World world);
	
	/*------------------------------------------------------------------------*/
	
}
