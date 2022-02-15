package ca.mcgill.ecse.climbsafe.controller;

import java.util.ArrayList;

import ca.mcgill.ecse.climbsafe.application.ClimbSafeApplication;
import ca.mcgill.ecse.climbsafe.model.BookableItem;
import ca.mcgill.ecse.climbsafe.model.ClimbSafe;
import ca.mcgill.ecse.climbsafe.model.Equipment;
import ca.mcgill.ecse.climbsafe.persistence.ClimbSafePersistence;

public class ClimbSafeFeatureSet4Controller {

	/**
	 * This method adds a new equipment to the system.
	 * @author FrederikMartin
	 * @param name Name of the equipment.
	 * @param weight Weight of the equipment.
	 * @param pricePerWeek Price of the equipment for one week.
	 * @throws InvalidInputException
	 */
  public static void addEquipment(String name, int weight, int pricePerWeek)
      throws InvalidInputException {
	  
	  ClimbSafe climbSafeApp = ClimbSafeApplication.getClimbSafe();
	  
	  checkName(name);
	  checkWeight(weight);
	  checkPricePerWeek(pricePerWeek);
	  
	  climbSafeApp.addEquipment(name, weight, pricePerWeek);
	  try {
	        ClimbSafePersistence.save();					// Update the persistence layer
	      } catch (RuntimeException e) {
	    	  throw new InvalidInputException(e.getMessage());
	        }
  }

  /**
   * This method changes the data for an existing equipment.
   * @author FrederikMartin
   * @param oldName Current name of the equipment in the system.
   * @param newName The new name of the equipment.
   * @param newWeight The new weight of the equipment.
   * @param newPricePerWeek The new price of the equipment for one week.
   * @throws InvalidInputException
   */
  public static void updateEquipment(String oldName, String newName, int newWeight,
      int newPricePerWeek) throws InvalidInputException {	 
	  
	  BookableItem item = BookableItem.getWithName(oldName);
	  if(item instanceof Equipment equipment) {
		  checkName(oldName, newName);
		  checkWeight(newWeight);
		  checkPricePerWeek(newPricePerWeek);
		  
		  equipment.setName(newName);
		  equipment.setWeight(newWeight);
		  equipment.setPricePerWeek(newPricePerWeek);
		  try {
		        ClimbSafePersistence.save();					// Update the persistence layer
		      } catch (RuntimeException e) {
		        throw new InvalidInputException(e.getMessage());
		        }
	  }
	  else throw new InvalidInputException("The piece of equipment does not exist");
  }
  
  /**
   * Performs a validation check for the price per week of the equipment.
   * @author FrederikMartin
   * @param pricePerWeek
   * @throws InvalidInputException
   */
  private static void checkPricePerWeek(int pricePerWeek) throws InvalidInputException {
	  if(pricePerWeek<0) {
		  //throw new InvalidInputException("Equipment expects a pricePerWeek greater or equal to 0. Received: " + pricePerWeek);
		  throw new InvalidInputException("The price per week must be greater than or equal to 0");
	  }	  
  }
  
  /**
   * Performs a validation check for the weight of the equipment.
   * @author FrederikMartin
   * @param weight
   * @throws InvalidInputException
   */
  private static void checkWeight(int weight) throws InvalidInputException {
	  if(weight<=0) {
		  //throw new InvalidInputException("Equipment expects a weight greater than 0. Received: " + weight);
		  throw new InvalidInputException("The weight must be greater than 0");
	  }
  }
  
  /**
   * Performs a validation check to verify the name of the equipment is not already in the system.
   * @author FrederikMartin
   * @param name
   * @throws InvalidInputException
   */
  private static void checkName(String name) throws InvalidInputException {
	  
	  if(!VerifyString(name)) {
		  throw new InvalidInputException("The name must not be empty");
	  }
	  BookableItem item = BookableItem.getWithName(name);
	  if(item != null) {
			  throw new InvalidInputException(item instanceof Equipment ?
					  							"The piece of equipment already exists":
					  							"The equipment bundle already exists");
	  }
  }
  
  /**
   * Performs a validation check to verify the name of the equipment is not already in the system.
   * @author FrederikMartin
   * @param oldName current name of the equipment in the system
   * @param newName new name of the equipment in the system
   * @throws InvalidInputException
   */
  private static void checkName(String oldName, String newName) throws InvalidInputException {
	  
	  if(!VerifyString(newName)) {
		  throw new InvalidInputException("The name must not be empty");
	  }
	  
	  BookableItem item = BookableItem.getWithName(newName);
	  if(item != null && !oldName.equals(newName)) {
			  throw new InvalidInputException(item instanceof Equipment ?
					  							"The piece of equipment already exists":
					  							"An equipment bundle with the same name already exists");
	  }
  }
  
  /**
   * Checks if a string is not null and is not empty
   * @author FrederikMartin
   * @param s
   * @return true if the string is valid
   */
  private static boolean VerifyString(String s) {
	  if(s!=null) {	  
		  //if at least one character is not white space, the string is not empty and valid
		  for(var c: s.toCharArray()) {
			  if(!Character.isWhitespace(c)) return true;
		  }
	  }
	  return false;
  }
  
  /**
   * This method will return a list of equipments in the system as transfer objects
   * @author rambodazimi
   * @return
   */
  public static ArrayList<TOEquipment> getEquipments(){
	  
	  ClimbSafe climbSafe = ClimbSafeApplication.getClimbSafe();	

	  ArrayList<TOEquipment> equipmentList = new ArrayList<TOEquipment>();
	  
	  for(Equipment e : climbSafe.getEquipment()) {
		  var toEquipment = new TOEquipment(e.getName(), e.getWeight(), e.getPricePerWeek());
		  equipmentList.add(toEquipment);
	  }
	  return equipmentList;
  }
}

