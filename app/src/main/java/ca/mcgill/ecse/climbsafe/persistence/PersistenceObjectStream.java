package ca.mcgill.ecse.climbsafe.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author AbdelrahmanAli
 *
 */
public class PersistenceObjectStream {

	private static String filename = "output.txt";

	  /**
	   * This method serialize the passed object into 
	   * persistence layer
	   * 
	   * @author AbdelrahmanAli
	   * @param object to be serialized 
	   */
	public static void serialize(Object object) {
	    try (var oos = new ObjectOutputStream(new FileOutputStream(new File(filename)))) {
	      oos.writeObject(object);
	    } catch (IOException e) {
	      throw new RuntimeException("Could not save data to file '" + filename + "'.");
	    }
	  }

	  /**
	   * This method deserialize climbSafe object if it was
	   * serialized before 
	   * 
	   * @author AbdelrahmanAli
	   * @return object climbSafe or null
	   */
	public static Object deserialize() {
	    try (var ois = new ObjectInputStream(new FileInputStream(new File(filename)))) {
	      return ois.readObject();
	    } catch (IOException | ClassNotFoundException e) {
	      e.printStackTrace();
	    }
	    return null;
	  }

	  /**
	   * This method will change the filename to the given value
	   * 
	   * @author AbdelrahmanAli
	   * @param newFilename where serialization will occur
	   */
	public static void setFilename(String newFilename) {
	    filename = newFilename;
	  }
}