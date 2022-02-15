package ca.mcgill.ecse.climbsafe.persistence;

import java.sql.Date;
import java.time.LocalDate;

import ca.mcgill.ecse.climbsafe.application.ClimbSafeApplication;
import ca.mcgill.ecse.climbsafe.model.ClimbSafe;

/**
 * 
 * @author AbdelrahmanAli
 *
 */
public class ClimbSafePersistence {

	private static String filename = "ClimbSafeDemo.data";

	  /**
	   * This methods sets the file name of the object
	   * 
	   * @author AbdelrahmanAli
	   * @param filename to serialize the object in that file
	   */
	public static void setFilename(String filename) {
	    ClimbSafePersistence.filename = filename;
	  }

	 /**
	  *  This method save the exist climbSafe instance in 
	  *  the system into the persistence layer
	  * @author AbdelrahmanAli
	  */
	public static void save() {
	    PersistenceObjectStream.setFilename(filename);
	    save(ClimbSafeApplication.getClimbSafe());
	  }

	  /**
	   * This method saves the passed climbSafe as a serialized object
	   * @param climbSafe
	   */
	public static void save(ClimbSafe climbSafe) {
	    PersistenceObjectStream.setFilename(filename);
	    PersistenceObjectStream.serialize(climbSafe);
	  }

	  /** This method deserialize the climbSafe object if 
	   * any was saved before
	   * @return the saved climbSafe object or a new one
	   */
	public static ClimbSafe load() {
	    PersistenceObjectStream.setFilename(filename);
	    ClimbSafe climbSafe = (ClimbSafe) PersistenceObjectStream.deserialize();
	    // model cannot be loaded - create empty BTMS
	    if (climbSafe == null) {
	    	climbSafe = new ClimbSafe(Date.valueOf(LocalDate.MIN), 0, 0);		//set a default value, can be changed later using setter methods
	    } else {
	    	climbSafe.reinitialize();
	    }
	    return climbSafe;
	  }
}
